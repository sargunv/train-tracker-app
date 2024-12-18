package dev.sargunv.maplibrecompose.compose.controls

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler
import dev.sargunv.maplibrecompose.compose.StyleState

@Composable
public fun AttributionDialog(
  styleState: StyleState,
  onDismiss: () -> Unit,
  titleText: String = "Data sources",
  buttonText: String = "OK",
) {
  val attributions = remember(styleState) { styleState.queryAttributionLinks() }
  val uriHandler = LocalUriHandler.current

  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text(text = buttonText, style = MaterialTheme.typography.labelLarge)
      }
    },
    title = { Text(text = titleText, style = MaterialTheme.typography.headlineSmall) },
    text = {
      Column {
        attributions.forEach {
          TextButton(onClick = { uriHandler.openUri(it.url) }) { Text(text = it.title) }
        }
      }
    },
  )
}
