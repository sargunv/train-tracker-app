package dev.sargunv.maplibrecompose.material3.controls

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import dev.sargunv.maplibrecompose.compose.StyleState

@Composable
public fun AttributionButton(
  styleState: StyleState,
  modifier: Modifier = Modifier,
  colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
  contentDescription: String = "Attribution",
  confirmText: String = "OK",
) {
  var showDialog by remember { mutableStateOf(false) }

  IconButton(modifier = modifier, colors = colors, onClick = { showDialog = true }) {
    Icon(Icons.Outlined.Info, contentDescription = contentDescription)
  }

  if (showDialog) {
    val attributions = remember(styleState) { styleState.queryAttributionLinks() }
    val uriHandler = LocalUriHandler.current

    AlertDialog(
      onDismissRequest = { showDialog = false },
      confirmButton = {
        TextButton(onClick = { showDialog = false }) {
          Text(text = confirmText, style = MaterialTheme.typography.labelLarge)
        }
      },
      title = { Text(text = "MapLibre Compose", style = MaterialTheme.typography.headlineSmall) },
      text = {
        Column {
          attributions.forEach {
            TextButton(onClick = { uriHandler.openUri(it.url) }) { Text(text = it.title) }
          }
        }
      },
    )
  }
}
