package dev.sargunv.maplibrecompose.compose.layer

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import dev.sargunv.maplibrecompose.compose.FeaturesClickHandler
import dev.sargunv.maplibrecompose.core.expression.BooleanValue
import dev.sargunv.maplibrecompose.core.expression.ColorValue
import dev.sargunv.maplibrecompose.core.expression.Defaults
import dev.sargunv.maplibrecompose.core.expression.DpOffsetValue
import dev.sargunv.maplibrecompose.core.expression.DpValue
import dev.sargunv.maplibrecompose.core.expression.EnumValue
import dev.sargunv.maplibrecompose.core.expression.Expression
import dev.sargunv.maplibrecompose.core.expression.ExpressionScope.const
import dev.sargunv.maplibrecompose.core.expression.ExpressionScope.nil
import dev.sargunv.maplibrecompose.core.expression.FloatValue
import dev.sargunv.maplibrecompose.core.expression.FormattedValue
import dev.sargunv.maplibrecompose.core.expression.IconPitchAlignment
import dev.sargunv.maplibrecompose.core.expression.IconRotationAlignment
import dev.sargunv.maplibrecompose.core.expression.IconTextFit
import dev.sargunv.maplibrecompose.core.expression.ImageValue
import dev.sargunv.maplibrecompose.core.expression.ListValue
import dev.sargunv.maplibrecompose.core.expression.OffsetValue
import dev.sargunv.maplibrecompose.core.expression.PaddingValue
import dev.sargunv.maplibrecompose.core.expression.StringValue
import dev.sargunv.maplibrecompose.core.expression.SymbolAnchor
import dev.sargunv.maplibrecompose.core.expression.SymbolPlacement
import dev.sargunv.maplibrecompose.core.expression.SymbolZOrder
import dev.sargunv.maplibrecompose.core.expression.TextJustify
import dev.sargunv.maplibrecompose.core.expression.TextPitchAlignment
import dev.sargunv.maplibrecompose.core.expression.TextRotationAlignment
import dev.sargunv.maplibrecompose.core.expression.TextTransform
import dev.sargunv.maplibrecompose.core.expression.TextWritingMode
import dev.sargunv.maplibrecompose.core.expression.TranslateAnchor
import dev.sargunv.maplibrecompose.core.expression.ZeroPadding
import dev.sargunv.maplibrecompose.core.layer.SymbolLayer
import dev.sargunv.maplibrecompose.core.source.Source
import dev.sargunv.maplibrecompose.core.util.JsOnlyApi

