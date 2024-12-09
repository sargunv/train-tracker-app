package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.runtime.Immutable
import dev.sargunv.maplibrecompose.core.expression.Expression.Companion.const

public interface LayerPropertyEnum {
  public val expr: Expression<String>
}

/** Frame of reference for offsetting geometry */
@Immutable
public enum class TranslateAnchor(override val expr: Expression<String>) : LayerPropertyEnum {
  /** Offset is relative to the map */
  Map(const("map")),

  /** Offset is relative to the viewport */
  Viewport(const("viewport")),
}

/** Scaling behavior of circles when the map is pitched. */
@Immutable
public enum class CirclePitchScale(override val expr: Expression<String>) : LayerPropertyEnum {
  /**
   * Circles are scaled according to their apparent distance to the camera, i.e. as if they are on
   * the map.
   */
  Map(const("map")),

  /** Circles are not scaled, i.e. as if glued to the viewport. */
  Viewport(const("viewport")),
}

/** Orientation of circles when the map is pitched. */
@Immutable
public enum class CirclePitchAlignment(override val expr: Expression<String>) : LayerPropertyEnum {
  /** Circles are aligned to the plane of the map, i.e. flat on top of the map. */
  Map(const("map")),

  /** Circles are aligned to the plane of the viewport, i.e. facing the camera. */
  Viewport(const("viewport")),
}

/** Direction of light source when map is rotated. */
@Immutable
public enum class IlluminationAnchor(override val expr: Expression<String>) : LayerPropertyEnum {

  /** The hillshade illumination is relative to the north direction. */
  Map(const("map")),

  /** The hillshade illumination is relative to the top of the viewport. */
  Viewport(const("viewport")),
}

/** Display of joined lines */
@Immutable
public enum class LineJoin(override val expr: Expression<String>) : LayerPropertyEnum {
  /**
   * A join with a squared-off end which is drawn beyond the endpoint of the line at a distance of
   * one-half of the line's width.
   */
  Bevel(const("bevel")),

  /**
   * A join with a rounded end which is drawn beyond the endpoint of the line at a radius of
   * one-half of the line's width and centered on the endpoint of the line.
   */
  Round(const("round")),

  /**
   * A join with a sharp, angled corner which is drawn with the outer sides beyond the endpoint of
   * the path until they meet.
   */
  Miter(const("miter")),
}

/** Display of line endings */
@Immutable
public enum class LineCap(override val expr: Expression<String>) : LayerPropertyEnum {
  /** A cap with a squared-off end which is drawn to the exact endpoint of the line. */
  Butt(const("butt")),

  /**
   * A cap with a rounded end which is drawn beyond the endpoint of the line at a radius of one-half
   * of the line's width and centered on the endpoint of the line.
   */
  Round(const("round")),

  /**
   * A cap with a squared-off end which is drawn beyond the endpoint of the line at a distance of
   * one-half of the line's width.
   */
  Square(const("square")),
}

/**
 * The resampling/interpolation method to use for overscaling, also known as texture magnification
 * filter
 */
@Immutable
public enum class RasterResampling(override val expr: Expression<String>) : LayerPropertyEnum {
  /**
   * (Bi)linear filtering interpolates pixel values using the weighted average of the four closest
   * original source pixels creating a smooth but blurry look when overscaled
   */
  Linear(const("linear")),

  /**
   * Nearest neighbor filtering interpolates pixel values using the nearest original source pixel
   * creating a sharp but pixelated look when overscaled
   */
  Nearest(const("nearest")),
}

/** Symbol placement relative to its geometry. */
@Immutable
public enum class SymbolPlacement(override val expr: Expression<String>) : LayerPropertyEnum {
  /** The label is placed at the point where the geometry is located. */
  Point(const("point")),

  /**
   * The label is placed along the line of the geometry. Can only be used on LineString and Polygon
   * geometries.
   */
  Line(const("line")),

  /**
   * The label is placed at the center of the line of the geometry. Can only be used on LineString
   * and Polygon geometries. Note that a single feature in a vector tile may contain multiple line
   * geometries.
   */
  LineCenter(const("line-center")),
}

/**
 * Determines whether overlapping symbols in the same layer are rendered in the order that they
 * appear in the data source or by their y-position relative to the viewport. To control the order
 * and prioritization of symbols otherwise, use `sortKey`.
 */
@Immutable
public enum class SymbolZOrder(override val expr: Expression<String>) : LayerPropertyEnum {
  /**
   * Sorts symbols by `sortKey` if set. Otherwise, sorts symbols by their y-position relative to the
   * viewport if `iconAllowOverlap` or `textAllowOverlap` is set to `true` or `iconIgnorePlacement`
   * or `textIgnorePlacement` is `false`.
   */
  Auto(const("auto")),

  /**
   * Sorts symbols by their y-position relative to the viewport if `iconAllowOverlap` or
   * `textAllowOverlap` is set to `true` or `iconIgnorePlacement` or `textIgnorePlacement` is
   * `false`.
   */
  ViewportY(const("viewport-y")),

  /**
   * Sorts symbols by `sortKey` if set. Otherwise, no sorting is applied; symbols are rendered in
   * the same order as the source data.
   */
  Source(const("source")),
}

/** Part of the icon/text placed closest to the anchor. */
@Immutable
public enum class SymbolAnchor(override val expr: Expression<String>) : LayerPropertyEnum {
  /** The center of the icon is placed closest to the anchor. */
  Center(const("center")),

  /** The left side of the icon is placed closest to the anchor. */
  Left(const("left")),

  /** The right side of the icon is placed closest to the anchor. */
  Right(const("right")),

