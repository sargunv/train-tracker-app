package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.runtime.Immutable

public interface LayerPropertyEnum {
  public val expr: Expression.Enum<LayerPropertyEnum>
}

/** Frame of reference for offsetting geometry */
@Immutable
public enum class TranslateAnchor(override val expr: Expression.Enum<TranslateAnchor>) :
  LayerPropertyEnum {
  /** Offset is relative to the map */
  Map(Expression.Impl("map").cast()),

  /** Offset is relative to the viewport */
  Viewport(Expression.Impl("viewport").cast()),
}

/** Scaling behavior of circles when the map is pitched. */
@Immutable
public enum class CirclePitchScale(override val expr: Expression.Enum<CirclePitchScale>) :
  LayerPropertyEnum {
  /**
   * Circles are scaled according to their apparent distance to the camera, i.e. as if they are on
   * the map.
   */
  Map(Expression.Impl("map").cast()),

  /** Circles are not scaled, i.e. as if glued to the viewport. */
  Viewport(Expression.Impl("viewport").cast()),
}

/** Orientation of circles when the map is pitched. */
@Immutable
public enum class CirclePitchAlignment(override val expr: Expression.Enum<CirclePitchAlignment>) :
  LayerPropertyEnum {
  /** Circles are aligned to the plane of the map, i.e. flat on top of the map. */
  Map(Expression.Impl("map").cast()),

  /** Circles are aligned to the plane of the viewport, i.e. facing the camera. */
  Viewport(Expression.Impl("viewport").cast()),
}

/** Direction of light source when map is rotated. */
@Immutable
public enum class IlluminationAnchor(override val expr: Expression.Enum<IlluminationAnchor>) :
  LayerPropertyEnum {

  /** The hillshade illumination is relative to the north direction. */
  Map(Expression.Impl("map").cast()),

  /** The hillshade illumination is relative to the top of the viewport. */
  Viewport(Expression.Impl("viewport").cast()),
}

/** Display of joined lines */
@Immutable
public enum class LineJoin(override val expr: Expression.Enum<LineJoin>) : LayerPropertyEnum {
  /**
   * A join with a squared-off end which is drawn beyond the endpoint of the line at a distance of
   * one-half of the line's width.
   */
  Bevel(Expression.Impl("bevel").cast()),

  /**
   * A join with a rounded end which is drawn beyond the endpoint of the line at a radius of
   * one-half of the line's width and centered on the endpoint of the line.
   */
  Round(Expression.Impl("round").cast()),

  /**
   * A join with a sharp, angled corner which is drawn with the outer sides beyond the endpoint of
   * the path until they meet.
   */
  Miter(Expression.Impl("miter").cast()),
}

/** Display of line endings */
@Immutable
public enum class LineCap(override val expr: Expression.Enum<LineCap>) : LayerPropertyEnum {
  /** A cap with a squared-off end which is drawn to the exact endpoint of the line. */
  Butt(Expression.Impl("butt").cast()),

  /**
   * A cap with a rounded end which is drawn beyond the endpoint of the line at a radius of one-half
   * of the line's width and centered on the endpoint of the line.
   */
  Round(Expression.Impl("round").cast()),

  /**
   * A cap with a squared-off end which is drawn beyond the endpoint of the line at a distance of
   * one-half of the line's width.
   */
  Square(Expression.Impl("square").cast()),
}

/**
 * The resampling/interpolation method to use for overscaling, also known as texture magnification
 * filter
 */
@Immutable
public enum class RasterResampling(override val expr: Expression.Enum<RasterResampling>) :
  LayerPropertyEnum {
  /**
   * (Bi)linear filtering interpolates pixel values using the weighted average of the four closest
   * original source pixels creating a smooth but blurry look when overscaled
   */
  Linear(Expression.Impl("linear").cast()),

  /**
   * Nearest neighbor filtering interpolates pixel values using the nearest original source pixel
   * creating a sharp but pixelated look when overscaled
   */
  Nearest(Expression.Impl("nearest").cast()),
}

/** Symbol placement relative to its geometry. */
@Immutable
public enum class SymbolPlacement(override val expr: Expression.Enum<SymbolPlacement>) :
  LayerPropertyEnum {
  /** The label is placed at the point where the geometry is located. */
  Point(Expression.Impl("point").cast()),

  /**
   * The label is placed along the line of the geometry. Can only be used on LineString and Polygon
   * geometries.
   */
  Line(Expression.Impl("line").cast()),

  /**
   * The label is placed at the center of the line of the geometry. Can only be used on LineString
   * and Polygon geometries. Note that a single feature in a vector tile may contain multiple line
   * geometries.
   */
  LineCenter(Expression.Impl("line-center").cast()),
}

