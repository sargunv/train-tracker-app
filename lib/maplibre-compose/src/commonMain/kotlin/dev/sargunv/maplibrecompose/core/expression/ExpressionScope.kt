package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
public interface ExpressionScope {

  // basic types: https://maplibre.org/maplibre-style-spec/types/

  // region Literals

  public fun const(string: String): StringExpression = ExpressionImpl.ofString(string)

  @Suppress("UNCHECKED_CAST")
  public fun <T : LayerPropertyEnum> const(value: T): EnumExpression<T> =
    value.expr as EnumExpression<T>

  public fun const(float: Float): FloatExpression = ExpressionImpl.ofFloat(float)

  public fun const(int: Int): IntExpression = ExpressionImpl.ofInt(int)

  public fun const(dp: Dp): DpExpression = ExpressionImpl.ofFloat(dp.value)

  public fun const(bool: Boolean): BooleanExpression =
    if (bool) ExpressionImpl.ofTrue else ExpressionImpl.ofFalse

  public fun const(color: Color): ColorExpression = ExpressionImpl.ofColor(color)

  public fun const(offset: Offset): OffsetExpression = ExpressionImpl.ofOffset(offset)

  public fun const(dpOffset: DpOffset): DpOffsetExpression =
    ExpressionImpl.ofOffset(Offset(dpOffset.x.value, dpOffset.y.value))

  public fun const(padding: PaddingValues.Absolute): PaddingExpression =
    ExpressionImpl.ofPadding(padding)

  /**
   * For simplicity, the expression type system does not encode nullability, so the return value of
   * this function is assignable to any kind of expression.
   */
  @Suppress("UNCHECKED_CAST") public fun <T : Expression> nil(): T = ExpressionImpl.ofNull as T

  // endregion

  // region Conversion

  public val NumberExpression.dp: DpExpression
    get() = this as DpExpression

  // endregion

  // region Variable binding

  /**
   * Binds expressions to named variables, which can then be referenced in the result expression
   * using [useVariable].
   */
  public fun <T : Expression> withVariable(name: String, value: Expression, expression: T): T =
    callFn("let", const(name), value, expression)

  /** References variable bound using [withVariable]. */
  public fun <T : Expression> useVariable(name: String): T = callFn("var", const(name))

  // endregion

  // region Types

  /** Produces a literal list value. */
  public fun <T : Expression> literal(values: List<T>): ListExpression<T> =
    callFn("literal", ExpressionImpl.ofList(values))

  /** Produces a literal map value. */
  public fun literal(values: Map<String, Expression>): MapExpression =
    callFn("literal", ExpressionImpl.ofMap(values))

  /**
   * Returns a string describing the type of this expression. Either "boolean", "string", "number",
   * "color" or "array".
   */
  public fun Expression.type(): StringExpression = callFn("typeof", this)

  /**
   * Asserts that this is a list (optionally with a specific item [type] and [length]).
   *
   * If, when the input expression is evaluated, it is not of the asserted type, then this assertion
   * will cause the whole expression to be aborted.
   */
  public fun Expression.asList(
    type: StringExpression? = null,
    length: IntExpression? = null,
  ): ListExpression<*> {
    val args = buildList {
      type?.let { add(type) }
      length?.let { add(length) }
    }
    return callFn("array", this, *args.toTypedArray<Expression>())
  }

  /**
   * Asserts that this value is a string.
   *
   * In case this expression is not a string, each of the [fallbacks] is evaluated in order until a
   * string is obtained. If none of the inputs are strings, the expression is an error.
   */
  public fun Expression.asString(vararg fallbacks: Expression): StringExpression =
    callFn("string", this, *fallbacks)

  /**
   * Asserts that this value is a number.
   *
   * In case this expression is not a number, each of the [fallbacks] is evaluated in order until a
   * number is obtained. If none of the inputs are numbers, the expression is an error.
   */
  public fun Expression.asNumber(vararg fallbacks: Expression): NumberExpression =
    callFn("number", this, *fallbacks)

  /**
   * Asserts that this value is a boolean.
   *
   * In case this expression is not a boolean, each of the [fallbacks] is evaluated in order until a
   * boolean is obtained. If none of the inputs are booleans, the expression is an error.
   */
  public fun Expression.asBoolean(vararg fallbacks: Expression): BooleanExpression =
    callFn("boolean", this, *fallbacks)

  /**
   * Asserts that this value is a map.
   *
   * In case this expression is not a map, each of the [fallbacks] is evaluated in order until a map
   * is obtained. If none of the inputs are maps, the expression is an error.
   */
  public fun Expression.asMap(vararg fallbacks: Expression): MapExpression =
    callFn("object", this, *fallbacks)

