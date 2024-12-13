package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.runtime.Immutable

public interface LayerPropertyEnum {
  public val expr: Expression<*>
}

/** Frame of reference for offsetting geometry */
@Immutable
public enum class TranslateAnchor(override val expr: Expression<EnumValue<TranslateAnchor>>) :
  LayerPropertyEnum {
  /** Offset is relative to the map */
  Map(Expression.ofString("map").cast()),

  /** Offset is relative to the viewport */
  Viewport(Expression.ofString("viewport").cast()),
}

/** Scaling behavior of circles when the map is pitched. */
@Immutable
public enum class CirclePitchScale(override val expr: Expression<EnumValue<CirclePitchScale>>) :
  LayerPropertyEnum {
  /**
   * Circles are scaled according to their apparent distance to the camera, i.e. as if they are on
   * the map.
   */
  Map(Expression.ofString("map").cast()),

  /** Circles are not scaled, i.e. as if glued to the viewport. */
  Viewport(Expression.ofString("viewport").cast()),
}

/** Orientation of circles when the map is pitched. */
@Immutable
public enum class CirclePitchAlignment(
  override val expr: Expression<EnumValue<CirclePitchAlignment>>
) : LayerPropertyEnum {
  /** Circles are aligned to the plane of the map, i.e. flat on top of the map. */
  Map(Expression.ofString("map").cast()),

  /** Circles are aligned to the plane of the viewport, i.e. facing the camera. */
  Viewport(Expression.ofString("viewport").cast()),
}

/** Direction of light source when map is rotated. */
@Immutable
public enum class IlluminationAnchor(override val expr: Expression<EnumValue<IlluminationAnchor>>) :
  LayerPropertyEnum {

  /** The hillshade illumination is relative to the north direction. */
  Map(Expression.ofString("map").cast()),

  /** The hillshade illumination is relative to the top of the viewport. */
  Viewport(Expression.ofString("viewport").cast()),
}

/** Display of joined lines */
@Immutable
public enum class LineJoin(override val expr: Expression<EnumValue<LineJoin>>) : LayerPropertyEnum {
  /**
   * A join with a squared-off end which is drawn beyond the endpoint of the line at a distance of
   * one-half of the line's width.
   */
  Bevel(Expression.ofString("bevel").cast()),

  /**
   * A join with a rounded end which is drawn beyond the endpoint of the line at a radius of
   * one-half of the line's width and centered on the endpoint of the line.
   */
  Round(Expression.ofString("round").cast()),

  /**
   * A join with a sharp, angled corner which is drawn with the outer sides beyond the endpoint of
   * the path until they meet.
   */
  Miter(Expression.ofString("miter").cast()),
}

/** Display of line endings */
@Immutable
public enum class LineCap(override val expr: Expression<EnumValue<LineCap>>) : LayerPropertyEnum {
  /** A cap with a squared-off end which is drawn to the exact endpoint of the line. */
  Butt(Expression.ofString("butt").cast()),

  /**
   * A cap with a rounded end which is drawn beyond the endpoint of the line at a radius of one-half
   * of the line's width and centered on the endpoint of the line.
   */
  Round(Expression.ofString("round").cast()),

  /**
   * A cap with a squared-off end which is drawn beyond the endpoint of the line at a distance of
   * one-half of the line's width.
   */
  Square(Expression.ofString("square").cast()),
}

/**
 * The resampling/interpolation method to use for overscaling, also known as texture magnification
 * filter
 */
@Immutable
public enum class RasterResampling(override val expr: Expression<EnumValue<RasterResampling>>) :
  LayerPropertyEnum {
  /**
   * (Bi)linear filtering interpolates pixel values using the weighted average of the four closest
   * original source pixels creating a smooth but blurry look when overscaled
   */
  Linear(Expression.ofString("linear").cast()),

  /**
   * Nearest neighbor filtering interpolates pixel values using the nearest original source pixel
   * creating a sharp but pixelated look when overscaled
   */
  Nearest(Expression.ofString("nearest").cast()),
}

/** Symbol placement relative to its geometry. */
@Immutable
public enum class SymbolPlacement(override val expr: Expression<EnumValue<SymbolPlacement>>) :
  LayerPropertyEnum {
  /** The label is placed at the point where the geometry is located. */
  Point(Expression.ofString("point").cast()),

  /**
   * The label is placed along the line of the geometry. Can only be used on LineString and Polygon
   * geometries.
   */
  Line(Expression.ofString("line").cast()),

  /**
   * The label is placed at the center of the line of the geometry. Can only be used on LineString
   * and Polygon geometries. Note that a single feature in a vector tile may contain multiple line
   * geometries.
   */
  LineCenter(Expression.ofString("line-center").cast()),
}

