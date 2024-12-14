package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import kotlin.time.Duration

/**
 * Represents a value that an [Expression] can resolve to. These types are never actually
 * instantiated at runtime; they're only used as type parameters to hint at the type of an
 * [Expression]
 */
public sealed interface ExpressionValue

/**
 * Represents an [Expression] that resolves to a true or false value. See [ExpressionScope.const].
 */
public sealed interface BooleanValue : ExpressionValue, EquatableValue

/**
 * Represents an [Expression] that resolves to a numeric quantity. Corresponds to numbers in the
 * JSON style spec. Use [ExpressionScope.const] to create a literal [ScalarValue].
 *
 * @param U the unit type of the scalar value. For dimensionless quantities, use [Unit].
 */
public sealed interface ScalarValue<U> :
  ExpressionValue,
  MatchableValue,
  InterpolateableValue<U>,
  ComparableValue<ScalarValue<U>>,
  EquatableValue

/**
 * Represents an [Expression] that resolves to a dimensionless quantity. See
 * [ExpressionScope.const].
 */
public typealias FloatValue = ScalarValue<Unit>

/**
 * Represents an [Expression] that resolves to an integer dimensionless quantity. See
 * [ExpressionScope.const].
 */
public sealed interface IntValue : ScalarValue<Unit>

/**
 * Represents an [Expression] that resolves to device-independent pixels ([Dp]). See
 * [ExpressionScope.const].
 */
public typealias DpValue = ScalarValue<Dp>

/**
 * Represents an [Expression] that resolves to an amount of time with millisecond precision
 * ([Duration]). See [ExpressionScope.const].
 */
public typealias DurationValue = ScalarValue<Duration>

/** Represents an [Expression] that resolves to a string value. See [ExpressionScope.const]. */
public sealed interface StringValue :
  ExpressionValue, MatchableValue, ComparableValue<StringValue>, EquatableValue

/**
 * Represents an [Expression] that resolves to an enum string. See [ExpressionScope.const].
 *
 * @param T The [EnumValue] descendent type that this value represents.
 */
public sealed interface EnumValue<out T> : StringValue {
  /** The string expression representing this enum value. You probably don't need this. */
  public val stringConst: Expression<StringValue>
}

/** Represents an [Expression] that resolves to a [Color] value. See [ExpressionScope.const]. */
public sealed interface ColorValue : ExpressionValue, InterpolateableValue<ColorValue>

/**
 * Represents an [Expression] that resolves to a map value (corresponds to a JSON object). See
 * [ExpressionScope.literal].
 */
public sealed interface MapValue : ExpressionValue

/**
 * Represents an [Expression] that resolves to a list value (corresponds to a JSON array). See
 * [ExpressionScope.literal].
 */
public sealed interface ListValue<out T : ExpressionValue> : ExpressionValue

/**
 * Represents an [Expression] that resolves to a list of scalar values.
 *
 * @param U the unit type of the scalar values. For dimensionless quantities, use [Unit].
 */
public sealed interface VectorValue<U> :
  ListValue<ScalarValue<U>>, InterpolateableValue<VectorValue<U>>

/**
 * Represents an [Expression] that resolves to a 2D floating point offset in physical pixels
 * ([Offset]). See [ExpressionScope.const].
 */
public sealed interface OffsetValue : VectorValue<Unit>

/**
 * Represents an [Expression] that resolves to a 2D floating point offset in device-independent
 * pixels ([DpOffset]). See [ExpressionScope.const].
 */
public sealed interface DpOffsetValue : VectorValue<Dp>

/**
 * Represents an [Expression] that resolves to an absolute (layout direction unaware) padding
 * applied along the edges inside a box ([PaddingValues.Absolute]). See [ExpressionScope.const].
 */
public sealed interface PaddingValue : VectorValue<Dp>

/**
 * Represents an [Expression] that resolves to a collator object for use in locale-dependent
 * comparison operations. See [ExpressionScope.collator].
 */
public sealed interface CollatorValue : ExpressionValue

/** Represents an [Expression] that resolves to a formatted string. See [ExpressionScope.format]. */
public sealed interface FormattedValue : ExpressionValue

/** Represents an [Expression] that resolves to a geometry object. */
public sealed interface GeoJsonValue : ExpressionValue

/** Represents an [Expression] that resolves to an image. See [ExpressionScope.image]. */
public sealed interface ImageValue : ExpressionValue

/**
 * Represents an [Expression] that resolves to an interpolation type. See [ExpressionScope.linear],
 * [ExpressionScope.exponential], and [ExpressionScope.cubicBezier].
 */
public sealed interface InterpolationValue : ExpressionValue

/**
 * Represents an [Expression] that resolves to a value that can be compared for equality. See
 * [ExpressionScope.eq] and [ExpressionScope.neq].
 */
