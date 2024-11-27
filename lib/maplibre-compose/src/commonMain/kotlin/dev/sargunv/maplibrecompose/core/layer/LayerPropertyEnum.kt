package dev.sargunv.maplibrecompose.core.layer

import androidx.compose.runtime.Immutable

internal interface LayerPropertyEnum {
  val value: String
}

/** Frame of reference for offsetting geometry */
@Immutable
public enum class TranslateAnchor(override val value: String) : LayerPropertyEnum {
  /** Offset is relative to the map */
  Map("map"),

  /** Offset is relative to the viewport */
  Viewport("viewport"),
}

/** Scaling behavior of circles when the map is pitched. */
@Immutable
public enum class CirclePitchScale(override val value: String) : LayerPropertyEnum {
  /**
   * Circles are scaled according to their apparent distance to the camera, i.e. as if they are on
   * the map.
   */
  Map("map"),

  /** Circles are not scaled, i.e. as if glued to the viewport. */
  Viewport("viewport"),
}

/** Orientation of circles when the map is pitched. */
@Immutable
public enum class CirclePitchAlignment(override val value: String) : LayerPropertyEnum {
  /** Circles are aligned to the plane of the map, i.e. flat on top of the map. */
  Map("map"),

  /** Circles are aligned to the plane of the viewport, i.e. facing the camera. */
  Viewport("viewport"),
}

/** Direction of light source when map is rotated. */
@Immutable
public enum class IlluminationAnchor(override val value: String) : LayerPropertyEnum {

  /** The hillshade illumination is relative to the north direction. */
  Map("map"),

  /** The hillshade illumination is relative to the top of the viewport. */
  Viewport("viewport"),
}

/** Display of joined lines */
@Immutable
public enum class LineJoin(override val value: String) : LayerPropertyEnum {
  /**
   * A join with a squared-off end which is drawn beyond the endpoint of the line at a distance of
   * one-half of the line's width.
   */
  Bevel("bevel"),

  /**
   * A join with a rounded end which is drawn beyond the endpoint of the line at a radius of
   * one-half of the line's width and centered on the endpoint of the line.
   */
  Round("round"),

  /**
   * A join with a sharp, angled corner which is drawn with the outer sides beyond the endpoint of
   * the path until they meet.
   */
  Miter("miter"),
}

/** Display of line endings */
@Immutable
public enum class LineCap(override val value: String) : LayerPropertyEnum {
  /** A cap with a squared-off end which is drawn to the exact endpoint of the line. */
  Butt("butt"),

  /**
   * A cap with a rounded end which is drawn beyond the endpoint of the line at a radius of one-half
   * of the line's width and centered on the endpoint of the line.
   */
  Round("round"),

  /**
   * A cap with a squared-off end which is drawn beyond the endpoint of the line at a distance of
   * one-half of the line's width.
   */
  Square("square"),
}

/**
 * The resampling/interpolation method to use for overscaling, also known as texture magnification
 * filter
 */
@Immutable
public enum class RasterResampling(override val value: String) : LayerPropertyEnum {
  /**
   * (Bi)linear filtering interpolates pixel values using the weighted average of the four closest
   * original source pixels creating a smooth but blurry look when overscaled
   */
  Linear("linear"),

  /**
   * Nearest neighbor filtering interpolates pixel values using the nearest original source pixel
   * creating a sharp but pixelated look when overscaled
   */
  Nearest("nearest"),
}

/** Symbol placement relative to its geometry. */
@Immutable
public enum class SymbolPlacement(override val value: String) : LayerPropertyEnum {
  /** The label is placed at the point where the geometry is located. */
  Point("point"),

  /**
   * The label is placed along the line of the geometry. Can only be used on LineString and Polygon
   * geometries.
   */
  Line("line"),

  /**
   * The label is placed at the center of the line of the geometry. Can only be used on LineString
   * and Polygon geometries. Note that a single feature in a vector tile may contain multiple line
   * geometries.
   */
  LineCenter("line-center"),
}

/**
 * Determines whether overlapping symbols in the same layer are rendered in the order that they
 * appear in the data source or by their y-position relative to the viewport. To control the order
 * and prioritization of symbols otherwise, use `sortKey`.
 */
@Immutable
public enum class SymbolZOrder(override val value: String) : LayerPropertyEnum {
  /**
   * Sorts symbols by `sortKey` if set. Otherwise, sorts symbols by their y-position relative to the
   * viewport if `iconAllowOverlap` or `textAllowOverlap` is set to `true` or `iconIgnorePlacement`
   * or `textIgnorePlacement` is `false`.
   */
  Auto("auto"),

  /**
   * Sorts symbols by their y-position relative to the viewport if `iconAllowOverlap` or
   * `textAllowOverlap` is set to `true` or `iconIgnorePlacement` or `textIgnorePlacement` is
   * `false`.
   */
  ViewportY("viewport-y"),

  /**
   * Sorts symbols by `sortKey` if set. Otherwise, no sorting is applied; symbols are rendered in
   * the same order as the source data.
   */
  Source("source"),
}

/** Part of the icon/text placed closest to the anchor. */
@Immutable
public enum class SymbolAnchor(override val value: String) : LayerPropertyEnum {
  /** The center of the icon is placed closest to the anchor. */
  Center("center"),

  /** The left side of the icon is placed closest to the anchor. */
  Left("left"),

  /** The right side of the icon is placed closest to the anchor. */
  Right("right"),