  /**
   * Returns a collator for use in locale-dependent comparison operations. The [caseSensitive] and
   * [diacriticSensitive] options default to `false`. The [locale] argument specifies the IETF
   * language tag of the locale to use. If none is provided, the default locale is used. If the
   * requested locale is not available, the collator will use a system-defined fallback locale. Use
   * [resolvedLocale] to test the results of locale fallback behavior.
   */
  public fun collator(
    caseSensitive: BooleanExpression? = null,
    diacriticSensitive: BooleanExpression? = null,
    locale: StringExpression? = null,
  ): CollatorExpression =
    callFn(
      "collator",
      buildOptions {
        caseSensitive?.let { put("case-sensitive", it) }
        diacriticSensitive?.let { put("diacritic-sensitive", it) }
        locale?.let { put("locale", it) }
      },
    )

  /**
   * Returns a formatted string for displaying mixed-format text in the `textField` property (see
   * [SymbolLayer][dev.sargunv.maplibrecompose.compose.layer.SymbolLayer]). The input may contain a
   * string literal or expression, including an 'image' expression.
   *
   * Example:
   * ```
   * format(
   *   get("name").substring(const(0), const(1)).uppercase() to FormatStyle(textScale = const(1.5)),
   *   get("name").substring(const(1)) to FormatStyle(),
   * )
   * ```
   *
   * Capitalizes the first letter of the features' property "name" and formats it to be extra-large,
   * the rest of the name is written normally.
   */
  public fun format(vararg sections: Pair<Expression, FormatStyle>): FormattedExpression =
    callFn(
      "format",
      *sections.foldToArgs { (value, style) ->
        add(value)
        add(
          buildOptions {
            style.textFont?.let { put("text-font", it) }
            style.textColor?.let { put("text-color", it) }
            style.fontScale?.let { put("font-scale", it) }
          }
        )
      },
    )

  /** Use a string as a formatted value without any extra formatting */
  public fun format(value: StringExpression): FormattedExpression =
    callFn("format", value, buildOptions {})

  public data class FormatStyle(
    val textFont: StringExpression? = null,
    val textColor: StringExpression? = null,
    val fontScale: FloatExpression? = null,
  )

  /**
   * Returns an image type for use in `iconImage` (see
   * [SymbolLayer][dev.sargunv.maplibrecompose.compose.layer.SymbolLayer]), `pattern` entries (see
   * [BackgroundLayer][dev.sargunv.maplibrecompose.compose.layer.BackgroundLayer],
   * [FillLayer][dev.sargunv.maplibrecompose.compose.layer.FillLayer],
   * [FillExtrusionLayer][dev.sargunv.maplibrecompose.compose.layer.FillExtrusionLayer],
   * [LineLayer][dev.sargunv.maplibrecompose.compose.layer.LineLayer]) and as a section in the
   * [format] expression.
   *
   * If set, the image argument will check that the requested image exists in the style and will
   * return either the resolved image name or `null`, depending on whether or not the image is
   * currently in the style. This validation process is synchronous and requires the image to have
   * been added to the style before requesting it in the image argument.
   */
  public fun image(value: StringExpression): ImageExpression = callFn("image", value)

  /**
   * Converts this number into a string representation using the provided formatting rules.
   *
   * @param locale BCP 47 language tag for which locale to use
   * @param currency an ISO 4217 code to use for currency-style formatting
   * @param minFractionDigits minimum fractional digits to include
   * @param maxFractionDigits maximum fractional digits to include
   */
  public fun ScalarExpression.numberFormat(
    locale: StringExpression? = null,
    currency: StringExpression? = null,
    minFractionDigits: IntExpression? = null,
    maxFractionDigits: IntExpression? = null,
  ): StringExpression =
    callFn(
      "number-format",
      this,
      buildOptions {
        locale?.let { put("locale", it) }
        currency?.let { put("currency", it) }
        minFractionDigits?.let { put("min-fraction-digits", it) }
        maxFractionDigits?.let { put("max-fraction-digits", it) }
      },
    )

  /**
   * Converts this expression to a string.
   *
   * If this is ...
   * - `null`, the result is `""`
   * - a boolean, the result is `"true"` or `"false"`
   * - a number, it is converted to a string as specified by the "NumberToString" algorithm of the
   *   ECMAScript Language Specification.
   * - a color, it is converted to a string of the form `"rgba(r,g,b,a)"`, where `r`, `g`, and `b`
   *   are numerals ranging from 0 to 255, and `a` ranges from 0 to 1.
   *
   * Otherwise, the input is converted to a string in the format specified by the JSON.stringify
   * function of the ECMAScript Language Specification.
   */
  public fun Expression.convertToString(): StringExpression = callFn("to-string", this)

