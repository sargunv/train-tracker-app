package dev.sargunv.maplibrecompose.compose.controls

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.sargunv.maplibrecompose.compose.StyleState

@Composable
public fun AttributionButton(
  styleState: StyleState,
  modifier: Modifier = Modifier,
  colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
  contentDescription: String = "Attribution",
) {
  var showDialog by remember { mutableStateOf(false) }

  IconButton(modifier = modifier, colors = colors, onClick = { showDialog = true }) {
    Icon(Icons.Outlined.Info, contentDescription = contentDescription)
  }

  if (showDialog) {
    AttributionDialog(styleState, onDismiss = { showDialog = false })
  }
}
