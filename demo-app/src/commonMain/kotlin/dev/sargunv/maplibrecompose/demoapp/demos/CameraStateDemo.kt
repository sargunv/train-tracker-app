package dev.sargunv.maplibrecompose.demoapp.demos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastRoundToInt
import dev.sargunv.maplibrecompose.compose.ClickResult
import dev.sargunv.maplibrecompose.compose.MaplibreMap
import dev.sargunv.maplibrecompose.compose.rememberCameraState
import dev.sargunv.maplibrecompose.core.CameraPosition
import dev.sargunv.maplibrecompose.demoapp.DEFAULT_STYLE
import io.github.dellisd.spatialk.geojson.Position
import kotlin.math.pow

private val CHICAGO = Position(latitude = 41.878, longitude = -87.626)

@Composable
fun CameraStateDemo() = Column {
  val cameraState =
    rememberCameraState(firstPosition = CameraPosition(target = CHICAGO, zoom = 12.0))

  MaplibreMap(
    modifier = Modifier.weight(1f),
    styleUrl = DEFAULT_STYLE,
    cameraState = cameraState,
    onMapClick = { _, _ ->
      println(cameraState.queryVisibleBoundingBox())
      ClickResult.Pass
    },
  )

  Row(modifier = Modifier.safeDrawingPadding().wrapContentSize(Alignment.Center)) {
    val pos = cameraState.position
    Cell("Latitude", pos.target.latitude.format(3), Modifier.weight(1.4f))
    Cell("Longitude", pos.target.longitude.format(3), Modifier.weight(1.4f))
    Cell("Zoom", pos.zoom.format(2), Modifier.weight(1f))
    Cell("Bearing", pos.bearing.format(2), Modifier.weight(1f))
    Cell("Tilt", pos.tilt.format(2), Modifier.weight(1f))
  }
}

@Composable
private fun Cell(title: String, value: String, modifier: Modifier = Modifier) {
  Column(modifier = modifier.padding(PaddingValues(4.dp)).wrapContentSize(Alignment.Center)) {
    Text(
      text = title,
      textAlign = TextAlign.Center,
      maxLines = 1,
      style = MaterialTheme.typography.labelSmall,
    )
    Text(
      text = value,
      textAlign = TextAlign.Center,
      maxLines = 1,
      style = MaterialTheme.typography.bodySmall,
    )
  }
}

private fun Double.format(decimals: Int): String {
  val factor = 10.0.pow(decimals)
  return ((this * factor).fastRoundToInt() / factor).toString()
}