/**
 * Determines whether overlapping symbols in the same layer are rendered in the order that they
 * appear in the data source or by their y-position relative to the viewport. To control the order
 * and prioritization of symbols otherwise, use `sortKey`.
 */
@Immutable
public enum class SymbolZOrder(override val expr: Expression.Enum<SymbolZOrder>) :
  LayerPropertyEnum {
  /**
   * Sorts symbols by `sortKey` if set. Otherwise, sorts symbols by their y-position relative to the
   * viewport if `iconAllowOverlap` or `textAllowOverlap` is set to `true` or `iconIgnorePlacement`
   * or `textIgnorePlacement` is `false`.
   */
  Auto(Expression.Impl("auto").cast()),

  /**
   * Sorts symbols by their y-position relative to the viewport if `iconAllowOverlap` or
   * `textAllowOverlap` is set to `true` or `iconIgnorePlacement` or `textIgnorePlacement` is
   * `false`.
   */
  ViewportY(Expression.Impl("viewport-y").cast()),

  /**
   * Sorts symbols by `sortKey` if set. Otherwise, no sorting is applied; symbols are rendered in
   * the same order as the source data.
   */
  Source(Expression.Impl("source").cast()),
}

/** Part of the icon/text placed closest to the anchor. */
@Immutable
public enum class SymbolAnchor(override val expr: Expression.Enum<SymbolAnchor>) :
  LayerPropertyEnum {
  /** The center of the icon is placed closest to the anchor. */
  Center(Expression.Impl("center").cast()),

  /** The left side of the icon is placed closest to the anchor. */
  Left(Expression.Impl("left").cast()),

  /** The right side of the icon is placed closest to the anchor. */
  Right(Expression.Impl("right").cast()),

  /** The top of the icon is placed closest to the anchor. */
  Top(Expression.Impl("top").cast()),

  /** The bottom of the icon is placed closest to the anchor. */
  Bottom(Expression.Impl("bottom").cast()),

  /** The top left corner of the icon is placed closest to the anchor. */
  TopLeft(Expression.Impl("top-left").cast()),

  /** The top right corner of the icon is placed closest to the anchor. */
  TopRight(Expression.Impl("top-right").cast()),

  /** The bottom left corner of the icon is placed closest to the anchor. */
  BottomLeft(Expression.Impl("bottom-left").cast()),

  /** The bottom right corner of the icon is placed closest to the anchor. */
  BottomRight(Expression.Impl("bottom-right").cast()),
}

/** Controls whether to show an icon/text when it overlaps other symbols on the map. */
@Immutable
public enum class SymbolOverlap(override val expr: Expression.Enum<SymbolOverlap>) :
  LayerPropertyEnum {
  /** The icon/text will be hidden if it collides with any other previously drawn symbol. */
  Never(Expression.Impl("never").cast()),

  /** The icon/text will be visible even if it collides with any other previously drawn symbol. */
  Always(Expression.Impl("always").cast()),

  /**
   * If the icon/text collides with another previously drawn symbol, the overlap mode for that
   * symbol is checked. If the previous symbol was placed using never overlap mode, the new
   * icon/text is hidden. If the previous symbol was placed using always or cooperative overlap
   * mode, the new icon/text is visible.
   */
  Cooperative(Expression.Impl("cooperative").cast()),
}

/** In combination with [SymbolPlacement], determines the rotation behavior of icons. */
@Immutable
public enum class IconRotationAlignment(override val expr: Expression.Enum<IconRotationAlignment>) :
  LayerPropertyEnum {
  /**
   * For [SymbolPlacement.Point], aligns icons east-west. Otherwise, aligns icon x-axes with the
   * line.
   */
  Map(Expression.Impl("map").cast()),

  /**
   * Produces icons whose x-axes are aligned with the x-axis of the viewport, regardless of the
   * [SymbolPlacement].
   */
  Viewport(Expression.Impl("viewport").cast()),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [IconRotationAlignment.Viewport]. Otherwise,
   * this is equivalent to [IconRotationAlignment.Map].
   */
  Auto(Expression.Impl("auto").cast()),
}