  /**
   * Converts this expression to a number.
   *
   * If this expression is `null` or `false`, the result is `0`. If this is `true`, the result is
   * `1`. If the input is a string, it is converted to a number as specified by the "ToNumber
   * Applied to the String Type" algorithm of the ECMAScript Language Specification.
   *
   * In case this expression cannot be converted to a number, each of the [fallbacks] is evaluated
   * in order until the first successful conversion is obtained. If none of the inputs can be
   * converted, the expression is an error.
   */
  public fun Expression.convertToNumber(vararg fallbacks: Expression): NumberExpression =
    callFn("to-number", this, *fallbacks)

  /**
   * Converts this expression to a boolean expression.
   *
   * The result is `false` when then this is an empty string, `0`, `false`,`null` or `NaN`;
   * otherwise it is `true`.
   */
  public fun Expression.convertToBoolean(): BooleanExpression = callFn("to-boolean", this)

  /**
   * Converts this expression to a color expression.
   *
   * In case this expression cannot be converted to a color, each of the [fallbacks] is evaluated in
   * order until the first successful conversion is obtained. If none of the inputs can be
   * converted, the expression is an error.
   */
  public fun Expression.convertToColor(vararg fallbacks: Expression): ColorExpression =
    callFn("to-color", this, *fallbacks)

  // endregion

  // region Lookup

  /** Returns the item at [index]. */
  public operator fun <T : Expression> ListExpression<T>.get(index: IntExpression): T =
    callFn("at", index, this)

  /** Returns whether this list contains the [item]. */
  @JvmName("containsList")
  public fun <T : Expression> ListExpression<T>.contains(item: T): BooleanExpression =
    callFn("in", item, this)

  /** Returns whether this string contains the [substring]. */
  @JvmName("containsString")
  public fun StringExpression.contains(substring: StringExpression): BooleanExpression =
    callFn("in", substring, this)

  /**
   * Returns the first index at which the [substring] is located in this string, or `-1` if it
   * cannot be found. Accepts an optional [startIndex] from where to begin the search.
   */
  @JvmName("indexOfString")
  public fun StringExpression.indexOf(
    substring: StringExpression,
    startIndex: IntExpression? = null,
  ): IntExpression {
    val args = buildList {
      add(substring)
      add(this@indexOf)
      startIndex?.let { add(it) }
    }
    return callFn("index-of", *args.toTypedArray<Expression>())
  }

  /**
   * Returns the first index at which the [item] is located in this list, or `-1` if it cannot be
   * found. Accepts an optional [startIndex] from where to begin the search.
   */
  @JvmName("indexOfList")
  public fun ListExpression<*>.indexOf(
    item: Expression,
    startIndex: IntExpression? = null,
  ): IntExpression {
    val args = buildList {
      add(item)
      add(this@indexOf)
      startIndex?.let { add(it) }
    }
    return callFn("index-of", *args.toTypedArray())
  }

  /**
   * Returns a substring from this string from the [startIndex] (inclusive) to the end of the string
   * if [endIndex] is not specified or `null`, otherwise to [endIndex] (exclusive).
   *
   * A UTF-16 surrogate pair counts as a single position.
   */
  public fun StringExpression.substring(
    startIndex: IntExpression,
    endIndex: IntExpression? = null,
  ): StringExpression {
    val args = buildList {
      add(this@substring)
      add(startIndex)
      endIndex?.let { add(it) }
    }
    return callFn("slice", *args.toTypedArray<Expression>())
  }

  /**
   * Returns the items in this list from the [startIndex] (inclusive) to the end of this list if
   * [endIndex] is not specified or `null`, otherwise to [endIndex] (exclusive).
   */
  public fun <T : Expression> ListExpression<T>.slice(
    startIndex: IntExpression,
    endIndex: IntExpression? = null,
  ): ListExpression<T> {
    val args = buildList {
      add(this@slice)
      add(startIndex)
      endIndex?.let { add(it) }
    }
    return callFn("slice", *args.toTypedArray())
  }

  /**
   * Returns the value corresponding to the given [key] in the current feature's properties or
   * `null` if it is not present.
   */
  public fun <T : Expression> get(key: StringExpression): T = callFn("get", key)

  /** Tests for the presence of a property value [key] in the current feature's properties. */
  public fun has(key: StringExpression): BooleanExpression = callFn("has", key)

  /** Returns the value corresponding the given [key] or `null` if it is not present in this map. */
  public operator fun <T : Expression> MapExpression.get(key: StringExpression): T =
    callFn("get", key, this)

