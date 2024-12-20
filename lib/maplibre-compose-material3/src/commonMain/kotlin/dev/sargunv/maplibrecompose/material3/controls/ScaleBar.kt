package dev.sargunv.maplibrecompose.material3.controls

// Based on the scale bar from Google Maps Compose.

// Copyright 2022 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import dev.sargunv.maplibrecompose.compose.CameraState
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

public data class ScaleBarColors(
  val textColor: Color,
  val lineColor: Color,
  val shadowColor: Color,
)

public object ScaleBarDefaults {
  public val width: Dp = 65.dp
  public val height: Dp = 50.dp

  @Composable
  public fun colors(): ScaleBarColors =
    ScaleBarColors(
      textColor = MaterialTheme.colorScheme.onSurface,
      lineColor = MaterialTheme.colorScheme.onSurface,
      shadowColor = MaterialTheme.colorScheme.surface,
    )
}

/**
 * A scale bar composable that shows the current scale of the map in feet and meters when zoomed in
 * to the map, changing to miles and kilometers, respectively, when zooming out.
 */
@Composable
public fun ScaleBar(
  cameraState: CameraState,
  modifier: Modifier = Modifier,
  width: Dp = ScaleBarDefaults.width,
  height: Dp = ScaleBarDefaults.height,
  colors: ScaleBarColors = ScaleBarDefaults.colors(),
) {
  Box(modifier = modifier.size(width = width, height = height)) {
    var horizontalLineWidthMeters by remember { mutableDoubleStateOf(0.0) }

    Canvas(
      modifier = Modifier.fillMaxSize(),
      onDraw = {
        horizontalLineWidthMeters = cameraState.metersPerDpAtTarget * size.width.toDp().value

        val midHeight = size.height / 2
        val oneThirdHeight = size.height / 3
        val twoThirdsHeight = size.height * 2 / 3
        val strokeWidth = 4f
        val shadowStrokeWidth = strokeWidth + 3

        // Middle horizontal line shadow (drawn under main lines)
        drawLine(
          color = colors.shadowColor,
          start = Offset(0f, midHeight),
          end = Offset(size.width, midHeight),
          strokeWidth = shadowStrokeWidth,
          cap = StrokeCap.Round,
        )
        // Top vertical line shadow (drawn under main lines)
        drawLine(
          color = colors.shadowColor,
          start = Offset(0f, oneThirdHeight),
          end = Offset(0f, midHeight),
          strokeWidth = shadowStrokeWidth,
          cap = StrokeCap.Round,
        )
        // Bottom vertical line shadow (drawn under main lines)
        drawLine(
          color = colors.shadowColor,
          start = Offset(0f, midHeight),
          end = Offset(0f, twoThirdsHeight),
          strokeWidth = shadowStrokeWidth,
          cap = StrokeCap.Round,
        )

        // Middle horizontal line
        drawLine(
          color = colors.lineColor,
          start = Offset(0f, midHeight),
          end = Offset(size.width, midHeight),
          strokeWidth = strokeWidth,
          cap = StrokeCap.Round,
        )
        // Top vertical line
        drawLine(
          color = colors.lineColor,
          start = Offset(0f, oneThirdHeight),
          end = Offset(0f, midHeight),
          strokeWidth = strokeWidth,
          cap = StrokeCap.Round,
        )
        // Bottom vertical line
        drawLine(
          color = colors.lineColor,
          start = Offset(0f, midHeight),
          end = Offset(0f, twoThirdsHeight),
          strokeWidth = strokeWidth,
          cap = StrokeCap.Round,
        )
      },
    )
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceAround) {
      var metricUnits = "m"
      var metricDistance = horizontalLineWidthMeters
      if (horizontalLineWidthMeters > METERS_IN_KILOMETER) {
        // Switch from meters to kilometers as unit
        metricUnits = "km"
        metricDistance /= METERS_IN_KILOMETER.toInt()
      }

      var imperialUnits = "ft"
      var imperialDistance = horizontalLineWidthMeters.toFeet()
      if (imperialDistance > FEET_IN_MILE) {
        // Switch from ft to miles as unit
        imperialUnits = "mi"
        imperialDistance = imperialDistance.toMiles()
      }

      ScaleText(
        text = "${imperialDistance.roundToInt()} $imperialUnits",
        modifier = Modifier.align(End),
        textColor = colors.textColor,
        shadowColor = colors.shadowColor,
      )
      ScaleText(
        text = "${metricDistance.roundToInt()} $metricUnits",
        modifier = Modifier.align(End),
        textColor = colors.textColor,
        shadowColor = colors.shadowColor,
      )
    }
  }
}

/**
 * An animated scale bar that appears when the zoom level of the map changes, and then disappears
 * after [visibilityDuration]. This composable wraps [ScaleBar] with visibility animations.
 */
@Composable
public fun DisappearingScaleBar(
  cameraState: CameraState,
  modifier: Modifier = Modifier,
  width: Dp = ScaleBarDefaults.width,
  height: Dp = ScaleBarDefaults.height,
  colors: ScaleBarColors = ScaleBarDefaults.colors(),
  visibilityDuration: Duration = 3.seconds,
  enterTransition: EnterTransition = fadeIn(),
  exitTransition: ExitTransition = fadeOut(),
) {
  val visible = remember { MutableTransitionState(true) }

  LaunchedEffect(key1 = cameraState.position.zoom) {
    // Show ScaleBar
    visible.targetState = true
    delay(visibilityDuration)
    // Hide ScaleBar after timeout period
    visible.targetState = false
  }

  AnimatedVisibility(
    visibleState = visible,
    modifier = modifier,
    enter = enterTransition,
    exit = exitTransition,
  ) {
    ScaleBar(width = width, height = height, cameraState = cameraState, colors = colors)
  }
}

@Composable
private fun ScaleText(text: String, modifier: Modifier, textColor: Color, shadowColor: Color) {
  Text(
    text = text,
    fontSize = 12.sp,
    color = textColor,
    textAlign = TextAlign.End,
    lineHeight = 1.em,
    modifier = modifier,
    style =
      MaterialTheme.typography.headlineSmall.copy(
        shadow = Shadow(color = shadowColor, offset = Offset(2f, 2f), blurRadius = 1f)
      ),
  )
}

/**
 * Converts [this] value in meters to the corresponding value in feet
 *
 * @return [this] meters value converted to feet
 */
private fun Double.toFeet(): Double {
  return this * CENTIMETERS_IN_METER / CENTIMETERS_IN_INCH / INCHES_IN_FOOT
}

/**
 * Converts [this] value in feet to the corresponding value in miles
 *
 * @return [this] feet value converted to miles
 */
private fun Double.toMiles(): Double {
  return this / FEET_IN_MILE
}

private const val CENTIMETERS_IN_METER: Double = 100.0
private const val METERS_IN_KILOMETER: Double = 1000.0
private const val CENTIMETERS_IN_INCH: Double = 2.54
private const val INCHES_IN_FOOT: Double = 12.0
private const val FEET_IN_MILE: Double = 5280.0