/** Scales the icon to fit around the associated text. */
@Immutable
public enum class IconTextFit(override val expr: Expression.Enum<IconTextFit>) : LayerPropertyEnum {
  /** The icon is displayed at its intrinsic aspect ratio. */
  None(Expression.Impl("none").cast()),

  /** The icon is scaled in the x-dimension to fit the width of the text. */
  Width(Expression.Impl("width").cast()),

  /** The icon is scaled in the y-dimension to fit the height of the text. */
  Height(Expression.Impl("height").cast()),

  /** The icon is scaled in both x- and y-dimensions. */
  Both(Expression.Impl("both").cast()),
}

/** Orientation of icon when map is pitched. */
@Immutable
public enum class IconPitchAlignment(override val expr: Expression.Enum<IconPitchAlignment>) :
  LayerPropertyEnum {
  /** The icon is aligned to the plane of the map. */
  Map(Expression.Impl("map").cast()),

  /** The icon is aligned to the plane of the viewport, i.e. as if glued to the screen */
  Viewport(Expression.Impl("viewport").cast()),

  /** Automatically matches the value of [IconRotationAlignment] */
  Auto(Expression.Impl("auto").cast()),
}

/** Orientation of text when map is pitched. */
@Immutable
public enum class TextPitchAlignment(override val expr: Expression.Enum<TextPitchAlignment>) :
  LayerPropertyEnum {
  /** The text is aligned to the plane of the map. */
  Map(Expression.Impl("map").cast()),

  /** The text is aligned to the plane of the viewport, i.e. as if glued to the screen */
  Viewport(Expression.Impl("viewport").cast()),

  /** Automatically matches the value of [TextRotationAlignment] */
  Auto(Expression.Impl("auto").cast()),
}

/**
 * In combination with [SymbolPlacement], determines the rotation behavior of the individual glyphs
 * forming the text.
 */
@Immutable
public enum class TextRotationAlignment(override val expr: Expression.Enum<TextRotationAlignment>) :
  LayerPropertyEnum {
  /**
   * For [SymbolPlacement.Point], aligns text east-west. Otherwise, aligns text x-axes with the
   * line.
   */
  Map(Expression.Impl("map").cast()),

  /**
   * Produces glyphs whose x-axes are aligned with the x-axis of the viewport, regardless of the
   * [SymbolPlacement].
   */
  Viewport(Expression.Impl("viewport").cast()),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [TextRotationAlignment.Viewport]. Otherwise,
   * aligns glyphs to the x-axis of the viewport and places them along the line.
   *
   * **Note**: This value not supported on native platforms, yet, see
   * [maplibre-native#250](https://github.com/maplibre/maplibre-native/issues/250)**
   */
  ViewportGlyph(Expression.Impl("viewport-glyph").cast()),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [TextRotationAlignment.Viewport]. Otherwise,
   * this is equivalent to [TextRotationAlignment.Map].
   */
  Auto(Expression.Impl("auto").cast()),
}

/** How the text will be laid out. */
@Immutable
public enum class TextWritingMode(override val expr: Expression.Enum<TextWritingMode>) :
  LayerPropertyEnum {
  /**
   * If a text's language supports horizontal writing mode, symbols with point placement would be
   * laid out horizontally.
   */
  Horizontal(Expression.Impl("horizontal").cast()),

  /**
   * If a text's language supports vertical writing mode, symbols with point placement would be laid
   * out vertically.
   */
  Vertical(Expression.Impl("vertical").cast()),
}

/** Text justification options. */
@Immutable
public enum class TextJustify(override val expr: Expression.Enum<TextJustify>) : LayerPropertyEnum {
  /** The text is aligned towards the anchor position. */
  Auto(Expression.Impl("auto").cast()),

  /** The text is aligned to the left. */
  Left(Expression.Impl("left").cast()),

  /** The text is centered. */
  Center(Expression.Impl("center").cast()),

  /** The text is aligned to the right. */
  Right(Expression.Impl("right").cast()),
}

/** Specifies how to capitalize text, similar to the CSS text-transform property. */
@Immutable
public enum class TextTransform(override val expr: Expression.Enum<TextTransform>) :
  LayerPropertyEnum {
  /** The text is not altered. */
  None(Expression.Impl("none").cast()),

  /** Forces all letters to be displayed in uppercase. */
  Uppercase(Expression.Impl("uppercase").cast()),

  /** Forces all letters to be displayed in lowercase. */
  Lowercase(Expression.Impl("lowercase").cast()),
}
