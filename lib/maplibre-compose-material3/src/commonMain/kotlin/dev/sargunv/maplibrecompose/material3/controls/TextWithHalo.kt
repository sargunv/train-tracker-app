package dev.sargunv.maplibrecompose.material3.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

/** A text that has a halo. */
@Composable
internal fun TextWithHalo(
  text: String,
  modifier: Modifier = Modifier,
  haloColor: Color = MaterialTheme.colorScheme.surface,
  haloWidth: Dp = 1.dp,
  color: Color = contentColorFor(haloColor),
  fontSize: TextUnit = TextUnit.Unspecified,
  fontStyle: FontStyle? = null,
  fontWeight: FontWeight? = null,
  fontFamily: FontFamily? = null,
  letterSpacing: TextUnit = TextUnit.Unspecified,
  textDecoration: TextDecoration? = null,
  textAlign: TextAlign? = null,
  lineHeight: TextUnit = TextUnit.Unspecified,
  overflow: TextOverflow = TextOverflow.Clip,
  softWrap: Boolean = true,
  maxLines: Int = Int.MAX_VALUE,
  minLines: Int = 1,
  onTextLayout: ((TextLayoutResult) -> Unit)? = null,
  style: TextStyle = LocalTextStyle.current,
) {
  // * 2 because the stroke is painted half outside and half inside of the text shape
  val strokeWidth = with(LocalDensity.current) { haloWidth.toPx() }
  val stroke = Stroke(strokeWidth * 2, cap = StrokeCap.Round, join = StrokeJoin.Round)
  Box {
    Text(
      text = text,
      modifier = modifier,
      color = haloColor,
      fontSize = fontSize,
      fontStyle = fontStyle,
      fontWeight = fontWeight,
      fontFamily = fontFamily,
      letterSpacing = letterSpacing,
      textDecoration = textDecoration,
      textAlign = textAlign,
      lineHeight = lineHeight,
      overflow = overflow,
      softWrap = softWrap,
      maxLines = maxLines,
      minLines = minLines,
      onTextLayout = onTextLayout,
      style = style.copy(drawStyle = stroke),
    )
    Text(
      text = text,
      modifier = modifier,
      color = color,
      fontSize = fontSize,
      fontStyle = fontStyle,
      fontWeight = fontWeight,
      fontFamily = fontFamily,
      letterSpacing = letterSpacing,
      textDecoration = textDecoration,
      textAlign = textAlign,
      lineHeight = lineHeight,
      overflow = overflow,
      softWrap = softWrap,
      maxLines = maxLines,
      minLines = minLines,
      onTextLayout = null,
      style = style,
    )
  }
}
