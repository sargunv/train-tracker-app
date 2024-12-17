package dev.sargunv.maplibrecompose.compose.controls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dev.sargunv.maplibrecompose.compose.StyleState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
public fun AttributionDialog(
  styleState: StyleState,
  onDismiss: () -> Unit,
  titleText: String = "Data sources",
  buttonText: String = "OK",
) {
  val attributions = remember(styleState) { styleState.queryAttributionLinks() }
  val uriHandler = LocalUriHandler.current

  BasicAlertDialog(
    onDismissRequest = onDismiss,
    content = {
      Surface(
        modifier = Modifier.wrapContentWidth().wrapContentHeight().widthIn(min = 280.dp),
        shape = MaterialTheme.shapes.extraLarge,
        tonalElevation = AlertDialogDefaults.TonalElevation,
        contentColor = AlertDialogDefaults.textContentColor,
      ) {
        Column(modifier = Modifier.padding(24.dp)) {
          Text(
            text = titleText,
            modifier = Modifier.padding(bottom = 16.dp).semantics { heading() },
            style = MaterialTheme.typography.headlineSmall,
          )

          attributions.forEach {
            TextButton(onClick = { uriHandler.openUri(it.url) }) { Text(text = it.title) }
          }

          TextButton(
            onClick = onDismiss,
            modifier = Modifier.padding(top = 24.dp).align(Alignment.End),
          ) {
            Text(text = buttonText, style = MaterialTheme.typography.labelLarge)
          }
        }
      }
    },
  )
}