/**
 * A symbol layer draws data from the [sourceLayer] in the given [source] as icons and/or text
 * labels.
 *
 * @param id Unique layer name.
 * @param source Vector data source for this layer.
 * @param sourceLayer Layer to use from the given vector tile [source].
 * @param minZoom The minimum zoom level for the layer. At zoom levels less than this, the layer
 *   will be hidden. A value in the range of `[0..24]`.
 * @param maxZoom The maximum zoom level for the layer. At zoom levels equal to or greater than
 *   this, the layer will be hidden. A value in the range of `[0..24]`.
 * @param filter An expression specifying conditions on source features. Only features that match
 *   the filter are displayed. Zoom expressions in filters are only evaluated at integer zoom
 *   levels. The
 *   [featureState][dev.sargunv.maplibrecompose.core.expression.ExpressionScope.featureState]
 *   expression is not supported in filter expressions.
 * @param visible Whether the layer should be displayed.
 * @param sortKey Sorts features within this layer in ascending order based on this value. Features
 *   with a higher sort key will appear above features with a lower sort key.
 * @param placement Symbol placement relative to its geometry.
 * @param spacing Distance between two symbol anchors.
 *
 *   Only applicable when [placement] is [SymbolPlacement.Line].
 *
 * @param avoidEdges If true, the symbols will not cross tile edges to avoid mutual collisions.
 *   Recommended in layers that don't have enough padding in the vector tile to prevent collisions,
 *   or if it is a point symbol layer placed after a line symbol layer.
 * @param zOrder Determines whether overlapping symbols in the same layer are rendered in the order
 *   that they appear in the data source or by their y-position relative to the viewport. To control
 *   the order and prioritization of symbols otherwise, use [sortKey].
 * @param iconImage Image to use for drawing an image background.
 * @param iconOpacity The opacity at which the icon will be drawn. A value in the range `[0..1]`.
 *
 *   Ignored if [iconImage] is not specified.
 *
 * @param iconColor The color of the icon. This can only be used with SDF icons.
 *
 *   Ignored if [iconImage] is not specified.
 *
 * @param iconHaloColor The color of the icon's halo. Icon halos can only be used with SDF icons.
 *
 *   Ignored if [iconImage] is not specified.
 *
 * @param iconHaloWidth Distance of halo to the icon outline. The unit is in dp only for SDF sprites
 *   that were created with a blur radius of 8, multiplied by the display density. I.e., the radius
 *   needs to be 16 for @2x sprites, etc.
 *
 *   Ignored if [iconImage] is not specified.
 *
 * @param iconHaloBlur Fade out the halo towards the outside.
 *
 *   Ignored if [iconImage] is not specified.
 *
 * @param iconSize Scales the original size of the icon by the provided factor. The new pixel size
 *   of the image will be the original pixel size multiplied by [iconSize]. 1 is the original size;
 *   3 triples the size of the image.
 *
 *   Ignored if [iconImage] is not specified.
 *
 * @param iconRotationAlignment In combination with [placement], determines the rotation behavior of
 *   icons.
 *
 *   Ignored if [iconImage] is not specified.
 *
 * @param iconPitchAlignment Orientation of icon when map is pitched.
 *
 *   Ignored if [iconImage] is not specified.
 *
 * @param iconTextFit Scales the icon to fit around the associated text.
 *
 *   Ignored if not both [iconImage] and [textField] are specified.
 *
 * @param iconTextFitPadding Size of the additional area added to dimensions determined by
 *   [iconTextFit].
 *
 *   Only applicable when if [iconTextFit] is not [IconTextFit.None]. Ignored if not both
 *   [iconImage] and [textField] are specified.
 *
 * @param iconKeepUpright If true, the icon may be flipped to prevent it from being rendered
 *   upside-down.
 *
 *   Only applicable if [iconRotationAlignment] is [IconRotationAlignment.Map] and [placement] is
 *   either [SymbolPlacement.Line] or [SymbolPlacement.LineCenter].
 *
 *   Ignored if [iconImage] is not specified.
 *
 * @param iconRotate Rotates the icon clockwise by the number in degrees.
 *
 *   Ignored if [iconImage] is not specified.
 *
 * @param iconAnchor Part of the icon placed closest to the anchor.
 *
 *   Ignored if [iconImage] is not specified.
 *
 * @param iconOffset Offset distance of icon from its anchor. Positive values indicate right and
 *   down, while negative values indicate left and up. Each component is multiplied by the value of
 *   [iconSize] to obtain the final offset in dp. When combined with [iconRotate] the offset will be
 *   as if the rotated direction was up.
 *
 *   Ignored if [iconImage] is not specified.
 *
 * @param iconPadding Size of additional area round the icon bounding box used for detecting symbol
 *   collisions.
 *
 *   Ignored if [iconImage] is not specified.
 *
 * @param iconAllowOverlap If true, the icon will be visible even if it collides with other
 *   previously drawn symbols.
 *
 *   Ignored if [iconImage] is not specified, overridden by [iconOverlap], if specified.
 *
 * @param iconOverlap Controls whether to show an icon when it overlaps other symbols on the map.
 *
 *   Ignored if [iconImage] is not specified.
 *
 *   **Note**: This property is not supported on native platforms yet, see
 *   [maplibre-native#251](https://github.com/maplibre/maplibre-native/issues/251)**
 *
 * @param iconIgnorePlacement If true, other symbols can be visible even if they collide with the
 *   icon.
 *
 *   Ignored if [iconImage] is not specified.
 *
 * @param iconOptional If true, text will display without their corresponding icons when the icon
 *   collides with other symbols and the text does not.
 *
 *   Ignored if not both [iconImage] and [textField] are specified.
 *
 * @param iconTranslate The geometry's offset relative to the [iconTranslateAnchor]. Negative
 *   numbers indicate left and up, respectively.
 *
 *   Ignored if [iconImage] is not specified.
 *
 * @param iconTranslateAnchor Frame of reference for offsetting geometry.
 *
 *   Ignored if [iconTranslate] is not set.
 *
 * @param textField Value to use for a text label. Use e.g. `format(const("My label"))` to display
 *   the plain string "My label".
 *
 *   The text can also be formatted, employing different colors, fonts, etc., see
 *   [format][dev.sargunv.maplibrecompose.core.expression.ExpressionScope.format].
 *
 * @param textOpacity The opacity at which the text will be drawn.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textColor The color with which the text will be drawn.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textHaloColor The color of the text's halo, which helps it stand out from backgrounds.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textHaloWidth Distance of halo to the font outline. Max text halo width is 1/4 of the
 *   font-size.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textHaloBlur The halo's fadeout distance towards the outside.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textFont Font stack to use for displaying text.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textSize Font size.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textTransform Specifies how to capitalize text.
 * @param textLetterSpacing Text tracking amount in ems.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textRotationAlignment In combination with [placement], determines the rotation behavior of
 *   the individual glyphs forming the text.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textPitchAlignment Orientation of text when map is pitched.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textMaxAngle Maximum angle change in degrees between adjacent characters.
 *
 *   Only applicable if [placement] is [SymbolPlacement.Line] or [SymbolPlacement.LineCenter]
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textMaxWidth The maximum line width in ems for text wrapping.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textLineHeight Text leading value in ems for multi-line text.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textJustify Text justification options.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textWritingMode The property allows control over a symbol's orientation. Note that the
 *   property values act as a hint, so that a symbol whose language doesn’t support the provided
 *   orientation will be laid out in its natural orientation. Example: English point symbol will be
 *   rendered horizontally even if array value contains single 'vertical' enum value. The order of
 *   elements in an array define priority order for the placement of an orientation variant.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textKeepUpright If true, the text may be flipped vertically to prevent it from being
 *   rendered upside-down.
 *
 *   Only applicable if [textRotationAlignment] is [TextRotationAlignment.Map] and [placement] is
 *   [SymbolPlacement.Line] or [SymbolPlacement.LineCenter]
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textRotate Rotates the text clockwise. Unit in degrees.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textAnchor Part of the text placed closest to the anchor.
 *
 *   Overridden by [textVariableAnchorOffset].
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textOffset Offset distance of text from its anchor in ems. Positive values indicate right
 *   and down, while negative values indicate left and up. If used with [textVariableAnchor], input
 *   values will be taken as absolute values. Offsets along the x- and y-axis will be applied
 *   automatically based on the anchor position.
 *
 *   Overridden by [textRadialOffset].
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textVariableAnchor To increase the chance of placing high-priority labels on the map, you
 *   can provide an array of [textAnchor] locations: the renderer will attempt to place the label at
 *   each location, in order, before moving onto the next label. Use [textJustify] =
 *   [TextJustify.Auto] to choose justification based on anchor position. To apply an offset, use
 *   the [textRadialOffset] or the two-dimensional [textOffset].
 *
 *   Only applicable if [placement] is [SymbolPlacement.Point].
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textRadialOffset Radial offset of text in ems, in the direction of the symbol's anchor.
 *   Useful in combination with [textVariableAnchor], which defaults to using the two-dimensional
 *   [textOffset] if present.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textVariableAnchorOffset To increase the chance of placing high-priority labels on the
 *   map, you can provide an array of [SymbolAnchor] locations, each paired with an offset value.
 *   The renderer will attempt to place the label at each location, in order, before moving on to
 *   the next location+offset. Use [textJustify] = [TextJustify.Auto] to choose justification based
 *   on anchor position.
 *
 *   Each anchor location is accompanied by a point which defines the offset when the corresponding
 *   anchor location is used. Positive offset values indicate right and down, while negative values
 *   indicate left and up. Anchor locations may repeat, allowing the renderer to try multiple
 *   offsets to try and place a label using the same anchor.
 *
 *   When present, this property takes precedence over [textAnchor], [textVariableAnchor],
 *   [textOffset] and [textRadialOffset].
 *
 *   Example: ``` listOf( SymbolAnchor.Top to Point(0, 4), SymbolAnchor.Left to Point(3, 0),
 *   SymbolAnchor.Bottom to Point(1, 1) ``` When the renderer chooses the top anchor, [0, 4] will be
 *   used for [textOffset]; the text will be shifted down by 4 ems. When the renderer chooses the
 *   left anchor, [3, 0] will be used for [textOffset]; the text will be shifted right by 3 ems.
 *   Etc.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textPadding Size of the additional area in dp around the text bounding box used for
 *   detecting symbol collisions.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textAllowOverlap If true, the text will be visible even if it collides with other
 *   previously drawn symbols.
 *
 *   Overridden by [textOverlap].
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textOverlap Controls whether to show an icon/text when it overlaps other symbols on the
 *   map. See [SymbolOverlap]. Overrides [textAllowOverlap].
 *
 *   Ignored if [textField] is not specified.
 *
 *   **Note**: This property is not supported on native platforms, yet, see
 *   [maplibre-native#251](https://github.com/maplibre/maplibre-native/issues/251)**
 *
 * @param textIgnorePlacement If true, other symbols can be visible even if they collide with the
 *   text.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textOptional If true, icons will display without their corresponding text when the text
 *   collides with other symbols and the icon does not.
 *
 *   Ignored if not both [textField] and [iconImage] are specified.
 *
 * @param textTranslate Distance that the text's anchor is moved from its original placement in dp.
 *   Positive values indicate right and down, while negative values indicate left and up.
 *
 *   Ignored if [textField] is not specified.
 *
 * @param textTranslateAnchor Controls the frame of reference for [textTranslate].
 *
 *   Ignored if [textField] is not specified.
 *
 * @param onClick Function to call when any feature in this layer has been clicked.
 * @param onLongClick Function to call when any feature in this layer has been long-clicked.
 */