public sealed interface EquatableValue : ExpressionValue

/**
 * Union type for an [Expression] that resolves to a value that can be matched. See
 * [ExpressionScope.match].
 */
public sealed interface MatchableValue : ExpressionValue

/**
 * Union type for an [Expression] that resolves to a value that can be ordered with other values of
 * its type. See [ExpressionScope.gt], [ExpressionScope.lt], [ExpressionScope.gte], and
 * [ExpressionScope.lte].
 *
 * @param T the type of the value that can be compared against for ordering.
 */
public sealed interface ComparableValue<T> : ExpressionValue

/**
 * Union type for an [Expression] that resolves to a value that can be interpolated. See
 * [ExpressionScope.interpolate].
 *
 * @param T the type of values that can be interpolated between.
 */
public sealed interface InterpolateableValue<T> : ExpressionValue

/** The type of value resolved from an expression, as returned by [ExpressionScope.type]. */
public enum class ExpressionType(override val stringConst: Expression<StringValue>) :
  EnumValue<ExpressionType> {
  Number(Expression.ofString("number")),
  String(Expression.ofString("string")),
  Object(Expression.ofString("object")),
  Boolean(Expression.ofString("boolean")),
  Color(Expression.ofString("color")),
  Array(Expression.ofString("array")),
}

/** Type of a GeoJson feature, as returned by [ExpressionScope.geometryType]. */
public enum class GeometryType(override val stringConst: Expression<StringValue>) :
  EnumValue<GeometryType> {
  Point(Expression.ofString("Point")),
  LineString(Expression.ofString("LineString")),
  Polygon(Expression.ofString("Polygon")),
  MultiPoint(Expression.ofString("MultiPoint")),
  MultiLineString(Expression.ofString("MultiLineString")),
  MultiPolygon(Expression.ofString("MultiPolygon")),
}

/** Frame of reference for offsetting geometry. */
public enum class TranslateAnchor(override val stringConst: Expression<StringValue>) :
  EnumValue<TranslateAnchor> {
  /** Offset is relative to the map */
  Map(Expression.ofString("map")),

  /** Offset is relative to the viewport */
  Viewport(Expression.ofString("viewport")),
}

/** Scaling behavior of circles when the map is pitched. */
public enum class CirclePitchScale(override val stringConst: Expression<StringValue>) :
  EnumValue<CirclePitchScale> {
  /**
   * Circles are scaled according to their apparent distance to the camera, i.e. as if they are on
   * the map.
   */
  Map(Expression.ofString("map")),

  /** Circles are not scaled, i.e. as if glued to the viewport. */
  Viewport(Expression.ofString("viewport")),
}

/** Orientation of circles when the map is pitched. */
public enum class CirclePitchAlignment(override val stringConst: Expression<StringValue>) :
  EnumValue<CirclePitchAlignment> {
  /** Circles are aligned to the plane of the map, i.e. flat on top of the map. */
  Map(Expression.ofString("map")),

  /** Circles are aligned to the plane of the viewport, i.e. facing the camera. */
  Viewport(Expression.ofString("viewport")),
}

/** Direction of light source when map is rotated. */
public enum class IlluminationAnchor(override val stringConst: Expression<StringValue>) :
  EnumValue<IlluminationAnchor> {

  /** The hillshade illumination is relative to the north direction. */
  Map(Expression.ofString("map")),

  /** The hillshade illumination is relative to the top of the viewport. */
  Viewport(Expression.ofString("viewport")),
}

/** Display of joined lines */
public enum class LineJoin(override val stringConst: Expression<StringValue>) :
  EnumValue<LineJoin> {
  /**
   * A join with a squared-off end which is drawn beyond the endpoint of the line at a distance of
   * one-half of the line's width.
   */
  Bevel(Expression.ofString("bevel")),

  /**
   * A join with a rounded end which is drawn beyond the endpoint of the line at a radius of
   * one-half of the line's width and centered on the endpoint of the line.
   */
  Round(Expression.ofString("round")),

  /**
   * A join with a sharp, angled corner which is drawn with the outer sides beyond the endpoint of
   * the path until they meet.
   */
  Miter(Expression.ofString("miter")),
}

/** Display of line endings */
public enum class LineCap(override val stringConst: Expression<StringValue>) : EnumValue<LineCap> {
  /** A cap with a squared-off end which is drawn to the exact endpoint of the line. */
  Butt(Expression.ofString("butt")),

  /**
   * A cap with a rounded end which is drawn beyond the endpoint of the line at a radius of one-half
   * of the line's width and centered on the endpoint of the line.
   */
  Round(Expression.ofString("round")),

  /**
   * A cap with a squared-off end which is drawn beyond the endpoint of the line at a distance of
   * one-half of the line's width.
   */
  Square(Expression.ofString("square")),
}

