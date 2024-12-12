package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import io.github.dellisd.spatialk.geojson.Geometry
import io.github.dellisd.spatialk.geojson.MultiPolygon
import io.github.dellisd.spatialk.geojson.Polygon
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
public interface ExpressionScope {

  // basic types: https://maplibre.org/maplibre-style-spec/types/

  // region Literals

  public fun const(string: String): Expression<String> = Expression.ofString(string)

  public fun <T : LayerPropertyEnum> const(value: T): Expression<T> =
    Expression.ofLayerPropertyEnum(value)

  public fun const(float: Float): Expression<Number> = Expression.ofFloat(float)

  public fun const(dp: Dp): Expression<Dp> = Expression.ofDp(dp)

  public fun const(bool: Boolean): Expression<Boolean> = Expression.ofBoolean(bool)

  public fun const(color: Color): Expression<Color> = Expression.ofColor(color)

  // corresponds to "point" in the style spec
  public fun const(offset: Offset): Expression<Offset> = Expression.ofOffset(offset)

  // corresponds to "point" in the style spec
  public fun const(dpOffset: DpOffset): Expression<DpOffset> = Expression.ofDpOffset(dpOffset)

  // corresponds to "padding" in the style spec
  public fun const(padding: PaddingValues.Absolute): Expression<PaddingValues.Absolute> =
    Expression.ofPadding(padding)

  @Suppress("UNCHECKED_CAST")
  public fun <T> nil(): Expression<T> = Expression.ofNull() as Expression<T>

  @Suppress("UNCHECKED_CAST")
  public val Expression<Number>.dp: Expression<Dp>
    get() = this as Expression<Dp>

  @Suppress("UNCHECKED_CAST")
  public fun Expression<Dp>.asNumber(): Expression<Number> = this as Expression<Number>

  // expressions: https://maplibre.org/maplibre-style-spec/expressions/

  // endregion

  // region Variable binding
  /**
   * Binds expressions to named variables, which can then be referenced in the result expression
   * using [useVariable].
   */
  public fun <T> withVariable(
    name: String,
    value: Expression<*>,
    expression: Expression<T>,
  ): Expression<T> = callFn("let", const(name), value, expression)

  /** References variable bound using [withVariable]. */
  public fun <T> useVariable(name: String): Expression<T> = callFn("var", const(name))

  // endregion

  // region Types

  /** Produces a literal list value. */
  public fun <T> literal(values: List<Expression<T>>): Expression<List<T>> =
    callFn("literal", Expression.ofList(values))

  /** Produces a literal map value. */
  public fun <T> literal(values: Map<String, Expression<T>>): Expression<Map<String, T>> =
    callFn("literal", Expression.ofMap(values))

  /**
   * Returns a string describing the type of this expression. Either "boolean", "string", "number",
   * "color" or "array".
   */
  public fun Expression<*>.type(): Expression<String> = callFn("typeof", this)

  /**
   * Asserts that this is a list (optionally with a specific item [type] and [length]).
   *
   * If, when the input expression is evaluated, it is not of the asserted type, then this assertion
   * will cause the whole expression to be aborted.
   */
  public fun <T> Expression<*>.asList(
    type: Expression<String>? = null,
    length: Expression<Number>? = null,
  ): Expression<List<T>> {
    val args = buildList {
      type?.let { add(const("array")) }
      length?.let { add(const("array")) }
    }
    return callFn("array", this, *args.toTypedArray())
  }

  /**
   * Asserts that this value is a string.
   *
   * In case this expression is not a string, each of the [fallbacks] is evaluated in order until a
   * string is obtained. If none of the inputs are strings, the expression is an error.
   */
  public fun Expression<*>.asString(vararg fallbacks: Expression<*>): Expression<String> =
    callFn("string", this, *fallbacks)

  /**
   * Asserts that this value is a number.
   *
   * In case this expression is not a number, each of the [fallbacks] is evaluated in order until a
   * number is obtained. If none of the inputs are numbers, the expression is an error.
   */
  public fun Expression<*>.asNumber(vararg fallbacks: Expression<*>): Expression<Number> =
    callFn("number", this, *fallbacks)

  /**
   * Asserts that this value is a boolean.
   *
   * In case this expression is not a boolean, each of the [fallbacks] is evaluated in order until a
   * boolean is obtained. If none of the inputs are booleans, the expression is an error.
   */
  public fun Expression<*>.asBoolean(vararg fallbacks: Expression<*>): Expression<Boolean> =
    callFn("boolean", this, *fallbacks)

  /**
   * Asserts that this value is a map.
   *
   * In case this expression is not a map, each of the [fallbacks] is evaluated in order until a map
   * is obtained. If none of the inputs are maps, the expression is an error.
   */
  public fun <T> Expression<*>.asMap(vararg fallbacks: Expression<*>): Expression<Map<String, T>> =
    callFn("object", this, *fallbacks)

