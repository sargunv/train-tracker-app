package dev.sargunv.maplibrekmp.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.touchlab.kermit.Logger
import dev.sargunv.maplibrekmp.core.MaplibreMap

@Composable
internal expect fun ComposableMapView(
  modifier: Modifier,
  styleUrl: String,
  update: (map: MaplibreMap) -> Unit,
  onReset: () -> Unit,
  logger: Logger?,
  callbacks: MaplibreMap.Callbacks,
)
