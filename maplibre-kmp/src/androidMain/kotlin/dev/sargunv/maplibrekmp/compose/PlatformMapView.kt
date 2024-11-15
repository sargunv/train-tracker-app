package dev.sargunv.maplibrekmp.compose

import android.graphics.PointF
import android.view.Gravity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.sargunv.maplibrekmp.core.LatLng
import dev.sargunv.maplibrekmp.core.PlatformMap
import dev.sargunv.maplibrekmp.core.Style
import dev.sargunv.maplibrekmp.core.correctedAndroidUri
import org.maplibre.android.MapLibre
import org.maplibre.android.maps.MapView
import org.maplibre.geojson.Feature

@Composable
internal actual fun PlatformMapView(
  modifier: Modifier,
  uiPadding: PaddingValues,
  styleUrl: String,
  updateMap: (map: PlatformMap) -> Unit,
  onMapLoaded: (map: PlatformMap) -> Unit,
  onStyleLoaded: (style: Style) -> Unit,
  onRelease: () -> Unit,
  onCameraMove: () -> Unit,
  onClick: (pos: LatLng) -> Unit,
  onLongClick: (pos: LatLng) -> Unit,
) {
  var observer by remember { mutableStateOf<LifecycleEventObserver?>(null) }

  val layoutDir = LocalLayoutDirection.current
  val density = LocalDensity.current

  val margins =
    remember(uiPadding, layoutDir, density) {
      with(density) {
        listOf(
          uiPadding.calculateLeftPadding(layoutDir).roundToPx(),
          uiPadding.calculateTopPadding().roundToPx(),
          uiPadding.calculateRightPadding(layoutDir).roundToPx(),
          uiPadding.calculateBottomPadding().roundToPx(),
        )
      }
    }

  var lastStyleUrl by remember { mutableStateOf<String?>(null) }
  var lastMargins by remember { mutableStateOf<List<Int>?>(null) }

  val currentOnStyleLoaded by rememberUpdatedState(onStyleLoaded)
  val currentOnRelease by rememberUpdatedState(onRelease)
  val currentOnCameraMove by rememberUpdatedState(onCameraMove)
  val currentOnClick by rememberUpdatedState(onClick)
  val currentOnLongClick by rememberUpdatedState(onLongClick)

  var platformMap by remember { mutableStateOf<PlatformMap?>(null) }
  SideEffect { platformMap?.layoutDirection = layoutDir }

  AndroidView(
    modifier = modifier,
    factory = { context ->
      MapLibre.getInstance(context)
      MapView(context).also { mapView ->
        observer = LifecycleEventObserver { _, event ->
          when (event) {
            Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
            Lifecycle.Event.ON_START -> mapView.onStart()
            Lifecycle.Event.ON_RESUME -> mapView.onResume()
            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            Lifecycle.Event.ON_STOP -> mapView.onStop()
            Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
            else -> throw IllegalStateException()
          }
        }

        mapView.getMapAsync { map ->
          map.uiSettings.attributionGravity = Gravity.BOTTOM or Gravity.END

          platformMap = PlatformMap(map)
          onMapLoaded(platformMap!!)

          map.addOnCameraMoveListener { currentOnCameraMove() }

          map.addOnMapClickListener { coords ->
            val point = map.projection.toScreenLocation(coords)
            val queryRenderedFeatures: (PointF, Array<String>?) -> List<Feature> =
              map::queryRenderedFeatures
            val features = queryRenderedFeatures(point, null).map { it.toJson() }
            println("Clicked: $features")
            currentOnClick(LatLng(coords.latitude, coords.longitude))
            false
          }

          map.addOnMapLongClickListener { coords ->
            currentOnLongClick(LatLng(coords.latitude, coords.longitude))
            false
          }
        }
      }
    },
    update = { mapView ->
      platformMap?.let { updateMap(it) }

      mapView.getMapAsync { map ->
        if (margins != lastMargins) {
          map.uiSettings.setAttributionMargins(margins[0], margins[1], margins[2], margins[3])
          map.uiSettings.setLogoMargins(margins[0], margins[1], margins[2], margins[3])
          map.uiSettings.setCompassMargins(margins[0], margins[1], margins[2], margins[3])
          lastMargins = margins
        }

        if (styleUrl != lastStyleUrl) {
          map.setStyle(styleUrl.correctedAndroidUri().toString()) {
            currentOnStyleLoaded(Style(it))
          }
          lastStyleUrl = styleUrl
        }
      }
    },
    onRelease = { currentOnRelease() },
  )

  val lifecycle = LocalLifecycleOwner.current.lifecycle
  DisposableEffect(lifecycle, observer) {
    observer?.let { lifecycle.addObserver(it) }
    onDispose { observer?.let { lifecycle.removeObserver(it) } }
  }
}