/**
 * Determines whether overlapping symbols in the same layer are rendered in the order that they
 * appear in the data source or by their y-position relative to the viewport. To control the order
 * and prioritization of symbols otherwise, use `sortKey`.
 */
@Immutable
public enum class SymbolZOrder(override val expr: Expression<EnumValue<SymbolZOrder>>) :
  LayerPropertyEnum {
  /**
   * Sorts symbols by `sortKey` if set. Otherwise, sorts symbols by their y-position relative to the
   * viewport if `iconAllowOverlap` or `textAllowOverlap` is set to `true` or `iconIgnorePlacement`
   * or `textIgnorePlacement` is `false`.
   */
  Auto(Expression.ofString("auto").cast()),

  /**
   * Sorts symbols by their y-position relative to the viewport if `iconAllowOverlap` or
   * `textAllowOverlap` is set to `true` or `iconIgnorePlacement` or `textIgnorePlacement` is
   * `false`.
   */
  ViewportY(Expression.ofString("viewport-y").cast()),

  /**
   * Sorts symbols by `sortKey` if set. Otherwise, no sorting is applied; symbols are rendered in
   * the same order as the source data.
   */
  Source(Expression.ofString("source").cast()),
}

/** Part of the icon/text placed closest to the anchor. */
@Immutable
public enum class SymbolAnchor(override val expr: Expression<EnumValue<SymbolAnchor>>) :
  LayerPropertyEnum {
  /** The center of the icon is placed closest to the anchor. */
  Center(Expression.ofString("center").cast()),

  /** The left side of the icon is placed closest to the anchor. */
  Left(Expression.ofString("left").cast()),

  /** The right side of the icon is placed closest to the anchor. */
  Right(Expression.ofString("right").cast()),

  /** The top of the icon is placed closest to the anchor. */
  Top(Expression.ofString("top").cast()),

  /** The bottom of the icon is placed closest to the anchor. */
  Bottom(Expression.ofString("bottom").cast()),

  /** The top left corner of the icon is placed closest to the anchor. */
  TopLeft(Expression.ofString("top-left").cast()),

  /** The top right corner of the icon is placed closest to the anchor. */
  TopRight(Expression.ofString("top-right").cast()),

  /** The bottom left corner of the icon is placed closest to the anchor. */
  BottomLeft(Expression.ofString("bottom-left").cast()),

  /** The bottom right corner of the icon is placed closest to the anchor. */
  BottomRight(Expression.ofString("bottom-right").cast()),
}

/** Controls whether to show an icon/text when it overlaps other symbols on the map. */
@Immutable
public enum class SymbolOverlap(override val expr: Expression<EnumValue<SymbolOverlap>>) :
  LayerPropertyEnum {
  /** The icon/text will be hidden if it collides with any other previously drawn symbol. */
  Never(Expression.ofString("never").cast()),

  /** The icon/text will be visible even if it collides with any other previously drawn symbol. */
  Always(Expression.ofString("always").cast()),

  /**
   * If the icon/text collides with another previously drawn symbol, the overlap mode for that
   * symbol is checked. If the previous symbol was placed using never overlap mode, the new
   * icon/text is hidden. If the previous symbol was placed using always or cooperative overlap
   * mode, the new icon/text is visible.
   */
  Cooperative(Expression.ofString("cooperative").cast()),
}

/** In combination with [SymbolPlacement], determines the rotation behavior of icons. */
@Immutable
public enum class IconRotationAlignment(
  override val expr: Expression<EnumValue<IconRotationAlignment>>
) : LayerPropertyEnum {
  /**
   * For [SymbolPlacement.Point], aligns icons east-west. Otherwise, aligns icon x-axes with the
   * line.
   */
  Map(Expression.ofString("map").cast()),

  /**
   * Produces icons whose x-axes are aligned with the x-axis of the viewport, regardless of the
   * [SymbolPlacement].
   */
  Viewport(Expression.ofString("viewport").cast()),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [IconRotationAlignment.Viewport]. Otherwise,
   * this is equivalent to [IconRotationAlignment.Map].
   */
  Auto(Expression.ofString("auto").cast()),
}