  /** Returns whether the given [key] is in this map. */
  public fun MapExpression.has(key: StringExpression): BooleanExpression = callFn("has", key, this)

  /**
   * Gets the length of this string.
   *
   * A UTF-16 surrogate pair counts as a single position.
   */
  @JvmName("lengthOfString")
  public fun StringExpression.length(): IntExpression = callFn("length", this)

  /** Gets the length of a this list. */
  @JvmName("lengthOfList")
  public fun ListExpression<*>.length(): IntExpression = callFn("length", this)

  // endregion

  // region Decision

  /**
   * Selects the first output from the given [branches] whose corresponding test condition evaluates
   * to `true`, or the [fallback] value otherwise.
   *
   * Example:
   * ```
   * case(
   *   (has("color1") and has("color2")) then
   *     interpolate(Interpolation.Linear, zoom(), 1 to get("color1"), 20 to get("color2")),
   *   has("color") then
   *     get("color"),
   *   const(Color.Red)
   * )
   * ```
   *
   * If the feature has both a "color1" and "color2" property, the result is an interpolation
   * between these two colors based on the zoom level. Otherwise, if the feature has a "color"
   * property, that color is returned. If the feature has none of the three, the color red is
   * returned.
   */
  public fun <T : Expression> case(vararg branches: CaseBranch<T>, fallback: T): T =
    callFn(
      "case",
      *branches.foldToArgs { (test, output) ->
        add(test)
        add(output)
      },
      fallback,
    )

  public data class CaseBranch<Output : Expression>
  internal constructor(internal val test: BooleanExpression, internal val output: Output)

  /** Create a [CaseBranch], see [case] */
  public infix fun <Output : Expression> BooleanExpression.then(
    output: Output
  ): CaseBranch<Output> = CaseBranch(this, output)

  /**
   * Selects the output from the given [branches] whose label value matches the [input], or the
   * [fallback] value if no match is found.
   *
   * Each label must be unique. If the input type does not match the type of the labels, the result
   * will be the [fallback] value.
   *
   * Example:
   * ```
   * match(
   *   get("building_type"),
   *   "residential" then const(Color.Cyan),
   *   listOf("commercial", "industrial") then const(Color.Yellow),
   *   const(Color.Red),
   * )
   * ```
   *
   * If the feature has a property "building_type" with the value "residential", cyan is returned.
   * Otherwise, if the value of that property is either "commercial" or "industrial", yellow is
   * returned. If none of that is true, the fallback is returned, i.e. red.
   */
  public fun <Input : MatchableExpression, Output : Expression> match(
    input: Input,
    vararg branches: MatchBranch<Input, Output>,
    fallback: Output,
  ): Output =
    callFn(
      "match",
      input,
      *branches.foldToArgs { (label, output) ->
        add(label)
        add(output)
      },
      fallback,
    )

  public data class MatchBranch<
    @Suppress("unused")
    Input : MatchableExpression,
    Output : Expression,
  > internal constructor(internal val label: Expression, internal val output: Output)

  /** Create a [MatchBranch], see [match] */
  public infix fun <Output : Expression> String.then(
    output: Output
  ): MatchBranch<StringExpression, Output> = MatchBranch(const(this), output)

  /** Create a [MatchBranch], see [match] */
  public infix fun <Output : Expression> Number.then(
    output: Output
  ): MatchBranch<NumberExpression, Output> = MatchBranch(const(this.toFloat()), output)

  /** Create a [MatchBranch], see [match] */
  @JvmName("stringsThen")
  public infix fun <Output : Expression> List<String>.then(
    output: Output
  ): MatchBranch<StringExpression, Output> =
    MatchBranch(ExpressionImpl.ofList(this.map(::const)), output)

  /** Create a [MatchBranch], see [match] */
  @JvmName("numbersThen")
  public infix fun <Output : Expression> List<Number>.then(
    output: Output
  ): MatchBranch<NumberExpression, Output> =
    MatchBranch(ExpressionImpl.ofList(this.map { const(it.toFloat()) }), output)

  /**
   * Evaluates each expression in [values] in turn until the first non-null value is obtained, and
   * returns that value.
   */
  public fun <T : Expression> coalesce(vararg values: T): T = callFn("coalesce", *values)

  /** Returns whether this expression is equal to [other]. */
  public infix fun Expression.eq(other: Expression): BooleanExpression = callFn("==", this, other)

  /**
   * Returns whether the [left] string expression is equal to the [right] string expression. An
   * optional [collator] (see [ExpressionScope.collator] function) can be specified to control
   * locale-dependent string comparisons.
   */
  public fun eq(
    left: StringExpression,
    right: StringExpression,
    collator: CollatorExpression,
  ): BooleanExpression = callFn("==", left, right, collator)

