package dev.sargunv.maplibrecompose.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.viewinterop.AndroidView
import co.touchlab.kermit.Logger
import dev.sargunv.maplibrecompose.core.AndroidMap
import dev.sargunv.maplibrecompose.core.MaplibreMap
import org.maplibre.android.MapLibre
import org.maplibre.android.maps.MapView

@Composable
internal actual fun ComposableMapView(
  modifier: Modifier,
  styleUri: String,
  update: (map: MaplibreMap) -> Unit,
  onReset: () -> Unit,
  logger: Logger?,
  callbacks: MaplibreMap.Callbacks,
) {
  AndroidMapView(
    modifier = modifier,
    styleUri = styleUri,
    update = update,
    onReset = onReset,
    logger = logger,
    callbacks = callbacks,
  )
}

@Composable
internal fun AndroidMapView(
  modifier: Modifier,
  styleUri: String,
  update: (map: MaplibreMap) -> Unit,
  onReset: () -> Unit,
  logger: Logger?,
  callbacks: MaplibreMap.Callbacks,
) {
  val layoutDir = LocalLayoutDirection.current
  val density = LocalDensity.current
  val currentOnReset by rememberUpdatedState(onReset)

  var currentMapView by remember { mutableStateOf<MapView?>(null) }
  var currentMap by remember { mutableStateOf<AndroidMap?>(null) }

  MapViewLifecycleEffect(currentMapView)

  AndroidView(
    modifier = modifier,
    factory = { context ->
      MapLibre.getInstance(context)
      MapView(context).also { mapView ->
        currentMapView = mapView
        mapView.getMapAsync { map ->
          currentMap =
            AndroidMap(
              mapView = mapView,
              map = map,
              layoutDir = layoutDir,
              density = density,
              callbacks = callbacks,
              styleUri = styleUri,
              logger = logger,
            )
        }
      }
    },
    update = { _ ->
      val map = currentMap ?: return@AndroidView
      map.layoutDir = layoutDir
      map.density = density
      map.callbacks = callbacks
      map.styleUri = styleUri
      map.logger = logger
      update(map)
    },
    onReset = {
      currentOnReset()
      currentMap = null
      currentMapView = null
    },
  )
}
