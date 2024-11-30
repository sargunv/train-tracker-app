package dev.sargunv.maplibrecompose.demoapp.demos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.sargunv.maplibrecompose.compose.MaplibreMap
import dev.sargunv.maplibrecompose.core.util.PlatformUtils
import dev.sargunv.maplibrecompose.demoapp.DEFAULT_STYLE
import kotlin.math.roundToInt

@Composable
fun FrameRateDemo() = Column {
  val systemRefreshRate = PlatformUtils.getSystemRefreshRate().roundToInt()
  var maximumFps by remember { mutableStateOf(systemRefreshRate) }

  MaplibreMap(modifier = Modifier.weight(1f), styleUrl = DEFAULT_STYLE, maximumFps = maximumFps)

  Column(modifier = Modifier.padding(16.dp)) {
    Slider(
      value = maximumFps.toFloat(),
      onValueChange = { maximumFps = it.roundToInt() },
      valueRange = 15f..systemRefreshRate.toFloat(),
    )
    Text("$maximumFps", style = MaterialTheme.typography.labelMedium)
  }
}