  /** Returns whether this expression is not equal to [other]. */
  public infix fun Expression.neq(other: Expression): BooleanExpression = callFn("!=", this, other)

  /**
   * Returns whether the [left] string expression is not equal to the [right] string expression. An
   * optional [collator] (see [ExpressionScope.collator]) can be specified to control
   * locale-dependent string comparisons.
   */
  public fun neq(
    left: StringExpression,
    right: StringExpression,
    collator: CollatorExpression,
  ): BooleanExpression = callFn("!=", left, right, collator)

  /**
   * Returns whether this expression is strictly greater than [other].
   *
   * Strings are compared lexicographically (`"b" > "a"`).
   */
  public infix fun ComparableExpression.gt(other: ComparableExpression): BooleanExpression =
    callFn(">", this, other)

  /**
   * Returns whether the [left] string expression is strictly greater than the [right] string
   * expression. An optional [collator] (see [ExpressionScope.collator]) can be specified to control
   * locale-dependent string comparisons.
   *
   * Strings are compared lexicographically (`"b" > "a"`).
   */
  public fun gt(
    left: StringExpression,
    right: StringExpression,
    collator: CollatorExpression,
  ): BooleanExpression = callFn(">", left, right, collator)

  /**
   * Returns whether this expression is strictly less than [other].
   *
   * Strings are compared lexicographically (`"a" < "b"`).
   */
  public infix fun ComparableExpression.lt(other: ComparableExpression): BooleanExpression =
    callFn("<", this, other)

  /**
   * Returns whether the [left] string expression is strictly less than the [right] string
   * expression. An optional [collator] (see [ExpressionScope.collator]) can be specified to control
   * locale-dependent string comparisons.
   *
   * Strings are compared lexicographically (`"a" < "b"`).
   */
  public fun lt(
    left: StringExpression,
    right: StringExpression,
    collator: CollatorExpression,
  ): BooleanExpression = callFn("<", left, right, collator)

  /**
   * Returns whether this expression is greater than or equal to [other].
   *
   * Strings are compared lexicographically (`"b" >= "a"`).
   */
  public infix fun ComparableExpression.gte(other: ComparableExpression): BooleanExpression =
    callFn(">=", this, other)

  /**
   * Returns whether the [left] string expression is greater than or equal to the [right] string
   * expression. An optional [collator] (see [ExpressionScope.collator]) can be specified to control
   * locale-dependent string comparisons.
   *
   * Strings are compared lexicographically (`"b" >= "a"`).
   */
  public fun gte(
    left: StringExpression,
    right: StringExpression,
    collator: CollatorExpression,
  ): BooleanExpression = callFn(">=", left, right, collator)

  /**
   * Returns whether this string expression is less than or equal to [other].
   *
   * Strings are compared lexicographically (`"a" <= "b"`).
   */
  public infix fun ComparableExpression.lte(other: ComparableExpression): BooleanExpression =
    callFn("<=", this, other)

  /**
   * Returns whether the [left] string expression is less than or equal to the [right] string
   * expression. An optional [collator] (see [ExpressionScope.collator]) can be specified to control
   * locale-dependent string comparisons.
   *
   * Strings are compared lexicographically (`"a" < "b"`).
   */
  public fun lte(
    left: StringExpression,
    right: StringExpression,
    collator: CollatorExpression,
  ): BooleanExpression = callFn("<=", left, right, collator)

  /** Returns whether all [expressions] are `true`. */
  public fun all(vararg expressions: BooleanExpression): BooleanExpression =
    callFn("all", *expressions)

  /** Returns whether both this and [other] expressions are `true`. */
  public infix fun BooleanExpression.and(other: BooleanExpression): BooleanExpression =
    all(this, other)

  /** Returns whether any [expressions] are `true`. */
  public fun any(vararg expressions: BooleanExpression): BooleanExpression =
    callFn("any", *expressions)

  /** Returns whether any of this or the [other] expressions are `true`. */
  public infix fun BooleanExpression.or(other: BooleanExpression): BooleanExpression =
    any(this, other)

  /** Negates this expression. */
  @JvmName("notOperator")
  public operator fun BooleanExpression.not(): BooleanExpression = callFn("!", this)

  /**
   * Returns true if the evaluated feature is fully contained inside a boundary of the input
   * geometry, false otherwise. The input value can be a valid GeoJSON of type Polygon,
   * MultiPolygon, Feature, or FeatureCollection. Supported features for evaluation:
   * - Point: Returns false if a point is on the boundary or falls outside the boundary.
   * - LineString: Returns false if any part of a line falls outside the boundary, the line
   *   intersects the boundary, or a line's endpoint is on the boundary.
   */
  public fun within(geometry: GeoJsonExpression): BooleanExpression = callFn("within", geometry)

