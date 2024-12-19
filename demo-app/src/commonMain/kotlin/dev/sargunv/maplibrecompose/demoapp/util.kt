package dev.sargunv.maplibrecompose.demoapp

import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.fastRoundToInt
import dev.sargunv.maplibrecompose.demoapp.generated.Res
import io.github.dellisd.spatialk.geojson.Position
import kotlin.math.pow
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.ExperimentalResourceApi

interface Demo {
  val name: String
  val description: String

  @Composable fun Component(navigateUp: () -> Unit)
}

private val REMOTE_STYLE_URIS =
  listOf(
    "Bright" to "https://tiles.openfreemap.org/styles/bright",
    "Liberty" to "https://tiles.openfreemap.org/styles/liberty",
    "Positron" to "https://tiles.openfreemap.org/styles/positron",
    "Fiord" to "https://tiles.openfreemap.org/styles/fiord",
    "Dark" to "https://tiles.openfreemap.org/styles/dark",
  )

private val LOCAL_STYLE_PATHS =
  listOf(
    // from https://tiles.versatiles.org/assets/styles/colorful.json
    "Colorful" to "files/styles/colorful.json",
    // from https://tiles.versatiles.org/assets/styles/eclipse.json
    "Eclipse" to "files/styles/eclipse.json",
  )

val DEFAULT_STYLE = REMOTE_STYLE_URIS[0].second

@OptIn(ExperimentalResourceApi::class)
fun getAllStyleUris() =
  REMOTE_STYLE_URIS + LOCAL_STYLE_PATHS.map { it.first to Res.getUri(it.second) }

/** Caution: this converter results in a loss of precision far from the origin. */
class PositionVectorConverter(private val origin: Position) :
  TwoWayConverter<Position, AnimationVector2D> {
  override val convertFromVector: (AnimationVector2D) -> Position = { vector ->
    Position(
      longitude = vector.v1.toDouble() + origin.longitude,
      latitude = vector.v2.toDouble() + origin.latitude,
    )
  }

  override val convertToVector: (Position) -> AnimationVector2D = { pos ->
    val dLon = pos.longitude - origin.longitude
    val dLat = pos.latitude - origin.latitude
    AnimationVector2D(dLon.toFloat(), dLat.toFloat())
  }
}

internal fun Double.format(decimals: Int): String {
  val factor = 10.0.pow(decimals)
  return ((this * factor).fastRoundToInt() / factor).toString()
}

internal class FrameRateState(private val spinner: String = "◐◓◑◒") {
  private var rollingAverage by mutableStateOf(0.0)
  private var spinnerIndex by mutableStateOf(0)

  fun recordFps(framesPerSecond: Double) {
    rollingAverage = (rollingAverage * 0.9) + (framesPerSecond * 0.1)
    spinnerIndex = (spinnerIndex + 1) % spinner.length
  }

  val spinChar: Char
    get() = spinner[spinnerIndex]

  val avgFps: Int
    get() = rollingAverage.roundToInt()
}