/** Scales the icon to fit around the associated text. */
@Immutable
public enum class IconTextFit(override val expr: Expression<EnumValue<IconTextFit>>) :
  LayerPropertyEnum {
  /** The icon is displayed at its intrinsic aspect ratio. */
  None(Expression.ofString("none").cast()),

  /** The icon is scaled in the x-dimension to fit the width of the text. */
  Width(Expression.ofString("width").cast()),

  /** The icon is scaled in the y-dimension to fit the height of the text. */
  Height(Expression.ofString("height").cast()),

  /** The icon is scaled in both x- and y-dimensions. */
  Both(Expression.ofString("both").cast()),
}

/** Orientation of icon when map is pitched. */
@Immutable
public enum class IconPitchAlignment(override val expr: Expression<EnumValue<IconPitchAlignment>>) :
  LayerPropertyEnum {
  /** The icon is aligned to the plane of the map. */
  Map(Expression.ofString("map").cast()),

  /** The icon is aligned to the plane of the viewport, i.e. as if glued to the screen */
  Viewport(Expression.ofString("viewport").cast()),

  /** Automatically matches the value of [IconRotationAlignment] */
  Auto(Expression.ofString("auto").cast()),
}

/** Orientation of text when map is pitched. */
@Immutable
public enum class TextPitchAlignment(override val expr: Expression<EnumValue<TextPitchAlignment>>) :
  LayerPropertyEnum {
  /** The text is aligned to the plane of the map. */
  Map(Expression.ofString("map").cast()),

  /** The text is aligned to the plane of the viewport, i.e. as if glued to the screen */
  Viewport(Expression.ofString("viewport").cast()),

  /** Automatically matches the value of [TextRotationAlignment] */
  Auto(Expression.ofString("auto").cast()),
}

/**
 * In combination with [SymbolPlacement], determines the rotation behavior of the individual glyphs
 * forming the text.
 */
@Immutable
public enum class TextRotationAlignment(
  override val expr: Expression<EnumValue<TextRotationAlignment>>
) : LayerPropertyEnum {
  /**
   * For [SymbolPlacement.Point], aligns text east-west. Otherwise, aligns text x-axes with the
   * line.
   */
  Map(Expression.ofString("map").cast()),

  /**
   * Produces glyphs whose x-axes are aligned with the x-axis of the viewport, regardless of the
   * [SymbolPlacement].
   */
  Viewport(Expression.ofString("viewport").cast()),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [TextRotationAlignment.Viewport]. Otherwise,
   * aligns glyphs to the x-axis of the viewport and places them along the line.
   *
   * **Note**: This value not supported on native platforms, yet, see
   * [maplibre-native#250](https://github.com/maplibre/maplibre-native/issues/250)**
   */
  ViewportGlyph(Expression.ofString("viewport-glyph").cast()),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [TextRotationAlignment.Viewport]. Otherwise,
   * this is equivalent to [TextRotationAlignment.Map].
   */
  Auto(Expression.ofString("auto").cast()),
}

/** How the text will be laid out. */
@Immutable
public enum class TextWritingMode(override val expr: Expression<EnumValue<TextWritingMode>>) :
  LayerPropertyEnum {
  /**
   * If a text's language supports horizontal writing mode, symbols with point placement would be
   * laid out horizontally.
   */
  Horizontal(Expression.ofString("horizontal").cast()),

  /**
   * If a text's language supports vertical writing mode, symbols with point placement would be laid
   * out vertically.
   */
  Vertical(Expression.ofString("vertical").cast()),
}

/** Text justification options. */
@Immutable
public enum class TextJustify(override val expr: Expression<EnumValue<TextJustify>>) :
  LayerPropertyEnum {
  /** The text is aligned towards the anchor position. */
  Auto(Expression.ofString("auto").cast()),

  /** The text is aligned to the left. */
  Left(Expression.ofString("left").cast()),

  /** The text is centered. */
  Center(Expression.ofString("center").cast()),

  /** The text is aligned to the right. */
  Right(Expression.ofString("right").cast()),
}

/** Specifies how to capitalize text, similar to the CSS text-transform property. */
@Immutable
public enum class TextTransform(override val expr: Expression<EnumValue<TextTransform>>) :
  LayerPropertyEnum {
  /** The text is not altered. */
  None(Expression.ofString("none").cast()),

  /** Forces all letters to be displayed in uppercase. */
  Uppercase(Expression.ofString("uppercase").cast()),

  /** Forces all letters to be displayed in lowercase. */
  Lowercase(Expression.ofString("lowercase").cast()),
}