  // endregion

  // region Ramps, Scales, Curves

  /**
   * Produces discrete, stepped results by evaluating a piecewise-constant function defined by pairs
   * of input and output values ([stops]). Returns the output value of the stop just less than the
   * [input], or the [fallback] if the input is less than the first stop.
   *
   * Example:
   * ```
   * step(zoom(), const(0), 10 to const(2.5), 20 to const(10.5))
   * ```
   *
   * returns 0 if the zoom is less than 10, 2.5 if the zoom is between 10 and less than 20, 10.5 if
   * the zoom is greater than or equal 20.
   */
  public fun <Output : Expression> step(
    input: NumberExpression,
    fallback: Output,
    vararg stops: Pair<Number, Output>,
  ): Output =
    callFn(
      "step",
      input,
      fallback,
      *stops
        .sortedBy { it.first.toFloat() }
        .foldToArgs {
          add(const(it.first.toFloat()))
          add(it.second)
        },
    )

  private fun <Output : InterpolateableExpression> interpolateImpl(
    name: String,
    type: Interpolation,
    input: NumberExpression,
    vararg stops: Pair<Number, Output>,
  ): Output =
    callFn(
      name,
      type,
      input,
      *stops
        .sortedBy { it.first.toDouble() }
        .foldToArgs {
          add(const(it.first.toFloat()))
          add(it.second)
        },
    )

  /**
   * Produces continuous, smooth results by interpolating between pairs of input and output values
   * ([stops]), given the [input] value.
   *
   * Example:
   * ```
   * interpolate(
   *   exponential(2), zoom(),
   *   16 to const(1),
   *   24 to const(256),
   * )
   * ```
   *
   * interpolates exponentially from 1 to 256 in zoom levels 16 to 24. Below zoom 16, it is 1, above
   * zoom 24, it is 256. Applied to for example line width, this has the visual effect that the line
   * stays the same width in meters on the map (rather than on the viewport).
   */
  public fun <Output : InterpolateableExpression> interpolate(
    type: Interpolation,
    input: NumberExpression,
    vararg stops: Pair<Number, Output>,
  ): Output = interpolateImpl("interpolate", type, input, *stops)

  /**
   * Produces continuous, smooth results by interpolating between pairs of input and output values
   * ([stops]), given the [input] value. Works like [interpolate], but the interpolation is
   * performed in the
   * [Hue-Chroma-Luminance color space](https://en.wikipedia.org/wiki/HCL_color_space).
   *
   * Example:
   * ```
   * interpolateHcl(
   *   Interpolation.Linear,
   *   zoom(),
   *   1 to const(Color.Red),
   *   5 to const(Color.Blue),
   *   10 to const(Color.Green)
   * )
   * ```
   *
   * interpolates linearly from red to blue between in zoom levels 1 to 5, then interpolates
   * linearly from blue to green in zoom levels 5 to 10, which it where it remains until maximum
   * zoom.
   */
  public fun interpolateHcl(
    type: Interpolation,
    input: NumberExpression,
    vararg stops: Pair<Number, ColorExpression>,
  ): ColorExpression = interpolateImpl("interpolate-hcl", type, input, *stops)

  /**
   * Produces continuous, smooth results by interpolating between pairs of input and output values
   * ([stops]), given the [input] value. Works like [interpolate], but the interpolation is
   * performed in the [CIELAB color space](https://en.wikipedia.org/wiki/CIELAB_color_space).
   */
  public fun interpolateLab(
    type: Interpolation,
    input: NumberExpression,
    vararg stops: Pair<Number, ColorExpression>,
  ): ColorExpression = interpolateImpl("interpolate-lab", type, input, *stops)

  // endregion

  // region Math

  /** Returns mathematical constant ln(2) = natural logarithm of 2. */
  public fun ln2(): FloatExpression = callFn("ln2")

  /** Returns the mathematical constant π */
  public fun pi(): FloatExpression = callFn("pi")

  /** Returns the mathematical constant e */
  public fun e(): FloatExpression = callFn("e")

  /** Returns the sum of this number expression with [other]. */
  public operator fun <T : ScalarExpression> T.plus(other: T): T = callFn("+", this, other)

  /** Returns the product of this number expression with [other]. */
  public operator fun <T : ScalarExpression> T.times(other: NumberExpression): T =
    callFn("*", this, other)

  /** Returns the result of subtracting [other] from this number expression. */
  public operator fun <T : ScalarExpression> T.minus(other: T): T = callFn("-", this, other)

