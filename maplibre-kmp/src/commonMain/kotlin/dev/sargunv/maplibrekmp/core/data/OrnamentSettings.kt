package dev.sargunv.maplibrekmp.core.data

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

public data class OrnamentSettings(
  val padding: PaddingValues = PaddingValues(8.dp),
  val isLogoEnabled: Boolean = true,
  val logoAlignment: Alignment = Alignment.BottomStart,
  val isAttributionEnabled: Boolean = true,
  val attributionAlignment: Alignment = Alignment.BottomEnd,
  val isCompassEnabled: Boolean = true,
  val compassAlignment: Alignment = Alignment.TopEnd,
)