  /** The top of the icon is placed closest to the anchor. */
  Top(const("top")),

  /** The bottom of the icon is placed closest to the anchor. */
  Bottom(const("bottom")),

  /** The top left corner of the icon is placed closest to the anchor. */
  TopLeft(const("top-left")),

  /** The top right corner of the icon is placed closest to the anchor. */
  TopRight(const("top-right")),

  /** The bottom left corner of the icon is placed closest to the anchor. */
  BottomLeft(const("bottom-left")),

  /** The bottom right corner of the icon is placed closest to the anchor. */
  BottomRight(const("bottom-right")),
}

/** Controls whether to show an icon/text when it overlaps other symbols on the map. */
@Immutable
public enum class SymbolOverlap(override val expr: Expression<String>) : LayerPropertyEnum {
  /** The icon/text will be hidden if it collides with any other previously drawn symbol. */
  Never(const("never")),

  /** The icon/text will be visible even if it collides with any other previously drawn symbol. */
  Always(const("always")),

  /**
   * If the icon/text collides with another previously drawn symbol, the overlap mode for that
   * symbol is checked. If the previous symbol was placed using never overlap mode, the new
   * icon/text is hidden. If the previous symbol was placed using always or cooperative overlap
   * mode, the new icon/text is visible.
   */
  Cooperative(const("cooperative")),
}

/** In combination with [SymbolPlacement], determines the rotation behavior of icons. */
@Immutable
public enum class IconRotationAlignment(override val expr: Expression<String>) : LayerPropertyEnum {
  /**
   * For [SymbolPlacement.Point], aligns icons east-west. Otherwise, aligns icon x-axes with the
   * line.
   */
  Map(const("map")),

  /**
   * Produces icons whose x-axes are aligned with the x-axis of the viewport, regardless of the
   * [SymbolPlacement].
   */
  Viewport(const("viewport")),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [IconRotationAlignment.Viewport]. Otherwise,
   * this is equivalent to [IconRotationAlignment.Map].
   */
  Auto(const("auto")),
}

/** Scales the icon to fit around the associated text. */
@Immutable
public enum class IconTextFit(override val expr: Expression<String>) : LayerPropertyEnum {
  /** The icon is displayed at its intrinsic aspect ratio. */
  None(const("none")),

  /** The icon is scaled in the x-dimension to fit the width of the text. */
  Width(const("width")),

  /** The icon is scaled in the y-dimension to fit the height of the text. */
  Height(const("height")),

  /** The icon is scaled in both x- and y-dimensions. */
  Both(const("both")),
}

/** Orientation of icon when map is pitched. */
@Immutable
public enum class IconPitchAlignment(override val expr: Expression<String>) : LayerPropertyEnum {
  /** The icon is aligned to the plane of the map. */
  Map(const("map")),

  /** The icon is aligned to the plane of the viewport, i.e. as if glued to the screen */
  Viewport(const("viewport")),

  /** Automatically matches the value of [IconRotationAlignment] */
  Auto(const("auto")),
}

/** Orientation of text when map is pitched. */
@Immutable
public enum class TextPitchAlignment(override val expr: Expression<String>) : LayerPropertyEnum {
  /** The text is aligned to the plane of the map. */
  Map(const("map")),

  /** The text is aligned to the plane of the viewport, i.e. as if glued to the screen */
  Viewport(const("viewport")),

  /** Automatically matches the value of [TextRotationAlignment] */
  Auto(const("auto")),
}

/**
 * In combination with [SymbolPlacement], determines the rotation behavior of the individual glyphs
 * forming the text.
 */
@Immutable
public enum class TextRotationAlignment(override val expr: Expression<String>) : LayerPropertyEnum {
  /**
   * For [SymbolPlacement.Point], aligns text east-west. Otherwise, aligns text x-axes with the
   * line.
   */
  Map(const("map")),

  /**
   * Produces glyphs whose x-axes are aligned with the x-axis of the viewport, regardless of the
   * [SymbolPlacement].
   */
  Viewport(const("viewport")),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [TextRotationAlignment.Viewport]. Otherwise,
   * aligns glyphs to the x-axis of the viewport and places them along the line.
   *
   * **Note**: This value not supported on native platforms, yet
   */
  ViewportGlyph(const("viewport-glyph")),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [TextRotationAlignment.Viewport]. Otherwise,
   * this is equivalent to [TextRotationAlignment.Map].
   */
  Auto(const("auto")),
}

/** How the text will be laid out. */
@Immutable
public enum class TextWritingMode(override val expr: Expression<String>) : LayerPropertyEnum {
  /**
   * If a text's language supports horizontal writing mode, symbols with point placement would be
   * laid out horizontally.
   */
  Horizontal(const("horizontal")),

  /**
   * If a text's language supports vertical writing mode, symbols with point placement would be laid
   * out vertically.
   */
  Vertical(const("vertical")),
}

/** Text justification options. */
@Immutable
public enum class TextJustify(override val expr: Expression<String>) : LayerPropertyEnum {
  /** The text is aligned towards the anchor position. */
  Auto(const("auto")),

  /** The text is aligned to the left. */
  Left(const("left")),

  /** The text is centered. */
  Center(const("center")),

  /** The text is aligned to the right. */
  Right(const("right")),
}

/** Specifies how to capitalize text, similar to the CSS text-transform property. */
@Immutable
public enum class TextTransform(override val expr: Expression<String>) : LayerPropertyEnum {
  /** The text is not altered. */
  None(const("none")),

  /** Forces all letters to be displayed in uppercase. */
  Uppercase(const("uppercase")),

  /** Forces all letters to be displayed in lowercase. */
  Lowercase(const("lowercase")),
}