  /** Negates this number expression. */
  public operator fun <T : ScalarExpression> T.unaryMinus(): T = callFn("-", this)

  /** Returns the result of floating point division of this number expression by [divisor]. */
  public operator fun <T : FloatScalarExpression> T.div(divisor: FloatExpression): T =
    callFn("/", this, divisor)

  /** Returns the remainder after integer division of this number expression by [divisor]. */
  public operator fun <T : IntScalarExpression> T.rem(divisor: IntExpression): T =
    callFn("%", this, divisor)

  /** Returns the result of raising this number expression to the power of [exponent]. */
  public fun NumberExpression.pow(exponent: NumberExpression): FloatExpression =
    callFn("^", this, exponent)

  /** Returns the square root of [value]. */
  public fun sqrt(value: NumberExpression): FloatExpression = callFn("sqrt", value)

  /** Returns the base-ten logarithm of [value]. */
  public fun log10(value: NumberExpression): FloatExpression = callFn("log10", value)

  /** Returns the natural logarithm of [value]. */
  public fun ln(value: NumberExpression): FloatExpression = callFn("ln", value)

  /** Returns the base-two logarithm of [value]. */
  public fun log2(value: NumberExpression): FloatExpression = callFn("log2", value)

  /** Returns the sine of [value]. */
  public fun sin(value: NumberExpression): FloatExpression = callFn("sin", value)

  /** Returns the cosine of [value]. */
  public fun cos(value: NumberExpression): FloatExpression = callFn("cos", value)

  /** Returns the tangent of [value]. */
  public fun tan(value: NumberExpression): FloatExpression = callFn("tan", value)

  /** Returns the arcsine of [value]. */
  public fun asin(value: NumberExpression): FloatExpression = callFn("asin", value)

  /** Returns the arccosine of [value]. */
  public fun acos(value: NumberExpression): FloatExpression = callFn("acos", value)

  /** Returns the arctangent of [value]. */
  public fun atan(value: NumberExpression): FloatExpression = callFn("atan", value)

  /** Returns the smallest of all given [numbers]. */
  public fun <T : ScalarExpression> min(vararg numbers: T): T = callFn("min", *numbers)

  /** Returns the greatest of all given [numbers]. */
  public fun <T : ScalarExpression> max(vararg numbers: T): T = callFn("max", *numbers)

  /** Returns the absolute value of [value], i.e. always a positive value. */
  public fun <T : ScalarExpression> abs(value: T): T = callFn("abs", value)

  /**
   * Rounds [value] to the nearest integer. Halfway values are rounded away from zero.
   *
   * For example `round(const(-1.5))` evaluates to `-2`.
   */
  public fun round(value: NumberExpression): IntExpression = callFn("round", value)

  /** Returns the smallest integer that is greater than or equal to [value]. */
  public fun ceil(value: NumberExpression): IntExpression = callFn("ceil", value)

  /** Returns the largest integer that is less than or equal to [value]. */
  public fun floor(value: NumberExpression): IntExpression = callFn("floor", value)

  /** Returns the shortest distance in meters between the evaluated feature and [geometry]. */
  public fun distance(geometry: GeoJsonExpression): FloatExpression = callFn("distance", geometry)

  // endregion

  // region Color

  /**
   * Returns a four-element list, containing the color's red, green, blue, and alpha components, in
   * that order.
   */
  public fun ColorExpression.toRgbaComponents(): VectorExpression = callFn("to-rgba", this)

  /**
   * Creates a color value from [red], [green], and [blue] components, which must range between 0
   * and 255, and optionally an [alpha] component which must range between 0 and 1.
   *
   * If any component is out of range, the expression is an error.
   */
  public fun rgbColor(
    red: IntExpression,
    green: IntExpression,
    blue: IntExpression,
    alpha: FloatExpression? = null,
  ): ColorExpression =
    if (alpha != null) {
      callFn("rgba", red, green, blue, alpha)
    } else {
      callFn("rgb", red, green, blue)
    }

  // endregion

  // region Feature data

  /**
   * Gets the feature properties object. Note that in some cases, it may be more efficient to use
   * `get("property_name")` directly.
   */
  public fun properties(): MapExpression = callFn("properties")

  /**
   * **Note: Not supported on native platforms. See
   * [maplibre-native#1698](https://github.com/maplibre/maplibre-native/issues/1698)**
   *
   * Retrieves a property value from the current feature's state. Returns `null` if the requested
   * property is not present on the feature's state.
   *
   * A feature's state is not part of the GeoJSON or vector tile data, and must be set
   * programmatically on each feature.
   *
   * When `source.promoteId` is not provided, features are identified by their `id` attribute, which
   * must be an integer or a string that can be cast to an integer. When `source.promoteId` is
   * provided, features are identified by their `promoteId` property, which may be a number, string,
   * or any primitive data type. Note that [featureState] can only be used with layer properties
   * that support data-driven styling.
   */
  // TODO: latest when featureState is supported on native platforms, should document which layer
  //   properties support data-driven styling, i.e. featureState expressions.
  public fun <T> featureState(key: StringExpression): T = callFn("feature-state", key)

