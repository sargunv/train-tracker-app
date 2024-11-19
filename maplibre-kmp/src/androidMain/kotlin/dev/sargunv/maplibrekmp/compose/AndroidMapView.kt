package dev.sargunv.maplibrekmp.compose

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
import dev.sargunv.maplibrekmp.core.AndroidMap
import dev.sargunv.maplibrekmp.core.MaplibreMap
import org.maplibre.android.MapLibre
import org.maplibre.android.maps.MapView

@Composable
internal actual fun ComposableMapView(
  modifier: Modifier,
  styleUrl: String,
  update: (map: MaplibreMap) -> Unit,
  onReset: () -> Unit,
  logger: Logger?,
  callbacks: MaplibreMap.Callbacks,
) {
  AndroidMapView(
    modifier = modifier,
    styleUrl = styleUrl,
    update = update,
    onReset = onReset,
    logger = logger,
    callbacks = callbacks,
  )
}

@Composable
internal fun AndroidMapView(
  modifier: Modifier,
  styleUrl: String,
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
              map = map,
              layoutDir = layoutDir,
              density = density,
              callbacks = callbacks,
              styleUrl = styleUrl,
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
      map.styleUrl = styleUrl
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
