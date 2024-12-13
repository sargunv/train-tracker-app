package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.runtime.Immutable

public interface LayerPropertyEnum {
  public val expr: EnumExpression<LayerPropertyEnum>
}

/** Frame of reference for offsetting geometry */
@Immutable
public enum class TranslateAnchor(override val expr: EnumExpression<TranslateAnchor>) :
  LayerPropertyEnum {
  /** Offset is relative to the map */
  Map(ExpressionImpl("map").cast()),

  /** Offset is relative to the viewport */
  Viewport(ExpressionImpl("viewport").cast()),
}

/** Scaling behavior of circles when the map is pitched. */
@Immutable
public enum class CirclePitchScale(override val expr: EnumExpression<CirclePitchScale>) :
  LayerPropertyEnum {
  /**
   * Circles are scaled according to their apparent distance to the camera, i.e. as if they are on
   * the map.
   */
  Map(ExpressionImpl("map").cast()),

  /** Circles are not scaled, i.e. as if glued to the viewport. */
  Viewport(ExpressionImpl("viewport").cast()),
}

/** Orientation of circles when the map is pitched. */
@Immutable
public enum class CirclePitchAlignment(override val expr: EnumExpression<CirclePitchAlignment>) :
  LayerPropertyEnum {
  /** Circles are aligned to the plane of the map, i.e. flat on top of the map. */
  Map(ExpressionImpl("map").cast()),

  /** Circles are aligned to the plane of the viewport, i.e. facing the camera. */
  Viewport(ExpressionImpl("viewport").cast()),
}

/** Direction of light source when map is rotated. */
@Immutable
public enum class IlluminationAnchor(override val expr: EnumExpression<IlluminationAnchor>) :
  LayerPropertyEnum {

  /** The hillshade illumination is relative to the north direction. */
  Map(ExpressionImpl("map").cast()),

  /** The hillshade illumination is relative to the top of the viewport. */
  Viewport(ExpressionImpl("viewport").cast()),
}

/** Display of joined lines */
@Immutable
public enum class LineJoin(override val expr: EnumExpression<LineJoin>) : LayerPropertyEnum {
  /**
   * A join with a squared-off end which is drawn beyond the endpoint of the line at a distance of
   * one-half of the line's width.
   */
  Bevel(ExpressionImpl("bevel").cast()),

  /**
   * A join with a rounded end which is drawn beyond the endpoint of the line at a radius of
   * one-half of the line's width and centered on the endpoint of the line.
   */
  Round(ExpressionImpl("round").cast()),

  /**
   * A join with a sharp, angled corner which is drawn with the outer sides beyond the endpoint of
   * the path until they meet.
   */
  Miter(ExpressionImpl("miter").cast()),
}

/** Display of line endings */
@Immutable
public enum class LineCap(override val expr: EnumExpression<LineCap>) : LayerPropertyEnum {
  /** A cap with a squared-off end which is drawn to the exact endpoint of the line. */
  Butt(ExpressionImpl("butt").cast()),

  /**
   * A cap with a rounded end which is drawn beyond the endpoint of the line at a radius of one-half
   * of the line's width and centered on the endpoint of the line.
   */
  Round(ExpressionImpl("round").cast()),

  /**
   * A cap with a squared-off end which is drawn beyond the endpoint of the line at a distance of
   * one-half of the line's width.
   */
  Square(ExpressionImpl("square").cast()),
}

/**
 * The resampling/interpolation method to use for overscaling, also known as texture magnification
 * filter
 */
@Immutable
public enum class RasterResampling(override val expr: EnumExpression<RasterResampling>) :
  LayerPropertyEnum {
  /**
   * (Bi)linear filtering interpolates pixel values using the weighted average of the four closest
   * original source pixels creating a smooth but blurry look when overscaled
   */
  Linear(ExpressionImpl("linear").cast()),

  /**
   * Nearest neighbor filtering interpolates pixel values using the nearest original source pixel
   * creating a sharp but pixelated look when overscaled
   */
  Nearest(ExpressionImpl("nearest").cast()),
}

/** Symbol placement relative to its geometry. */
@Immutable
public enum class SymbolPlacement(override val expr: EnumExpression<SymbolPlacement>) :
  LayerPropertyEnum {
  /** The label is placed at the point where the geometry is located. */
  Point(ExpressionImpl("point").cast()),

  /**
   * The label is placed along the line of the geometry. Can only be used on LineString and Polygon
   * geometries.
   */
  Line(ExpressionImpl("line").cast()),

  /**
   * The label is placed at the center of the line of the geometry. Can only be used on LineString
   * and Polygon geometries. Note that a single feature in a vector tile may contain multiple line
   * geometries.
   */
  LineCenter(ExpressionImpl("line-center").cast()),
}