  /**
   * Gets the feature's geometry type as a string: "Point", "MultiPoint", "LineString",
   * "MultiLineString", "Polygon" or "MultiPolygon".
   */
  public fun geometryType(): StringExpression = callFn("geometry-type")

  /** Gets the feature's id, if it has one. */
  public fun <T> id(): T = callFn("id")

  /**
   * Gets the progress along a gradient line. Can only be used in the `gradient` property of a line
   * layer, see [LineLayer][dev.sargunv.maplibrecompose.compose.layer.LineLayer].
   */
  public fun lineProgress(value: FloatExpression): FloatExpression = callFn("line-progress", value)

  /**
   * Gets the value of a cluster property accumulated so far. Can only be used in the
   * `clusterProperties` option of a clustered GeoJSON source, see
   * [GeoJsonOptions][dev.sargunv.maplibrecompose.core.source.GeoJsonOptions].
   */
  public fun <T> accumulated(key: StringExpression): T = callFn("accumulated", key)

  // endregion

  // region Zoom

  /**
   * Gets the current zoom level. Note that in layer style properties, [zoom] may only appear as the
   * input to a top-level [step] or [interpolate] (, [interpolateHcl], [interpolateLab], ...)
   * expression.
   */
  public fun zoom(): FloatExpression = callFn("zoom")

  // endregion

  // region Heatmap

  /**
   * Gets the kernel density estimation of a pixel in a heatmap layer, which is a relative measure
   * of how many data points are crowded around a particular pixel. Can only be used in the
   * expression for the `color` parameter in a
   * [HeatmapLayer][dev.sargunv.maplibrecompose.compose.layer.HeatmapLayer].
   */
  public fun heatmapDensity(): FloatExpression = callFn("heatmap-density")

  // endregion

  // region String

  /**
   * Returns `true` if this string is expected to render legibly. Returns `false` if this string
   * contains sections that cannot be rendered without potential loss of meaning (e.g. Indic scripts
   * that require complex text shaping).
   */
  public fun StringExpression.isScriptSupported(): BooleanExpression =
    callFn("is-supported-script", this)

  /**
   * Returns this string converted to uppercase. Follows the Unicode Default Case Conversion
   * algorithm and the locale-insensitive case mappings in the Unicode Character Database.
   */
  public fun StringExpression.uppercase(): StringExpression = callFn("upcase", this)

  /**
   * Returns this string converted to lowercase. Follows the Unicode Default Case Conversion
   * algorithm and the locale-insensitive case mappings in the Unicode Character Database.
   */
  public fun StringExpression.lowercase(): StringExpression = callFn("downcase", this)

  /** Concatenates this string expression with [other]. */
  @JvmName("plusString")
  public operator fun StringExpression.plus(other: StringExpression): StringExpression =
    callFn("concat", this, other)

  /**
   * Returns the IETF language tag of the locale being used by the provided [collator]. This can be
   * used to determine the default system locale, or to determine if a requested locale was
   * successfully loaded.
   */
  public fun resolvedLocale(collator: CollatorExpression): StringExpression =
    callFn("resolved-locale", collator)

  // endregion

  // region Utils

  @Suppress("UNCHECKED_CAST")
  private fun <T : Expression> callFn(function: String, vararg args: Expression) =
    ExpressionImpl.ofList(
      buildList {
        add(const(function))
        args.forEach { add(it) }
      }
    ) as T

  private inline fun buildOptions(block: MutableMap<String, Expression>.() -> Unit) =
    ExpressionImpl.ofMap(mutableMapOf<String, Expression>().apply(block).mapValues { it.value })

  private fun <T> Array<T>.foldToArgs(block: MutableList<Expression>.(element: T) -> Unit) =
    fold(mutableListOf<Expression>()) { acc, element -> acc.apply { block(element) } }
      .toTypedArray()

  private fun <T> List<T>.foldToArgs(block: MutableList<Expression>.(element: T) -> Unit) =
    fold(mutableListOf<Expression>()) { acc, element -> acc.apply { block(element) } }
      .toTypedArray()

  // endregion
}

/**
 * Helper object implementing [ExpressionScope] to help with importing individual functions from the
 * expressions DSL, or to provide a receiver for functions providing the DSL.
 */
public object Expressions : ExpressionScope