/**
 * The resampling/interpolation method to use for overscaling, also known as texture magnification
 * filter
 */
public enum class RasterResampling(override val stringConst: Expression<StringValue>) :
  EnumValue<RasterResampling> {
  /**
   * (Bi)linear filtering interpolates pixel values using the weighted average of the four closest
   * original source pixels creating a smooth but blurry look when overscaled
   */
  Linear(Expression.ofString("linear")),

  /**
   * Nearest neighbor filtering interpolates pixel values using the nearest original source pixel
   * creating a sharp but pixelated look when overscaled
   */
  Nearest(Expression.ofString("nearest")),
}

/** Symbol placement relative to its geometry. */
public enum class SymbolPlacement(override val stringConst: Expression<StringValue>) :
  EnumValue<SymbolPlacement> {
  /** The label is placed at the point where the geometry is located. */
  Point(Expression.ofString("point")),

  /**
   * The label is placed along the line of the geometry. Can only be used on LineString and Polygon
   * geometries.
   */
  Line(Expression.ofString("line")),

  /**
   * The label is placed at the center of the line of the geometry. Can only be used on LineString
   * and Polygon geometries. Note that a single feature in a vector tile may contain multiple line
   * geometries.
   */
  LineCenter(Expression.ofString("line-center")),
}

/**
 * Determines whether overlapping symbols in the same layer are rendered in the order that they
 * appear in the data source or by their y-position relative to the viewport. To control the order
 * and prioritization of symbols otherwise, use `sortKey`.
 */
public enum class SymbolZOrder(override val stringConst: Expression<StringValue>) :
  EnumValue<SymbolZOrder> {
  /**
   * Sorts symbols by `sortKey` if set. Otherwise, sorts symbols by their y-position relative to the
   * viewport if `iconAllowOverlap` or `textAllowOverlap` is set to `true` or `iconIgnorePlacement`
   * or `textIgnorePlacement` is `false`.
   */
  Auto(Expression.ofString("auto")),

  /**
   * Sorts symbols by their y-position relative to the viewport if `iconAllowOverlap` or
   * `textAllowOverlap` is set to `true` or `iconIgnorePlacement` or `textIgnorePlacement` is
   * `false`.
   */
  ViewportY(Expression.ofString("viewport-y")),

  /**
   * Sorts symbols by `sortKey` if set. Otherwise, no sorting is applied; symbols are rendered in
   * the same order as the source data.
   */
  Source(Expression.ofString("source")),
}

/** Part of the icon/text placed closest to the anchor. */
public enum class SymbolAnchor(override val stringConst: Expression<StringValue>) :
  EnumValue<SymbolAnchor> {
  /** The center of the icon is placed closest to the anchor. */
  Center(Expression.ofString("center")),

  /** The left side of the icon is placed closest to the anchor. */
  Left(Expression.ofString("left")),

  /** The right side of the icon is placed closest to the anchor. */
  Right(Expression.ofString("right")),

  /** The top of the icon is placed closest to the anchor. */
  Top(Expression.ofString("top")),

  /** The bottom of the icon is placed closest to the anchor. */
  Bottom(Expression.ofString("bottom")),

  /** The top left corner of the icon is placed closest to the anchor. */
  TopLeft(Expression.ofString("top-left")),

  /** The top right corner of the icon is placed closest to the anchor. */
  TopRight(Expression.ofString("top-right")),

  /** The bottom left corner of the icon is placed closest to the anchor. */
  BottomLeft(Expression.ofString("bottom-left")),

  /** The bottom right corner of the icon is placed closest to the anchor. */
  BottomRight(Expression.ofString("bottom-right")),
}

/** Controls whether to show an icon/text when it overlaps other symbols on the map. */
public enum class SymbolOverlap(override val stringConst: Expression<StringValue>) :
  EnumValue<SymbolOverlap> {
  /** The icon/text will be hidden if it collides with any other previously drawn symbol. */
  Never(Expression.ofString("never")),

  /** The icon/text will be visible even if it collides with any other previously drawn symbol. */
  Always(Expression.ofString("always")),

  /**
   * If the icon/text collides with another previously drawn symbol, the overlap mode for that
   * symbol is checked. If the previous symbol was placed using never overlap mode, the new
   * icon/text is hidden. If the previous symbol was placed using always or cooperative overlap
   * mode, the new icon/text is visible.
   */
  Cooperative(Expression.ofString("cooperative")),
}