@OptIn(JsOnlyApi::class)
@Composable
public fun SymbolLayer(
  id: String,
  source: Source,
  sourceLayer: String = "",
  minZoom: Float = 0.0f,
  maxZoom: Float = 24.0f,
  filter: Expression<BooleanValue> = nil(),
  visible: Boolean = true,
  sortKey: Expression<FloatValue> = nil(),
  placement: Expression<EnumValue<SymbolPlacement>> = const(SymbolPlacement.Point),
  spacing: Expression<DpValue> = const(250.dp),
  avoidEdges: Expression<BooleanValue> = const(false),
  zOrder: Expression<EnumValue<SymbolZOrder>> = const(SymbolZOrder.Auto),

  // icon image
  iconImage: Expression<ImageValue> = nil(),

  // icon colors
  iconOpacity: Expression<FloatValue> = const(1f),
  iconColor: Expression<ColorValue> = const(Color.Black),
  iconHaloColor: Expression<ColorValue> = const(Color.Transparent),
  iconHaloWidth: Expression<DpValue> = const(0.dp),
  iconHaloBlur: Expression<DpValue> = const(0.dp),

  // icon layout
  iconSize: Expression<FloatValue> = const(1f),
  iconRotationAlignment: Expression<EnumValue<IconRotationAlignment>> =
    const(IconRotationAlignment.Auto),
  iconPitchAlignment: Expression<EnumValue<IconPitchAlignment>> = const(IconPitchAlignment.Auto),
  iconTextFit: Expression<EnumValue<IconTextFit>> = const(IconTextFit.None),
  iconTextFitPadding: Expression<PaddingValue> = const(ZeroPadding),
  iconKeepUpright: Expression<BooleanValue> = const(false),
  iconRotate: Expression<FloatValue> = const(0f),

  // icon anchoring
  iconAnchor: Expression<EnumValue<SymbolAnchor>> = const(SymbolAnchor.Center),
  iconOffset: Expression<DpOffsetValue> = const(DpOffset.Zero),

  // icon collision
  iconPadding: Expression<DpValue> = const(2.dp),
  iconAllowOverlap: Expression<BooleanValue> = const(false),
  iconOverlap: Expression<StringValue> = nil(),
  iconIgnorePlacement: Expression<BooleanValue> = const(false),
  iconOptional: Expression<BooleanValue> = const(false),

  // icon translate
  iconTranslate: Expression<DpOffsetValue> = const(DpOffset.Zero),
  iconTranslateAnchor: Expression<EnumValue<TranslateAnchor>> = const(TranslateAnchor.Map),

  // text content
  textField: Expression<FormattedValue> = nil(),

  // text glyph colors
  textOpacity: Expression<FloatValue> = const(1f),
  textColor: Expression<ColorValue> = const(Color.Black),
  textHaloColor: Expression<ColorValue> = const(Color.Transparent),
  textHaloWidth: Expression<DpValue> = const(0.dp),
  textHaloBlur: Expression<DpValue> = const(0.dp),

  // text glyph properties
  textFont: Expression<ListValue<StringValue>> = Defaults.FontNames,
  textSize: Expression<DpValue> = const(16.dp),
  textTransform: Expression<EnumValue<TextTransform>> = const(TextTransform.None),
  textLetterSpacing: Expression<FloatValue> = const(0f),
  textRotationAlignment: Expression<EnumValue<TextRotationAlignment>> =
    const(TextRotationAlignment.Auto),
  textPitchAlignment: Expression<EnumValue<TextPitchAlignment>> = const(TextPitchAlignment.Auto),
  textMaxAngle: Expression<FloatValue> = const(45f),

  // text paragraph layout
  textMaxWidth: Expression<FloatValue> = const(10f),
  textLineHeight: Expression<FloatValue> = const(1.2f),
  textJustify: Expression<EnumValue<TextJustify>> = const(TextJustify.Center),
  textWritingMode: Expression<ListValue<EnumValue<TextWritingMode>>> = nil(),
  textKeepUpright: Expression<BooleanValue> = const(true),
  textRotate: Expression<FloatValue> = const(0f),

  // text anchoring
  textAnchor: Expression<EnumValue<SymbolAnchor>> = const(SymbolAnchor.Center),
  textOffset: Expression<OffsetValue> = const(Offset.Zero),
  textVariableAnchor: Expression<ListValue<EnumValue<SymbolAnchor>>> = nil(),
  textRadialOffset: Expression<FloatValue> = const(0f),
  textVariableAnchorOffset: Expression<ListValue<*>> = nil(), // TODO proper type

  // text collision
  textPadding: Expression<DpValue> = const(2.dp),
  textAllowOverlap: Expression<BooleanValue> = const(false),
  textOverlap: Expression<StringValue> = nil(),
  textIgnorePlacement: Expression<BooleanValue> = const(false),
  textOptional: Expression<BooleanValue> = const(false),

  // text translate
  textTranslate: Expression<DpOffsetValue> = const(DpOffset.Zero),
  textTranslateAnchor: Expression<EnumValue<TranslateAnchor>> = const(TranslateAnchor.Map),
  onClick: FeaturesClickHandler? = null,
  onLongClick: FeaturesClickHandler? = null,
) {
  LayerNode(
    factory = { SymbolLayer(id = id, source = source) },
    update = {
      set(sourceLayer) { layer.sourceLayer = it }
      set(minZoom) { layer.minZoom = it }
      set(maxZoom) { layer.maxZoom = it }
      set(filter) { layer.setFilter(it) }
      set(visible) { layer.visible = it }
      set(placement) { layer.setSymbolPlacement(it) }
      set(spacing) { layer.setSymbolSpacing(it) }
      set(avoidEdges) { layer.setSymbolAvoidEdges(it) }
      set(sortKey) { layer.setSymbolSortKey(it) }
      set(zOrder) { layer.setSymbolZOrder(it) }

      set(iconAllowOverlap) { layer.setIconAllowOverlap(it) }
      set(iconOverlap) { layer.setIconOverlap(it) }
      set(iconIgnorePlacement) { layer.setIconIgnorePlacement(it) }
      set(iconOptional) { layer.setIconOptional(it) }
      set(iconRotationAlignment) { layer.setIconRotationAlignment(it) }
      set(iconSize) { layer.setIconSize(it) }
      set(iconTextFit) { layer.setIconTextFit(it) }
      set(iconTextFitPadding) { layer.setIconTextFitPadding(it) }
      set(iconImage) { layer.setIconImage(it) }
      set(iconRotate) { layer.setIconRotate(it) }
      set(iconPadding) { layer.setIconPadding(it) }
      set(iconKeepUpright) { layer.setIconKeepUpright(it) }
      set(iconOffset) { layer.setIconOffset(it) }
      set(iconAnchor) { layer.setIconAnchor(it) }
      set(iconPitchAlignment) { layer.setIconPitchAlignment(it) }
      set(iconOpacity) { layer.setIconOpacity(it) }
      set(iconColor) { layer.setIconColor(it) }
      set(iconHaloColor) { layer.setIconHaloColor(it) }
      set(iconHaloWidth) { layer.setIconHaloWidth(it) }
      set(iconHaloBlur) { layer.setIconHaloBlur(it) }
      set(iconTranslate) { layer.setIconTranslate(it) }
      set(iconTranslateAnchor) { layer.setIconTranslateAnchor(it) }

      set(textPitchAlignment) { layer.setTextPitchAlignment(it) }
      set(textRotationAlignment) { layer.setTextRotationAlignment(it) }
      set(textField) { layer.setTextField(it) }
      set(textFont) { layer.setTextFont(it) }
      set(textSize) { layer.setTextSize(it) }
      set(textMaxWidth) { layer.setTextMaxWidth(it) }
      set(textLineHeight) { layer.setTextLineHeight(it) }
      set(textLetterSpacing) { layer.setTextLetterSpacing(it) }
      set(textJustify) { layer.setTextJustify(it) }
      set(textRadialOffset) { layer.setTextRadialOffset(it) }
      set(textVariableAnchor) { layer.setTextVariableAnchor(it) }
      set(textVariableAnchorOffset) { layer.setTextVariableAnchorOffset(it) }
      set(textAnchor) { layer.setTextAnchor(it) }
      set(textMaxAngle) { layer.setTextMaxAngle(it) }
      set(textWritingMode) { layer.setTextWritingMode(it) }
      set(textRotate) { layer.setTextRotate(it) }
      set(textPadding) { layer.setTextPadding(it) }
      set(textKeepUpright) { layer.setTextKeepUpright(it) }
      set(textTransform) { layer.setTextTransform(it) }
      set(textOffset) { layer.setTextOffset(it) }
      set(textAllowOverlap) { layer.setTextAllowOverlap(it) }
      set(textOverlap) { layer.setTextOverlap(it) }
      set(textIgnorePlacement) { layer.setTextIgnorePlacement(it) }
      set(textOptional) { layer.setTextOptional(it) }
      set(textOpacity) { layer.setTextOpacity(it) }
      set(textColor) { layer.setTextColor(it) }
      set(textHaloColor) { layer.setTextHaloColor(it) }
      set(textHaloWidth) { layer.setTextHaloWidth(it) }
      set(textHaloBlur) { layer.setTextHaloBlur(it) }
      set(textTranslate) { layer.setTextTranslate(it) }
      set(textTranslateAnchor) { layer.setTextTranslateAnchor(it) }
    },
    onClick = onClick,
    onLongClick = onLongClick,
  )
}