/**
 * Determines whether overlapping symbols in the same layer are rendered in the order that they
 * appear in the data source or by their y-position relative to the viewport. To control the order
 * and prioritization of symbols otherwise, use `sortKey`.
 */
@Immutable
public enum class SymbolZOrder(override val expr: EnumExpression<SymbolZOrder>) :
  LayerPropertyEnum {
  /**
   * Sorts symbols by `sortKey` if set. Otherwise, sorts symbols by their y-position relative to the
   * viewport if `iconAllowOverlap` or `textAllowOverlap` is set to `true` or `iconIgnorePlacement`
   * or `textIgnorePlacement` is `false`.
   */
  Auto(ExpressionImpl("auto").cast()),

  /**
   * Sorts symbols by their y-position relative to the viewport if `iconAllowOverlap` or
   * `textAllowOverlap` is set to `true` or `iconIgnorePlacement` or `textIgnorePlacement` is
   * `false`.
   */
  ViewportY(ExpressionImpl("viewport-y").cast()),

  /**
   * Sorts symbols by `sortKey` if set. Otherwise, no sorting is applied; symbols are rendered in
   * the same order as the source data.
   */
  Source(ExpressionImpl("source").cast()),
}

/** Part of the icon/text placed closest to the anchor. */
@Immutable
public enum class SymbolAnchor(override val expr: EnumExpression<SymbolAnchor>) :
  LayerPropertyEnum {
  /** The center of the icon is placed closest to the anchor. */
  Center(ExpressionImpl("center").cast()),

  /** The left side of the icon is placed closest to the anchor. */
  Left(ExpressionImpl("left").cast()),

  /** The right side of the icon is placed closest to the anchor. */
  Right(ExpressionImpl("right").cast()),

  /** The top of the icon is placed closest to the anchor. */
  Top(ExpressionImpl("top").cast()),

  /** The bottom of the icon is placed closest to the anchor. */
  Bottom(ExpressionImpl("bottom").cast()),

  /** The top left corner of the icon is placed closest to the anchor. */
  TopLeft(ExpressionImpl("top-left").cast()),

  /** The top right corner of the icon is placed closest to the anchor. */
  TopRight(ExpressionImpl("top-right").cast()),

  /** The bottom left corner of the icon is placed closest to the anchor. */
  BottomLeft(ExpressionImpl("bottom-left").cast()),

  /** The bottom right corner of the icon is placed closest to the anchor. */
  BottomRight(ExpressionImpl("bottom-right").cast()),
}

/** Controls whether to show an icon/text when it overlaps other symbols on the map. */
@Immutable
public enum class SymbolOverlap(override val expr: EnumExpression<SymbolOverlap>) :
  LayerPropertyEnum {
  /** The icon/text will be hidden if it collides with any other previously drawn symbol. */
  Never(ExpressionImpl("never").cast()),

  /** The icon/text will be visible even if it collides with any other previously drawn symbol. */
  Always(ExpressionImpl("always").cast()),

  /**
   * If the icon/text collides with another previously drawn symbol, the overlap mode for that
   * symbol is checked. If the previous symbol was placed using never overlap mode, the new
   * icon/text is hidden. If the previous symbol was placed using always or cooperative overlap
   * mode, the new icon/text is visible.
   */
  Cooperative(ExpressionImpl("cooperative").cast()),
}

/** In combination with [SymbolPlacement], determines the rotation behavior of icons. */
@Immutable
public enum class IconRotationAlignment(override val expr: EnumExpression<IconRotationAlignment>) :
  LayerPropertyEnum {
  /**
   * For [SymbolPlacement.Point], aligns icons east-west. Otherwise, aligns icon x-axes with the
   * line.
   */
  Map(ExpressionImpl("map").cast()),

  /**
   * Produces icons whose x-axes are aligned with the x-axis of the viewport, regardless of the
   * [SymbolPlacement].
   */
  Viewport(ExpressionImpl("viewport").cast()),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [IconRotationAlignment.Viewport]. Otherwise,
   * this is equivalent to [IconRotationAlignment.Map].
   */
  Auto(ExpressionImpl("auto").cast()),
}