  /**
   * Returns a collator for use in locale-dependent comparison operations. The [caseSensitive] and
   * [diacriticSensitive] options default to `false`. The [locale] argument specifies the IETF
   * language tag of the locale to use. If none is provided, the default locale is used. If the
   * requested locale is not available, the collator will use a system-defined fallback locale. Use
   * [resolvedLocale] to test the results of locale fallback behavior.
   */
  public fun collator(
    caseSensitive: Expression<Boolean>? = null,
    diacriticSensitive: Expression<Boolean>? = null,
    locale: Expression<String>? = null,
  ): Expression<TCollator> =
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
  public fun format(vararg sections: Pair<Expression<*>, FormatStyle>): Expression<TFormatted> =
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
  public fun format(value: Expression<String>): Expression<TFormatted> =
    callFn("format", value, buildOptions {})

  public data class FormatStyle(
    val textFont: Expression<String>? = null,
    val textColor: Expression<String>? = null,
    val fontScale: Expression<Number>? = null,
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
  public fun image(value: Expression<String>): Expression<TResolvedImage> = callFn("image", value)

  /**
   * Converts this number into a string representation using the provided formatting rules.
   *
   * @param locale BCP 47 language tag for which locale to use
   * @param currency an ISO 4217 code to use for currency-style formatting
   * @param minFractionDigits minimum fractional digits to include
   * @param maxFractionDigits maximum fractional digits to include
   */
  public fun Expression<Number>.numberFormat(
    locale: Expression<String>? = null,
    currency: Expression<String>? = null,
    minFractionDigits: Expression<Number>? = null,
    maxFractionDigits: Expression<Number>? = null,
  ): Expression<String> =
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
  public fun Expression<*>.convertToString(): Expression<String> = callFn("to-string", this)

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
  public fun Expression<*>.convertToNumber(vararg fallbacks: Expression<*>): Expression<Number> =
    callFn("to-number", this, *fallbacks)

  /**
   * Converts this expression to a boolean expression.
   *
   * The result is `false` when then this is an empty string, `0`, `false`,`null` or `NaN`;
   * otherwise it is `true`.
   */
  public fun Expression<*>.convertToBoolean(): Expression<Boolean> = callFn("to-boolean", this)

  /**
   * Converts this expression to a color expression.
   *
   * In case this expression cannot be converted to a color, each of the [fallbacks] is evaluated in
   * order until the first successful conversion is obtained. If none of the inputs can be
   * converted, the expression is an error.
   */
  public fun Expression<*>.convertToColor(vararg fallbacks: Expression<*>): Expression<Color> =
    callFn("to-color", this, *fallbacks)

  // endregion

  // region Lookup

  /** Returns the item at [index]. */
  @JvmName("getAtIndex")
  public operator fun <T> Expression<List<T>>.get(index: Expression<Number>): Expression<T> =
    callFn("at", index, this)

  /** Returns whether this list contains the [item]. */
  @JvmName("containsList")
  public fun Expression<List<*>>.contains(item: Expression<*>): Expression<Boolean> =
    callFn("in", item, this)

  /** Returns whether this string contains the [substring]. */
  @JvmName("containsString")
  public fun Expression<String>.contains(substring: Expression<String>): Expression<Boolean> =
    callFn("in", substring, this)

  /**
   * Returns the first index at which the [substring] is located in this string, or `-1` if it
   * cannot be found. Accepts an optional [startIndex] from where to begin the search.
   */
  @JvmName("indexOfString")
  public fun Expression<String>.indexOf(
    substring: Expression<String>,
    startIndex: Expression<Number>? = null,
  ): Expression<Number> {
    val args = buildList {
      add(substring)
      add(this@indexOf)
      startIndex?.let { add(it) }
    }
    return callFn("index-of", *args.toTypedArray())
  }

  /**
   * Returns the first index at which the [item] is located in this list, or `-1` if it cannot be
   * found. Accepts an optional [startIndex] from where to begin the search.
   */
  @JvmName("indexOfList")
  public fun Expression<List<*>>.indexOf(
    item: Expression<*>,
    startIndex: Expression<Number>? = null,
  ): Expression<Number> {
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
  public fun Expression<String>.substring(
    startIndex: Expression<Number>,
    endIndex: Expression<Number>? = null,
  ): Expression<String> {
    val args = buildList {
      add(this@substring)
      add(startIndex)
      endIndex?.let { add(it) }
    }
    return callFn("slice", *args.toTypedArray())
  }

  /**
   * Returns the items in this list from the [startIndex] (inclusive) to the end of this list if
   * [endIndex] is not specified or `null`, otherwise to [endIndex] (exclusive).
   */
  public fun <T> Expression<List<T>>.slice(
    startIndex: Expression<Number>,
    endIndex: Expression<Number>? = null,
  ): Expression<T> {
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
  public fun <T> get(key: Expression<String>): Expression<T> = callFn("get", key)

  /** Tests for the presence of a property value [key] in the current feature's properties. */
  public fun has(key: Expression<String>): Expression<Boolean> = callFn("has", key)

  /** Returns the value corresponding the given [key] or `null` if it is not present in this map. */
  public operator fun <T> Expression<Map<String, *>>.get(key: Expression<String>): Expression<T> =
    callFn("get", key, this)

  /** Returns whether the given [key] is in this map. */
  public fun Expression<Map<String, *>>.has(key: Expression<String>): Expression<Boolean> =
    callFn("has", key, this)

  /**
   * Gets the length of this string.
   *
   * A UTF-16 surrogate pair counts as a single position.
   */
  @JvmName("lengthOfString")
  public fun Expression<String>.length(): Expression<Number> = callFn("length", this)

  /** Gets the length of a this list. */
  @JvmName("lengthOfList")
  public fun Expression<List<*>>.length(): Expression<Number> = callFn("length", this)

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
   *     interpolate(linear(), zoom(), 1 to get("color1"), 20 to get("color2")),
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
  public fun <T> case(vararg branches: CaseBranch<T>, fallback: Expression<T>): Expression<T> =
    callFn(
      "case",
      *branches.foldToArgs { (test, output) ->
        add(test)
        add(output)
      },
      fallback,
    )

  public data class CaseBranch<Output>
  internal constructor(
    internal val test: Expression<Boolean>,
    internal val output: Expression<Output>,
  )

  /** Create a [CaseBranch], see [case] */
  public infix fun <Output> Expression<Boolean>.then(
    output: Expression<Output>
  ): CaseBranch<Output> = CaseBranch(this, output)

  /**
   * Selects the output from the given [branches] whose label value matches the [input] string, or
   * the [fallback] value if no match is found.
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
  @JvmName("matchStrings")
  public fun <T> match(
    input: Expression<String>,
    vararg branches: MatchBranch<String, T>,
    fallback: Expression<T>,
  ): Expression<T> =
    callFn(
      "match",
      input,
      *branches.foldToArgs { (label, output) ->
        add(label)
        add(output)
      },
      fallback,
    )

  /**
   * Selects the output from the given [branches] whose label value matches the [input] value, or
   * the [fallback] value if no match is found.
   *
   * Each label must be unique. If the input type does not match the type of the labels, the result
   * will be the [fallback] value.
   *
   * @see match
   */
  @JvmName("matchNumbers")
  public fun <T> match(
    input: Expression<Number>,
    vararg branches: MatchBranch<Number, T>,
    fallback: Expression<T>,
  ): Expression<T> =
    callFn(
      "match",
      input,
      *branches.foldToArgs { (label, output) ->
        add(label)
        add(output)
      },
      fallback,
    )

  public data class MatchBranch<Label, Output>
  internal constructor(internal val label: Expression<*>, internal val output: Expression<Output>)

  /** Create a [MatchBranch], see [match] */
  public infix fun <Output> String.then(output: Expression<Output>): MatchBranch<String, Output> =
    MatchBranch(const(this), output)

  /** Create a [MatchBranch], see [match] */
  public infix fun <Output> Number.then(output: Expression<Output>): MatchBranch<Number, Output> =
    MatchBranch(const(this.toFloat()), output)

  /** Create a [MatchBranch], see [match] */
  @JvmName("stringsThen")
  public infix fun <Output> List<String>.then(
    output: Expression<Output>
  ): MatchBranch<String, Output> = MatchBranch(Expression.ofList(this.map { const(it) }), output)

  /** Create a [MatchBranch], see [match] */
  @JvmName("numbersThen")
  public infix fun <Output> List<Number>.then(
    output: Expression<Output>
  ): MatchBranch<Number, Output> =
    MatchBranch(Expression.ofList(this.map { const(it.toFloat()) }), output)

  /**
   * Evaluates each expression in [values] in turn until the first non-null value is obtained, and
   * returns that value.
   */
  public fun <T> coalesce(vararg values: Expression<T?>): Expression<T> =
    callFn("coalesce", *values)

  /** Returns whether this expression is equal to [other]. */
  @JvmName("eqBoolean")
  public infix fun Expression<Boolean>.eq(other: Expression<Boolean>): Expression<Boolean> =
    callFn("==", this, other)

  /** Returns whether this expression is equal to [other]. */
  @JvmName("eqString")
  public infix fun Expression<String>.eq(other: Expression<String>): Expression<Boolean> =
    callFn("==", this, other)

  /** Returns whether this expression is equal to [other]. */
  @JvmName("eqNumber")
  public infix fun Expression<Number>.eq(other: Expression<Number>): Expression<Boolean> =
    callFn("==", this, other)

  /** Returns whether this expression is equal to [other]. */
  @JvmName("eqDp")
  public infix fun Expression<Dp>.eq(other: Expression<Dp>): Expression<Boolean> =
    callFn("==", this, other)

  /**
   * Returns whether the [left] string expression is equal to the [right] string expression. An
   * optional [collator] (see [ExpressionScope.collator] function) can be specified to control
   * locale-dependent string comparisons.
   */
  public fun eq(
    left: Expression<String>,
    right: Expression<String>,
    collator: Expression<TCollator>? = null,
  ): Expression<Boolean> = callFn("==", left, right, *buildArgs { collator?.let { add(it) } })

  /** Returns whether this expression is not equal to [other]. */
  @JvmName("neqBoolean")
  public infix fun Expression<Boolean>.neq(other: Expression<Boolean>): Expression<Boolean> =
    callFn("!=", this, other)

  /** Returns whether this expression is not equal to [other]. */
  @JvmName("neqString")
  public infix fun Expression<String>.neq(other: Expression<String>): Expression<Boolean> =
    callFn("!=", this, other)

  /** Returns whether this expression is not equal to [other]. */
  @JvmName("neqNumber")
  public infix fun Expression<Number>.neq(other: Expression<Number>): Expression<Boolean> =
    callFn("!=", this, other)

  /** Returns whether this expression is not equal to [other]. */
  @JvmName("neqDp")
  public infix fun Expression<Dp>.neq(other: Expression<Dp>): Expression<Boolean> =
    callFn("!=", this, other)

  /**
   * Returns whether the [left] string expression is not equal to the [right] string expression. An
   * optional [collator] (see [ExpressionScope.collator]) can be specified to control
   * locale-dependent string comparisons.
   */
  public fun neq(
    left: Expression<String>,
    right: Expression<String>,
    collator: Expression<TCollator>? = null,
  ): Expression<Boolean> = callFn("!=", left, right, *buildArgs { collator?.let { add(it) } })

  /** Returns whether this number expression is strictly greater than [other]. */
  @JvmName("gtNumber")
  public infix fun Expression<Number>.gt(other: Expression<Number>): Expression<Boolean> =
    callFn(">", this, other)

  /** Returns whether this dp expression is strictly greater than [other]. */
  @JvmName("gtDp")
  public infix fun Expression<Dp>.gt(other: Expression<Dp>): Expression<Boolean> =
    callFn(">", this, other)

  /**
   * Returns whether this string expression is strictly greater than [other].
   *
   * Strings are compared lexicographically (`"b" > "a"`).
   */
  @JvmName("gtString")
  public infix fun Expression<String>.gt(other: Expression<String>): Expression<Boolean> =
    callFn(">", this, other)

  /**
   * Returns whether the [left] string expression is strictly greater than the [right] string
   * expression. An optional [collator] (see [ExpressionScope.collator]) can be specified to control
   * locale-dependent string comparisons.
   *
   * Strings are compared lexicographically (`"b" > "a"`).
   */
  public fun gt(
    left: Expression<String>,
    right: Expression<String>,
    collator: Expression<TCollator>? = null,
  ): Expression<Boolean> = callFn(">", left, right, *buildArgs { collator?.let { add(it) } })

  /** Returns whether this number expression is strictly less than [other]. */
  @JvmName("ltNumber")
  public infix fun Expression<Number>.lt(other: Expression<Number>): Expression<Boolean> =
    callFn("<", this, other)

  /** Returns whether this dp expression is strictly less than [other]. */
  @JvmName("ltDp")
  public infix fun Expression<Dp>.lt(other: Expression<Dp>): Expression<Boolean> =
    callFn("<", this, other)

  /**
   * Returns whether this string expression is strictly less than [other].
   *
   * Strings are compared lexicographically (`"a" < "b"`).
   */
  @JvmName("ltString")
  public infix fun Expression<String>.lt(other: Expression<String>): Expression<Boolean> =
    callFn("<", this, other)

  /**
   * Returns whether the [left] string expression is strictly less than the [right] string
   * expression. An optional [collator] (see [ExpressionScope.collator]) can be specified to control
   * locale-dependent string comparisons.
   *
   * Strings are compared lexicographically (`"a" < "b"`).
   */
  public fun lt(
    left: Expression<String>,
    right: Expression<String>,
    collator: Expression<TCollator>? = null,
  ): Expression<Boolean> = callFn("<", left, right, *buildArgs { collator?.let { add(it) } })

  /** Returns whether this number expression is greater than or equal to [other]. */
  @JvmName("gteNumber")
  public infix fun Expression<Number>.gte(other: Expression<Number>): Expression<Boolean> =
    callFn(">=", this, other)

  /** Returns whether this dp expression is greater than or equal to [other]. */
  @JvmName("gteDp")
  public infix fun Expression<Dp>.gte(other: Expression<Dp>): Expression<Boolean> =
    callFn(">=", this, other)

  /**
   * Returns whether this string expression is greater than or equal to [other].
   *
   * Strings are compared lexicographically (`"b" >= "a"`).
   */
  @JvmName("gteString")
  public infix fun Expression<String>.gte(other: Expression<String>): Expression<Boolean> =
    callFn(">=", this, other)

  /**
   * Returns whether the [left] string expression is greater than or equal to the [right] string
   * expression. An optional [collator] (see [ExpressionScope.collator]) can be specified to control
   * locale-dependent string comparisons.
   *
   * Strings are compared lexicographically (`"b" >= "a"`).
   */
  public fun gte(
    left: Expression<String>,
    right: Expression<String>,
    collator: Expression<TCollator>? = null,
  ): Expression<Boolean> = callFn(">=", left, right, *buildArgs { collator?.let { add(it) } })

  /** Returns whether this number expression is less than or equal to [other]. */
  @JvmName("lteNumber")
  public infix fun Expression<Number>.lte(other: Expression<Number>): Expression<Boolean> =
    callFn("<=", this, other)

  /** Returns whether this dp expression is less than or equal to [other]. */
  @JvmName("lteDp")
  public infix fun Expression<Dp>.lte(other: Expression<Dp>): Expression<Boolean> =
    callFn("<=", this, other)

  /**
   * Returns whether this string expression is less than or equal to [other].
   *
   * Strings are compared lexicographically (`"a" <= "b"`).
   */
  @JvmName("lteString")
  public infix fun Expression<String>.lte(other: Expression<String>): Expression<Boolean> =
    callFn("<=", this, other)

  /**
   * Returns whether the [left] string expression is less than or equal to the [right] string
   * expression. An optional [collator] (see [ExpressionScope.collator]) can be specified to control
   * locale-dependent string comparisons.
   *
   * Strings are compared lexicographically (`"a" < "b"`).
   */
  public fun lte(
    left: Expression<String>,
    right: Expression<String>,
    collator: Expression<TCollator>? = null,
  ): Expression<Boolean> = callFn("<=", left, right, *buildArgs { collator?.let { add(it) } })

  /** Returns whether all [expressions] are `true`. */
  public fun all(vararg expressions: Expression<Boolean>): Expression<Boolean> =
    callFn("all", *expressions)

  /** Returns whether both this and [other] expressions are `true`. */
  public infix fun Expression<Boolean>.and(other: Expression<Boolean>): Expression<Boolean> =
    all(this, other)

  /** Returns whether any [expressions] are `true`. */
  public fun any(vararg expressions: Expression<Boolean>): Expression<Boolean> =
    callFn("any", *expressions)

  /** Returns whether any of this or the [other] expressions are `true`. */
  public infix fun Expression<Boolean>.or(other: Expression<Boolean>): Expression<Boolean> =
    any(this, other)

  /** Negates this expression. */
  @JvmName("notOperator")
  public operator fun Expression<Boolean>.not(): Expression<Boolean> = callFn("!", this)

  /**
   * Returns whether the evaluated feature is fully contained inside the boundary of the given
   * [polygon].
   */
  @JvmName("withinPolygon")
  public fun within(polygon: Expression<Polygon>): Expression<Boolean> = callFn("within", polygon)

  /**
   * Returns whether the evaluated feature is fully contained inside the boundary of the given
   * [polygon].
   */
  @JvmName("withinMultiPolygon")
  public fun within(polygon: Expression<MultiPolygon>): Expression<Boolean> =
    callFn("within", polygon)

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
  public fun <Output> step(
    input: Expression<Number>,
    fallback: Expression<Output>,
    vararg stops: Pair<Number, Expression<Output>>,
  ): Expression<Output> =
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

  private fun <Output> interpolateImpl(
    name: String,
    type: Expression<TInterpolationType>,
    input: Expression<Number>,
    vararg stops: Pair<Number, Expression<Output>>,
  ): Expression<Output> =
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
  public fun <Output> interpolate(
    type: Expression<TInterpolationType>,
    input: Expression<Number>,
    vararg stops: Pair<Number, Expression<Output>>,
  ): Expression<Output> = interpolateImpl("interpolate", type, input, *stops)

  /**
   * Produces continuous, smooth results by interpolating between pairs of input and output values
   * ([stops]), given the [input] value. Works like [interpolate], but the interpolation is
   * performed in the
   * [Hue-Chroma-Luminance color space](https://en.wikipedia.org/wiki/HCL_color_space).
   *
   * Example:
   * ```
   * interpolateHcl(
   *   linear(), zoom(),
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
    type: Expression<TInterpolationType>,
    input: Expression<Number>,
    vararg stops: Pair<Number, Expression<Color>>,
  ): Expression<Color> = interpolateImpl("interpolate-hcl", type, input, *stops)

  /**
   * Produces continuous, smooth results by interpolating between pairs of input and output values
   * ([stops]), given the [input] value. Works like [interpolate], but the interpolation is
   * performed in the [CIELAB color space](https://en.wikipedia.org/wiki/CIELAB_color_space).
   */
  public fun interpolateLab(
    type: Expression<TInterpolationType>,
    input: Expression<Number>,
    vararg stops: Pair<Number, Expression<Color>>,
  ): Expression<Color> = interpolateImpl("interpolate-lab", type, input, *stops)

  /**
   * Interpolates exponentially between the stops. [base] controls the rate at which the output
   * increases: higher values make the output increase more towards the high end of the range. With
   * values close to 1 the output increases linearly.
   */
  public fun exponential(base: Expression<Number>): Expression<TInterpolationType> =
    callFn("exponential", base)

  /** Interpolates linearly between the pairs of stops. */
  public fun linear(): Expression<TInterpolationType> = callFn("linear")

  /**
   * Interpolates using the cubic bezier curve defined by the given control points between the pairs
   * of stops.
   */
  public fun cubicBezier(
    x1: Expression<Number>,
    y1: Expression<Number>,
    x2: Expression<Number>,
    y2: Expression<Number>,
  ): Expression<TInterpolationType> = callFn("cubic-bezier", x1, y1, x2, y2)

  // endregion

  // region Math

  /** Returns mathematical constant ln(2) = natural logarithm of 2. */
  public fun ln2(): Expression<Number> = callFn("ln2")

  /** Returns the mathematical constant π */
  public fun pi(): Expression<Number> = callFn("pi")

  /** Returns the mathematical constant e */
  public fun e(): Expression<Number> = callFn("e")

  /** Returns the sum of this number expression with [other]. */
  @JvmName("plusNumber")
  public operator fun Expression<Number>.plus(other: Expression<Number>): Expression<Number> =
    callFn("+", this, other)

  /** Returns the sum of this dp expression with [other]. */
  @JvmName("plusDp")
  public operator fun Expression<Dp>.plus(other: Expression<Dp>): Expression<Dp> =
    callFn("+", this, other)

  /** Returns the product of this number expression with [other]. */
  public operator fun Expression<Number>.times(other: Expression<Number>): Expression<Number> =
    callFn("*", this, other)

  /** Returns the product of this dp expression with [other]. */
  @JvmName("timesDp")
  public operator fun Expression<Dp>.times(other: Expression<Dp>): Expression<Dp> =
    callFn("*", this, other)

  /** Returns the result of subtracting [other] from this number expression. */
  public operator fun Expression<Number>.minus(other: Expression<Number>): Expression<Number> =
    callFn("-", this, other)

  /** Returns the result of subtracting [other] from this dp expression. */
  @JvmName("minusDp")
  public operator fun Expression<Dp>.minus(other: Expression<Dp>): Expression<Dp> =
    callFn("-", this, other)

  /** Negates this number expression. */
  public operator fun Expression<Number>.unaryMinus(): Expression<Number> = callFn("-", this)

  /** Negates this dp expression. */
  @JvmName("unaryMinusDp")
  public operator fun Expression<Dp>.unaryMinus(): Expression<Dp> = callFn("-", this)

  /** Returns the result of floating point division of this number expression by [divisor]. */
  public operator fun Expression<Number>.div(divisor: Expression<Number>): Expression<Number> =
    callFn("/", this, divisor)

  /** Returns the result of floating point division of this dp expression by [divisor]. */
  @JvmName("divDp")
  public operator fun Expression<Dp>.div(divisor: Expression<Dp>): Expression<Dp> =
    callFn("/", this, divisor)

  /** Returns the remainder after integer division of this number expression by [divisor]. */
  public operator fun Expression<Number>.rem(divisor: Expression<Number>): Expression<Number> =
    callFn("%", this, divisor)

  /** Returns the remainder after dp division of this number expression by [divisor]. */
  @JvmName("remDp")
  public operator fun Expression<Dp>.rem(divisor: Expression<Dp>): Expression<Dp> =
    callFn("%", this, divisor)

  /** Returns the result of raising this number expression to the power of [exponent]. */
  @JvmName("powNumber")
  public fun Expression<Number>.pow(exponent: Expression<Number>): Expression<Number> =
    callFn("^", this, exponent)

  /** Returns the result of raising this dp expression to the power of [exponent]. */
  @JvmName("powDp")
  public fun Expression<Dp>.pow(exponent: Expression<Dp>): Expression<Number> =
    callFn("^", this, exponent)

  /** Returns the square root of [value]. */
  public fun sqrt(value: Expression<Number>): Expression<Number> = callFn("sqrt", value)

  /** Returns the base-ten logarithm of [value]. */
  public fun log10(value: Expression<Number>): Expression<Number> = callFn("log10", value)

  /** Returns the natural logarithm of [value]. */
  public fun ln(value: Expression<Number>): Expression<Number> = callFn("ln", value)

  /** Returns the base-two logarithm of [value]. */
  public fun log2(value: Expression<Number>): Expression<Number> = callFn("log2", value)

  /** Returns the sine of [value]. */
  public fun sin(value: Expression<Number>): Expression<Number> = callFn("sin", value)

  /** Returns the cosine of [value]. */
  public fun cos(value: Expression<Number>): Expression<Number> = callFn("cos", value)

  /** Returns the tangent of [value]. */
  public fun tan(value: Expression<Number>): Expression<Number> = callFn("tan", value)

  /** Returns the arcsine of [value]. */
  public fun asin(value: Expression<Number>): Expression<Number> = callFn("asin", value)

  /** Returns the arccosine of [value]. */
  public fun acos(value: Expression<Number>): Expression<Number> = callFn("acos", value)

  /** Returns the arctangent of [value]. */
  public fun atan(value: Expression<Number>): Expression<Number> = callFn("atan", value)

  /** Returns the smallest of all given [numbers]. */
  public fun min(vararg numbers: Expression<Number>): Expression<Number> = callFn("min", *numbers)

  /** Returns the smallest of all given dp [numbers]. */
  @JvmName("minDp")
  public fun min(vararg numbers: Expression<Dp>): Expression<Dp> = callFn("min", *numbers)

  /** Returns the greatest of all given [numbers]. */
  public fun max(vararg numbers: Expression<Number>): Expression<Number> = callFn("max", *numbers)

  /** Returns the greatest of all given dp [numbers]. */
  @JvmName("maxDp")
  public fun max(vararg numbers: Expression<Dp>): Expression<Dp> = callFn("max", *numbers)

  /**
   * Rounds [value] to the nearest integer. Halfway values are rounded away from zero.
   *
   * For example `round(const(-1.5))` evaluates to `-2`.
   */
  public fun round(value: Expression<Number>): Expression<Number> = callFn("round", value)

  /**
   * Rounds [value] to the nearest integer. Halfway values are rounded away from zero.
   *
   * For example `round(const(-1.5.dp))` evaluates to `-2.dp`.
   */
  @JvmName("roundDp")
  public fun round(value: Expression<Dp>): Expression<Dp> = callFn("round", value)

  /** Returns the absolute value of [value], i.e. always a positive value. */
  public fun abs(value: Expression<Number>): Expression<Number> = callFn("abs", value)

  /** Returns the absolute dp value of [value], i.e. always a positive dp value. */
  @JvmName("absDp") public fun abs(value: Expression<Dp>): Expression<Dp> = callFn("abs", value)

  /** Returns the smallest integer that is greater than or equal to [value]. */
  public fun ceil(value: Expression<Number>): Expression<Number> = callFn("ceil", value)

  /** Returns the smallest integer that is greater than or equal to [value]. */
  @JvmName("ceilDp") public fun ceil(value: Expression<Dp>): Expression<Dp> = callFn("ceil", value)

  /** Returns the largest integer that is less than or equal to [value]. */
  public fun floor(value: Expression<Number>): Expression<Number> = callFn("floor", value)

  /** Returns the largest integer that is less than or equal to [value]. */
  @JvmName("floorDp")
  public fun floor(value: Expression<Dp>): Expression<Dp> = callFn("floor", value)

  /** Returns the shortest distance in meters between the evaluated feature and [geometry]. */
  public fun distance(geometry: Expression<Geometry>): Expression<Number> =
    callFn("distance", geometry)

  // endregion

  // region Color

  /**
   * Returns a four-element list, containing the color's red, green, blue, and alpha components, in
   * that order.
   */
  public fun Expression<Color>.toRgbaComponents(): Expression<List<Number>> =
    callFn("to-rgba", this)

  /**
   * Creates a color value from [red], [green], and [blue] components, which must range between 0
   * and 255, and optionally an [alpha] component which must range between 0 and 1.
   *
   * If any component is out of range, the expression is an error.
   */
  public fun rgbColor(
    red: Expression<Number>,
    green: Expression<Number>,
    blue: Expression<Number>,
    alpha: Expression<Number>? = null,
  ): Expression<Color> =
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
  public fun <T> properties(): Expression<Map<String, T>> = callFn("properties")

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
  public fun <T> featureState(key: Expression<String>): Expression<T> = callFn("feature-state", key)

  /**
   * Gets the feature's geometry type as a string: "Point", "MultiPoint", "LineString",
   * "MultiLineString", "Polygon" or "MultiPolygon".
   */
  public fun geometryType(): Expression<String> = callFn("geometry-type")

  /** Gets the feature's id, if it has one. */
  public fun <T> id(): Expression<T> = callFn("id")

  /**
   * Gets the progress along a gradient line. Can only be used in the `gradient` property of a line
   * layer, see [LineLayer][dev.sargunv.maplibrecompose.compose.layer.LineLayer].
   */
  public fun lineProgress(value: Expression<Number>): Expression<Number> =
    callFn("line-progress", value)

  /**
   * Gets the value of a cluster property accumulated so far. Can only be used in the
   * `clusterProperties` option of a clustered GeoJSON source, see
   * [GeoJsonOptions][dev.sargunv.maplibrecompose.core.source.GeoJsonOptions].
   */
  public fun <T> accumulated(key: Expression<String>): Expression<T> = callFn("accumulated", key)

  // endregion

  // region Zoom

  /**
   * Gets the current zoom level. Note that in layer style properties, [zoom] may only appear as the
   * input to a top-level [step] or [interpolate] (, [interpolateHcl], [interpolateLab], ...)
   * expression.
   */
  public fun zoom(): Expression<Number> = callFn("zoom")

  // endregion

  // region Heatmap

  /**
   * Gets the kernel density estimation of a pixel in a heatmap layer, which is a relative measure
   * of how many data points are crowded around a particular pixel. Can only be used in the
   * expression for the `color` parameter in a
   * [HeatmapLayer][dev.sargunv.maplibrecompose.compose.layer.HeatmapLayer].
   */
  public fun heatmapDensity(): Expression<Number> = callFn("heatmap-density")

  // endregion

  // region String

  /**
   * Returns `true` if this string is expected to render legibly. Returns `false` if this string
   * contains sections that cannot be rendered without potential loss of meaning (e.g. Indic scripts
   * that require complex text shaping).
   */
  public fun Expression<String>.isScriptSupported(): Expression<Boolean> =
    callFn("is-supported-script", this)

  /**
   * Returns this string converted to uppercase. Follows the Unicode Default Case Conversion
   * algorithm and the locale-insensitive case mappings in the Unicode Character Database.
   */
  public fun Expression<String>.uppercase(): Expression<String> = callFn("upcase", this)

  /**
   * Returns this string converted to lowercase. Follows the Unicode Default Case Conversion
   * algorithm and the locale-insensitive case mappings in the Unicode Character Database.
   */
  public fun Expression<String>.lowercase(): Expression<String> = callFn("downcase", this)

  /** Concatenates this string expression with [other]. */
  @JvmName("plusString")
  public operator fun Expression<String>.plus(other: Expression<String>): Expression<String> =
    callFn("concat", this, other)

  /**
   * Returns the IETF language tag of the locale being used by the provided [collator]. This can be
   * used to determine the default system locale, or to determine if a requested locale was
   * successfully loaded.
   */
  public fun resolvedLocale(collator: Expression<TCollator>): Expression<String> =
    callFn("resolved-locale", collator)

  // endregion

  // region Utils

  @Suppress("UNCHECKED_CAST")
  private fun <Return> callFn(function: String, vararg args: Expression<*>) =
    Expression.ofList(listOf(const(function), *args)) as Expression<Return>

  private inline fun buildOptions(block: MutableMap<String, Expression<*>>.() -> Unit) =
    Expression.ofMap(mutableMapOf<String, Expression<*>>().apply(block))

  private fun buildArgs(block: MutableList<Expression<*>>.() -> Unit) =
    mutableListOf<Expression<*>>().apply(block).toTypedArray()

  private fun <T> Array<T>.foldToArgs(block: MutableList<Expression<*>>.(element: T) -> Unit) =
    fold(mutableListOf<Expression<*>>()) { acc, element -> acc.apply { block(element) } }
      .toTypedArray()

  private fun <T> List<T>.foldToArgs(block: MutableList<Expression<*>>.(element: T) -> Unit) =
    fold(mutableListOf<Expression<*>>()) { acc, element -> acc.apply { block(element) } }
      .toTypedArray()

  // endregion
}