  /** The top of the icon is placed closest to the anchor. */
  Top("top"),

  /** The bottom of the icon is placed closest to the anchor. */
  Bottom("bottom"),

  /** The top left corner of the icon is placed closest to the anchor. */
  TopLeft("top-left"),

  /** The top right corner of the icon is placed closest to the anchor. */
  TopRight("top-right"),

  /** The bottom left corner of the icon is placed closest to the anchor. */
  BottomLeft("bottom-left"),

  /** The bottom right corner of the icon is placed closest to the anchor. */
  BottomRight("bottom-right"),
}

/** Controls whether to show an icon/text when it overlaps other symbols on the map. */
@Immutable
public enum class SymbolOverlap(override val value: String) : LayerPropertyEnum {
  /** The icon/text will be hidden if it collides with any other previously drawn symbol. */
  Never("never"),

  /** The icon/text will be visible even if it collides with any other previously drawn symbol. */
  Always("always"),

  /**
   * If the icon/text collides with another previously drawn symbol, the overlap mode for that
   * symbol is checked. If the previous symbol was placed using never overlap mode, the new
   * icon/text is hidden. If the previous symbol was placed using always or cooperative overlap
   * mode, the new icon/text is visible.
   */
  Cooperative("cooperative"),
}

/** In combination with [SymbolPlacement], determines the rotation behavior of icons. */
@Immutable
public enum class IconRotationAlignment(override val value: String) : LayerPropertyEnum {
  /**
   * For [SymbolPlacement.Point], aligns icons east-west. Otherwise, aligns icon x-axes with the
   * line.
   */
  Map("map"),

  /**
   * Produces icons whose x-axes are aligned with the x-axis of the viewport, regardless of the
   * [SymbolPlacement].
   */
  Viewport("viewport"),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [IconRotationAlignment.Viewport]. Otherwise,
   * this is equivalent to [IconRotationAlignment.Map].
   */
  Auto("auto"),
}

/** Scales the icon to fit around the associated text. */
@Immutable
public enum class IconTextFit(override val value: String) : LayerPropertyEnum {
  /** The icon is displayed at its intrinsic aspect ratio. */
  None("none"),

  /** The icon is scaled in the x-dimension to fit the width of the text. */
  Width("width"),

  /** The icon is scaled in the y-dimension to fit the height of the text. */
  Height("height"),

  /** The icon is scaled in both x- and y-dimensions. */
  Both("both"),
}

/** Orientation of icon when map is pitched. */
@Immutable
public enum class IconPitchAlignment(override val value: String) : LayerPropertyEnum {
  /** The icon is aligned to the plane of the map. */
  Map("map"),

  /** The icon is aligned to the plane of the viewport, i.e. as if glued to the screen */
  Viewport("viewport"),

  /** Automatically matches the value of [IconRotationAlignment] */
  Auto("auto"),
}

/** Orientation of text when map is pitched. */
@Immutable
public enum class TextPitchAlignment(override val value: String) : LayerPropertyEnum {
  /** The text is aligned to the plane of the map. */
  Map("map"),

  /** The text is aligned to the plane of the viewport, i.e. as if glued to the screen */
  Viewport("viewport"),

  /** Automatically matches the value of [TextRotationAlignment] */
  Auto("auto"),
}

/**
 * In combination with [SymbolPlacement], determines the rotation behavior of the individual glyphs
 * forming the text.
 */
@Immutable
public enum class TextRotationAlignment(override val value: String) : LayerPropertyEnum {
  /**
   * For [SymbolPlacement.Point], aligns text east-west. Otherwise, aligns text x-axes with the
   * line.
   */
  Map("map"),

  /**
   * Produces glyphs whose x-axes are aligned with the x-axis of the viewport, regardless of the
   * [SymbolPlacement].
   */
  Viewport("viewport"),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [TextRotationAlignment.Viewport]. Otherwise,
   * aligns glyphs to the x-axis of the viewport and places them along the line.
   *
   * **Note**: This value not supported on native platforms, yet
   */
  ViewportGlyph("viewport-glyph"),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [TextRotationAlignment.Viewport]. Otherwise,
   * this is equivalent to [TextRotationAlignment.Map].
   */
  Auto("auto"),
}

/** How the text will be laid out. */
@Immutable
public enum class TextWritingMode(override val value: String) : LayerPropertyEnum {
  /**
   * If a text's language supports horizontal writing mode, symbols with point placement would be
   * laid out horizontally.
   */
  Horizontal("horizontal"),

  /**
   * If a text's language supports vertical writing mode, symbols with point placement would be laid
   * out vertically.
   */
  Vertical("vertical"),
}

/** Text justification options. */
@Immutable
public enum class TextJustify(override val value: String) : LayerPropertyEnum {
  /** The text is aligned towards the anchor position. */
  Auto("auto"),

  /** The text is aligned to the left. */
  Left("left"),

  /** The text is centered. */
  Center("center"),

  /** The text is aligned to the right. */
  Right("right"),
}

/** Specifies how to capitalize text, similar to the CSS text-transform property. */
@Immutable
public enum class TextTransform(override val value: String) : LayerPropertyEnum {
  /** The text is not altered. */
  None("none"),

  /** Forces all letters to be displayed in uppercase. */
  Uppercase("uppercase"),

  /** Forces all letters to be displayed in lowercase. */
  Lowercase("lowercase"),
}