/** Scales the icon to fit around the associated text. */
@Immutable
public enum class IconTextFit(override val expr: EnumExpression<IconTextFit>) : LayerPropertyEnum {
  /** The icon is displayed at its intrinsic aspect ratio. */
  None(ExpressionImpl("none").cast()),

  /** The icon is scaled in the x-dimension to fit the width of the text. */
  Width(ExpressionImpl("width").cast()),

  /** The icon is scaled in the y-dimension to fit the height of the text. */
  Height(ExpressionImpl("height").cast()),

  /** The icon is scaled in both x- and y-dimensions. */
  Both(ExpressionImpl("both").cast()),
}

/** Orientation of icon when map is pitched. */
@Immutable
public enum class IconPitchAlignment(override val expr: EnumExpression<IconPitchAlignment>) :
  LayerPropertyEnum {
  /** The icon is aligned to the plane of the map. */
  Map(ExpressionImpl("map").cast()),

  /** The icon is aligned to the plane of the viewport, i.e. as if glued to the screen */
  Viewport(ExpressionImpl("viewport").cast()),

  /** Automatically matches the value of [IconRotationAlignment] */
  Auto(ExpressionImpl("auto").cast()),
}

/** Orientation of text when map is pitched. */
@Immutable
public enum class TextPitchAlignment(override val expr: EnumExpression<TextPitchAlignment>) :
  LayerPropertyEnum {
  /** The text is aligned to the plane of the map. */
  Map(ExpressionImpl("map").cast()),

  /** The text is aligned to the plane of the viewport, i.e. as if glued to the screen */
  Viewport(ExpressionImpl("viewport").cast()),

  /** Automatically matches the value of [TextRotationAlignment] */
  Auto(ExpressionImpl("auto").cast()),
}

/**
 * In combination with [SymbolPlacement], determines the rotation behavior of the individual glyphs
 * forming the text.
 */
@Immutable
public enum class TextRotationAlignment(override val expr: EnumExpression<TextRotationAlignment>) :
  LayerPropertyEnum {
  /**
   * For [SymbolPlacement.Point], aligns text east-west. Otherwise, aligns text x-axes with the
   * line.
   */
  Map(ExpressionImpl("map").cast()),

  /**
   * Produces glyphs whose x-axes are aligned with the x-axis of the viewport, regardless of the
   * [SymbolPlacement].
   */
  Viewport(ExpressionImpl("viewport").cast()),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [TextRotationAlignment.Viewport]. Otherwise,
   * aligns glyphs to the x-axis of the viewport and places them along the line.
   *
   * **Note**: This value not supported on native platforms, yet, see
   * [maplibre-native#250](https://github.com/maplibre/maplibre-native/issues/250)**
   */
  ViewportGlyph(ExpressionImpl("viewport-glyph").cast()),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [TextRotationAlignment.Viewport]. Otherwise,
   * this is equivalent to [TextRotationAlignment.Map].
   */
  Auto(ExpressionImpl("auto").cast()),
}

/** How the text will be laid out. */
@Immutable
public enum class TextWritingMode(override val expr: EnumExpression<TextWritingMode>) :
  LayerPropertyEnum {
  /**
   * If a text's language supports horizontal writing mode, symbols with point placement would be
   * laid out horizontally.
   */
  Horizontal(ExpressionImpl("horizontal").cast()),

  /**
   * If a text's language supports vertical writing mode, symbols with point placement would be laid
   * out vertically.
   */
  Vertical(ExpressionImpl("vertical").cast()),
}

/** Text justification options. */
@Immutable
public enum class TextJustify(override val expr: EnumExpression<TextJustify>) : LayerPropertyEnum {
  /** The text is aligned towards the anchor position. */
  Auto(ExpressionImpl("auto").cast()),

  /** The text is aligned to the left. */
  Left(ExpressionImpl("left").cast()),

  /** The text is centered. */
  Center(ExpressionImpl("center").cast()),

  /** The text is aligned to the right. */
  Right(ExpressionImpl("right").cast()),
}

/** Specifies how to capitalize text, similar to the CSS text-transform property. */
@Immutable
public enum class TextTransform(override val expr: EnumExpression<TextTransform>) :
  LayerPropertyEnum {
  /** The text is not altered. */
  None(ExpressionImpl("none").cast()),

  /** Forces all letters to be displayed in uppercase. */
  Uppercase(ExpressionImpl("uppercase").cast()),

  /** Forces all letters to be displayed in lowercase. */
  Lowercase(ExpressionImpl("lowercase").cast()),
}
