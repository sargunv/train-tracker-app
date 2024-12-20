package dev.sargunv.maplibrecompose.material3.controls

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.sargunv.maplibrecompose.compose.CameraState
import dev.sargunv.maplibrecompose.material3.generated.Res
import dev.sargunv.maplibrecompose.material3.generated.compass_needle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
public fun CompassButton(
  cameraState: CameraState,
  modifier: Modifier = Modifier,
  coroutineScope: CoroutineScope = rememberCoroutineScope(),
  onClick: () -> Unit = {
    coroutineScope.launch {
      cameraState.animateTo(cameraState.position.copy(bearing = 0.0, tilt = 0.0))
    }
  },
  colors: ButtonColors = ButtonDefaults.elevatedButtonColors(),
  contentDescription: String = "Compass",
) {
  ElevatedButton(
    modifier = modifier.size(48.dp).aspectRatio(1f),
    onClick = onClick,
    shape = CircleShape,
    colors = colors,
    contentPadding = PaddingValues(8.dp),
  ) {
    Image(
      painter = painterResource(Res.drawable.compass_needle),
      contentDescription = contentDescription,
      modifier =
        Modifier.fillMaxSize()
          .graphicsLayer(
            rotationZ = -cameraState.position.bearing.toFloat(),
            rotationX = cameraState.position.tilt.toFloat(),
          ),
    )
  }
}