/** In combination with [SymbolPlacement], determines the rotation behavior of icons. */
public enum class IconRotationAlignment(override val stringConst: Expression<StringValue>) :
  EnumValue<IconRotationAlignment> {
  /**
   * For [SymbolPlacement.Point], aligns icons east-west. Otherwise, aligns icon x-axes with the
   * line.
   */
  Map(Expression.ofString("map")),

  /**
   * Produces icons whose x-axes are aligned with the x-axis of the viewport, regardless of the
   * [SymbolPlacement].
   */
  Viewport(Expression.ofString("viewport")),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [IconRotationAlignment.Viewport]. Otherwise,
   * this is equivalent to [IconRotationAlignment.Map].
   */
  Auto(Expression.ofString("auto")),
}

/** Scales the icon to fit around the associated text. */
public enum class IconTextFit(override val stringConst: Expression<StringValue>) :
  EnumValue<IconTextFit> {
  /** The icon is displayed at its intrinsic aspect ratio. */
  None(Expression.ofString("none")),

  /** The icon is scaled in the x-dimension to fit the width of the text. */
  Width(Expression.ofString("width")),

  /** The icon is scaled in the y-dimension to fit the height of the text. */
  Height(Expression.ofString("height")),

  /** The icon is scaled in both x- and y-dimensions. */
  Both(Expression.ofString("both")),
}

/** Orientation of icon when map is pitched. */
public enum class IconPitchAlignment(override val stringConst: Expression<StringValue>) :
  EnumValue<IconPitchAlignment> {
  /** The icon is aligned to the plane of the map. */
  Map(Expression.ofString("map")),

  /** The icon is aligned to the plane of the viewport, i.e. as if glued to the screen */
  Viewport(Expression.ofString("viewport")),

  /** Automatically matches the value of [IconRotationAlignment] */
  Auto(Expression.ofString("auto")),
}

/** Orientation of text when map is pitched. */
public enum class TextPitchAlignment(override val stringConst: Expression<StringValue>) :
  EnumValue<TextPitchAlignment> {
  /** The text is aligned to the plane of the map. */
  Map(Expression.ofString("map")),

  /** The text is aligned to the plane of the viewport, i.e. as if glued to the screen */
  Viewport(Expression.ofString("viewport")),

  /** Automatically matches the value of [TextRotationAlignment] */
  Auto(Expression.ofString("auto")),
}

/**
 * In combination with [SymbolPlacement], determines the rotation behavior of the individual glyphs
 * forming the text.
 */
public enum class TextRotationAlignment(override val stringConst: Expression<StringValue>) :
  EnumValue<TextRotationAlignment> {
  /**
   * For [SymbolPlacement.Point], aligns text east-west. Otherwise, aligns text x-axes with the
   * line.
   */
  Map(Expression.ofString("map")),

  /**
   * Produces glyphs whose x-axes are aligned with the x-axis of the viewport, regardless of the
   * [SymbolPlacement].
   */
  Viewport(Expression.ofString("viewport")),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [TextRotationAlignment.Viewport]. Otherwise,
   * aligns glyphs to the x-axis of the viewport and places them along the line.
   *
   * **Note**: This value not supported on native platforms, yet, see
   * [maplibre-native#250](https://github.com/maplibre/maplibre-native/issues/250)**
   */
  ViewportGlyph(Expression.ofString("viewport-glyph")),

  /**
   * For [SymbolPlacement.Point], this is equivalent to [TextRotationAlignment.Viewport]. Otherwise,
   * this is equivalent to [TextRotationAlignment.Map].
   */
  Auto(Expression.ofString("auto")),
}

/** How the text will be laid out. */
public enum class TextWritingMode(override val stringConst: Expression<StringValue>) :
  EnumValue<TextWritingMode> {
  /**
   * If a text's language supports horizontal writing mode, symbols with point placement would be
   * laid out horizontally.
   */
  Horizontal(Expression.ofString("horizontal")),

  /**
   * If a text's language supports vertical writing mode, symbols with point placement would be laid
   * out vertically.
   */
  Vertical(Expression.ofString("vertical")),
}

/** Text justification options. */
public enum class TextJustify(override val stringConst: Expression<StringValue>) :
  EnumValue<TextJustify> {
  /** The text is aligned towards the anchor position. */
  Auto(Expression.ofString("auto")),

  /** The text is aligned to the left. */
  Left(Expression.ofString("left")),

  /** The text is centered. */
  Center(Expression.ofString("center")),

  /** The text is aligned to the right. */
  Right(Expression.ofString("right")),
}

/** Specifies how to capitalize text, similar to the CSS text-transform property. */
public enum class TextTransform(override val stringConst: Expression<StringValue>) :
  EnumValue<TextTransform> {
  /** The text is not altered. */
  None(Expression.ofString("none")),

  /** Forces all letters to be displayed in uppercase. */
  Uppercase(Expression.ofString("uppercase")),

  /** Forces all letters to be displayed in lowercase. */
  Lowercase(Expression.ofString("lowercase")),
}
