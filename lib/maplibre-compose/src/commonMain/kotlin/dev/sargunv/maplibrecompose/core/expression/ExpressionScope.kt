package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import dev.sargunv.maplibrecompose.core.util.JsOnlyApi
import kotlin.enums.enumEntries
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName
import kotlin.time.Duration

public object ExpressionScope {
  // region Literals

  /** Creates a literal expression for a [String] value. */
  public fun const(string: String): Expression<StringValue> = Expression.ofString(string)

  /** Creates a literal expression for an enum value implementing [EnumValue]. */
  public fun <T : EnumValue<T>> const(value: T): Expression<T> = value.stringConst.cast()

  /** Creates a literal expression for a dimensionless [Float] value. */
  public fun const(float: Float): Expression<FloatValue> = Expression.ofFloat(float)

  /** Creates a literal expression for an dimensionless [Int] value. */
  public fun const(int: Int): Expression<IntValue> = Expression.ofInt(int)

  /** Creates a literal expression for a [Dp] value. */
  public fun const(dp: Dp): Expression<DpValue> = Expression.ofFloat(dp.value).cast()

  /**
   * Creates a literal expression for a [Duration] value.
   *
   * The duration will be rounded to the nearest whole milliseconds.
   */
  public fun const(duration: Duration): Expression<DurationValue> =
    Expression.ofInt(duration.inWholeMilliseconds.toInt()).cast()

  /** Creates a literal expression for a [Boolean] value. */
  public fun const(bool: Boolean): Expression<BooleanValue> =
    if (bool) Expression.ofTrue else Expression.ofFalse

  /** Creates a literal expression for a [Color] value. */
  public fun const(color: Color): Expression<ColorValue> = Expression.ofColor(color)

  /** Creates a literal expression for an [Offset] value. */
  public fun const(offset: Offset): Expression<OffsetValue> = Expression.ofOffset(offset)

  /** Creates a literal expression for a [DpOffset] value. */
  public fun const(dpOffset: DpOffset): Expression<DpOffsetValue> =
    Expression.ofOffset(Offset(dpOffset.x.value, dpOffset.y.value)).cast()

  /** Creates a literal expression for a [PaddingValues.Absolute] value. */
  public fun const(padding: PaddingValues.Absolute): Expression<PaddingValue> =
    Expression.ofPadding(padding)

  /**
   * Creates a literal expression for a `null` value.
   *
   * For simplicity, the expression type system does not encode nullability, so the return value of
   * this function is assignable to any kind of expression.
   */
  public fun <T : ExpressionValue> nil(): Expression<T> = Expression.ofNull.cast()

  // endregion

  // region Conversion

  /** Converts a numeric [Expression] to a [DpValue] expression. */
  public val Expression<FloatValue>.dp: Expression<DpValue>
    get() = this.cast()

  /** Converts a numeric [Expression] in milliseconds to a [DurationValue] expression. */
  public val Expression<FloatValue>.milliseconds: Expression<DurationValue>
    get() = this.cast()

  /** Converts a numeric [Expression] in seconds to a [DurationValue] expression. */
  public val Expression<FloatValue>.seconds: Expression<DurationValue>
    get() = (this * const(1000)).cast()

  // endregion

  // region Variable binding

  @JvmInline
  public value class Variable<@Suppress("unused") T : ExpressionValue>(public val name: String)

  /** Declares a named variable for use in [bind] and [use]. */
  public fun <T : ExpressionValue> declare(name: String): Variable<T> = Variable(name)

  /**
   * Binds expressions to variables declared by [declare], which can then be referenced with [use]
   * in the [expression].
   */
  public fun <T : ExpressionValue> Variable<T>.bind(
    value: Expression<T>,
    expression: Expression<T>,
  ): Expression<T> = callFn("let", const(name), value, expression).cast()

  /** References variable bound using [bind]. */
  public fun <T : ExpressionValue> Variable<T>.use(): Expression<T> =
    callFn("var", const(name)).cast()

  // endregion

  // region Types

  /** Produces a literal list value. */
  public fun <T : ExpressionValue> literal(values: List<Expression<T>>): Expression<ListValue<T>> =
    callFn("literal", Expression.ofList(values)).cast()

  /** Produces a literal map value. */
  public fun <T : ExpressionValue> literal(
    values: Map<String, Expression<T>>
  ): Expression<MapValue<T>> = callFn("literal", Expression.ofMap(values)).cast()

  /** Returns a string describing the type of this expression. */
  public fun Expression<*>.type(): Expression<ExpressionType> = callFn("typeof", this).cast()

  /**
   * Asserts that this is a list (optionally with a specific item [type] and [length]).
   *
   * If, when the input expression is evaluated, it is not of the asserted type, then this assertion
   * will cause the whole expression to be aborted.
   */
  public fun Expression<*>.asList(
    type: Expression<ExpressionType> = nil(),
    length: Expression<IntValue> = nil(),
  ): Expression<ListValue<*>> = callFn("array", this, type, length).cast()

  /**
   * Asserts that this is a list of numbers, optionally with a specific [length].
   *
   * If, when the input expression is evaluated, it is not of the asserted type, then this assertion
   * will cause the whole expression to be aborted.
   */
  public fun <U, V : ScalarValue<U>> Expression<*>.asVector(
    length: Expression<IntValue> = nil()
  ): Expression<V> = asList(const(ExpressionType.Number), length).cast()

  /**
   * Asserts that this is a list of numbers of length 2.
   *
   * If, when the input expression is evaluated, it is not of the asserted type, then this assertion
   * will cause the whole expression to be aborted.
   */
  public fun Expression<*>.asOffset(): Expression<OffsetValue> =
    asList(const(ExpressionType.Number), const(2)).cast()

  /**
   * Asserts that this is a list of numbers of length 2.
   *
   * If, when the input expression is evaluated, it is not of the asserted type, then this assertion
   * will cause the whole expression to be aborted.
   */
  public fun Expression<*>.asDpOffset(): Expression<DpOffsetValue> =
    asList(const(ExpressionType.Number), const(2)).cast()

  /**
   * Asserts that this is a list of numbers of length 4.
   *
   * If, when the input expression is evaluated, it is not of the asserted type, then this assertion
   * will cause the whole expression to be aborted.
   */
  public fun Expression<*>.asPadding(): Expression<PaddingValue> =
    asList(const(ExpressionType.Number), const(2)).cast()

  /**
   * Asserts that this value is a string.
   *
   * In case this expression is not a string, each of the [fallbacks] is evaluated in order until a
   * string is obtained. If none of the inputs are strings, the expression is an error.
   */
  public fun Expression<*>.asString(vararg fallbacks: Expression<*>): Expression<StringValue> =
    callFn("string", this, *fallbacks).cast()

  /**
   * Asserts that this value is an entry of the enum specified by [T].
   *
   * In case this expression is not an entry of the enum, each of the [fallbacks] is evaluated in
   * order until a match is obtained. If none of the inputs match, the expression is an error.
   */
  public inline fun <reified T> Expression<*>.asEnum(
    vararg fallbacks: Expression<*>
  ): Expression<T> where T : Enum<T>, T : EnumValue<T> {
    val entries = literal(enumEntries<T>().map { const(it) })
    return switch(
        condition(entries.contains(this), this),
        *fallbacks.map { condition(entries.contains(it), it) }.toTypedArray(),
        fallback = nil(), // should always error .asString(), which is what we want as per kdoc
      )
      .asString()
      .cast()
  }

  /**
   * Asserts that this value is a number.
   *
   * In case this expression is not a number, each of the [fallbacks] is evaluated in order until a
   * number is obtained. If none of the inputs are numbers, the expression is an error.
   */
  public fun Expression<*>.asNumber(vararg fallbacks: Expression<*>): Expression<FloatValue> =
    callFn("number", this, *fallbacks).cast()

  /**
   * Asserts that this value is a boolean.
   *
   * In case this expression is not a boolean, each of the [fallbacks] is evaluated in order until a
   * boolean is obtained. If none of the inputs are booleans, the expression is an error.
   */
  public fun Expression<*>.asBoolean(vararg fallbacks: Expression<*>): Expression<BooleanValue> =
    callFn("boolean", this, *fallbacks).cast()

  /**
   * Asserts that this value is a map.
   *
   * In case this expression is not a map, each of the [fallbacks] is evaluated in order until a map
   * is obtained. If none of the inputs are maps, the expression is an error.
   */
  public fun Expression<*>.asMap(vararg fallbacks: Expression<*>): Expression<MapValue<*>> =
    callFn("object", this, *fallbacks).cast()

  /**
   * Returns a collator for use in locale-dependent comparison operations. The [caseSensitive] and
   * [diacriticSensitive] options default to `false`. The [locale] argument specifies the IETF
   * language tag of the locale to use. If none is provided, the default locale is used. If the
   * requested locale is not available, the collator will use a system-defined fallback locale. Use
   * [resolvedLocale] to test the results of locale fallback behavior.
   */
  public fun collator(
    caseSensitive: Expression<BooleanValue>? = null,
    diacriticSensitive: Expression<BooleanValue>? = null,
    locale: Expression<StringValue>? = null,
  ): Expression<CollatorValue> =
    callFn(
        "collator",
        buildOptions {
          caseSensitive?.let { put("case-sensitive", it) }
          diacriticSensitive?.let { put("diacritic-sensitive", it) }
          locale?.let { put("locale", it) }
        },
      )
      .cast()

  /**
   * Returns a formatted string for displaying mixed-format text in the `textField` property (see
   * [SymbolLayer][dev.sargunv.maplibrecompose.compose.layer.SymbolLayer]). The input may contain a
   * string literal or expression, including an 'image' expression.
   *
   * Example:
   * ```
   * format(
   *   feature.get("name").asString().substring(const(0), const(1)).uppercase()
   *     to FormatStyle(textScale = const(1.5)),
   *   feature.get("name").asString().substring(const(1))
   *     to FormatStyle(),
   * )
   * ```
   *
   * Capitalizes the first letter of the features' property "name" and formats it to be extra-large,
   * the rest of the name is written normally.
   */
  public fun format(
    vararg sections: Pair<Expression<StringValue>, FormatStyle>
  ): Expression<FormattedValue> =
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
      .cast()

  /** Use a string as a formatted value without any extra formatting */
  public fun format(value: Expression<StringValue>): Expression<FormattedValue> =
    callFn("format", value, buildOptions {}).cast()

  public data class FormatStyle(
    val textFont: Expression<StringValue>? = null,
    val textColor: Expression<StringValue>? = null,
    val fontScale: Expression<FloatValue>? = null,
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
  public fun image(value: Expression<StringValue>): Expression<ImageValue> =
    callFn("image", value).cast()

  /**
   * Converts this number into a string representation using the provided formatting rules.
   *
   * @param locale BCP 47 language tag for which locale to use
   * @param currency an ISO 4217 code to use for currency-style formatting
   * @param minFractionDigits minimum fractional digits to include
   * @param maxFractionDigits maximum fractional digits to include
   */
  public fun Expression<ScalarValue<*>>.formatToString(
    locale: Expression<StringValue>? = null,
    currency: Expression<StringValue>? = null,
    minFractionDigits: Expression<IntValue>? = null,
    maxFractionDigits: Expression<IntValue>? = null,
  ): Expression<StringValue> =
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
      .cast()

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
  public fun Expression<*>.convertToString(): Expression<StringValue> =
    callFn("to-string", this).cast()

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
  public fun Expression<*>.convertToNumber(
    vararg fallbacks: Expression<*>
  ): Expression<FloatValue> = callFn("to-number", this, *fallbacks).cast()

  /**
   * Converts this expression to a boolean expression.
   *
   * The result is `false` when then this is an empty string, `0`, `false`,`null` or `NaN`;
   * otherwise it is `true`.
   */
  public fun Expression<*>.convertToBoolean(): Expression<BooleanValue> =
    callFn("to-boolean", this).cast()

  /**
   * Converts this expression to a color expression.
   *
   * In case this expression cannot be converted to a color, each of the [fallbacks] is evaluated in
   * order until the first successful conversion is obtained. If none of the inputs can be
   * converted, the expression is an error.
   */
  public fun Expression<*>.convertToColor(vararg fallbacks: Expression<*>): Expression<ColorValue> =
    callFn("to-color", this, *fallbacks).cast()

  // endregion

  // region List

  /** Returns the item at [index]. */
  @JvmName("getAt")
  public operator fun <T : ExpressionValue> Expression<ListValue<T>>.get(
    index: Expression<IntValue>
  ): Expression<T> = callFn("at", index, this).cast()

  /** Returns whether this list contains the [item]. */
  @JvmName("containsList")
  public fun <T : ExpressionValue> Expression<ListValue<T>>.contains(
    item: Expression<T>
  ): Expression<BooleanValue> = callFn("in", item, this).cast()

  /**
   * Returns the first index at which the [item] is located in this list, or `-1` if it cannot be
   * found. Accepts an optional [startIndex] from where to begin the search.
   */
  @JvmName("indexOfList")
  public fun <T : ExpressionValue> Expression<ListValue<T>>.indexOf(
    item: Expression<T>,
    startIndex: Expression<IntValue>? = null,
  ): Expression<IntValue> {
    val args = buildList {
      add(item)
      add(this@indexOf)
      startIndex?.let { add(it) }
    }
    return callFn("index-of", *args.toTypedArray()).cast()
  }

  /**
   * Returns the items in this list from the [startIndex] (inclusive) to the end of this list if
   * [endIndex] is not specified or `null`, otherwise to [endIndex] (exclusive).
   */
  public fun <T : ExpressionValue> Expression<ListValue<T>>.slice(
    startIndex: Expression<IntValue>,
    endIndex: Expression<IntValue>? = null,
  ): Expression<ListValue<T>> {
    val args = buildList {
      add(this@slice)
      add(startIndex)
      endIndex?.let { add(it) }
    }
    return callFn("slice", *args.toTypedArray()).cast()
  }

  /** Gets the length of a this list. */
  @JvmName("lengthOfList")
  public fun Expression<ListValue<*>>.length(): Expression<IntValue> = callFn("length", this).cast()

  // endregion

  // region Map

  /** Returns the value corresponding the given [key] or `null` if it is not present in this map. */
  public operator fun <T : ExpressionValue> Expression<MapValue<T>>.get(
    key: Expression<StringValue>
  ): Expression<T> = callFn("get", key, this).cast()

  /** Returns whether the given [key] is in this map. */
  public fun Expression<MapValue<*>>.has(key: Expression<StringValue>): Expression<BooleanValue> =
    callFn("has", key, this).cast()

  // endregion

  // region String

  /** Returns whether this string contains the [substring]. */
  @JvmName("containsString")
  public fun Expression<StringValue>.contains(
    substring: Expression<StringValue>
  ): Expression<BooleanValue> = callFn("in", substring, this).cast()

  /**
   * Returns the first index at which the [substring] is located in this string, or `-1` if it
   * cannot be found. Accepts an optional [startIndex] from where to begin the search.
   */
  @JvmName("indexOfString")
  public fun Expression<StringValue>.indexOf(
    substring: Expression<StringValue>,
    startIndex: Expression<IntValue>? = null,
  ): Expression<IntValue> {
    val args = buildList {
      add(substring)
      add(this@indexOf)
      startIndex?.let { add(it) }
    }
    return callFn("index-of", *args.toTypedArray<Expression<*>>()).cast()
  }

  /**
   * Returns a substring from this string from the [startIndex] (inclusive) to the end of the string
   * if [endIndex] is not specified or `null`, otherwise to [endIndex] (exclusive).
   *
   * A UTF-16 surrogate pair counts as a single position.
   */
  public fun Expression<StringValue>.substring(
    startIndex: Expression<IntValue>,
    endIndex: Expression<IntValue>? = null,
  ): Expression<StringValue> {
    val args = buildList {
      add(this@substring)
      add(startIndex)
      endIndex?.let { add(it) }
    }
    return callFn("slice", *args.toTypedArray<Expression<*>>()).cast()
  }

  /**
   * Gets the length of this string.
   *
   * A UTF-16 surrogate pair counts as a single position.
   */
  @JvmName("lengthOfString")
  public fun Expression<StringValue>.length(): Expression<IntValue> = callFn("length", this).cast()

  /**
   * Returns `true` if this string is expected to render legibly. Returns `false` if this string
   * contains sections that cannot be rendered without potential loss of meaning (e.g. Indic scripts
   * that require complex text shaping).
   */
  public fun Expression<StringValue>.isScriptSupported(): Expression<BooleanValue> =
    callFn("is-supported-script", this).cast()

  /**
   * Returns this string converted to uppercase. Follows the Unicode Default Case Conversion
   * algorithm and the locale-insensitive case mappings in the Unicode Character Database.
   */
  public fun Expression<StringValue>.uppercase(): Expression<StringValue> =
    callFn("upcase", this).cast()

  /**
   * Returns this string converted to lowercase. Follows the Unicode Default Case Conversion
   * algorithm and the locale-insensitive case mappings in the Unicode Character Database.
   */
  public fun Expression<StringValue>.lowercase(): Expression<StringValue> =
    callFn("downcase", this).cast()

  /** Concatenates this string expression with [other]. */
  @JvmName("concat")
  public operator fun Expression<StringValue>.plus(
    other: Expression<StringValue>
  ): Expression<StringValue> = callFn("concat", this, other).cast()

  /**
   * Returns the IETF language tag of the locale being used by the provided [collator]. This can be
   * used to determine the default system locale, or to determine if a requested locale was
   * successfully loaded.
   */
  public fun resolvedLocale(collator: Expression<CollatorValue>): Expression<StringValue> =
    callFn("resolved-locale", collator).cast()

  // endregion

  // region Decision

  /**
   * Selects the first output from the given [conditions] whose corresponding test condition
   * evaluates to `true`, or the [fallback] value otherwise.
   *
   * Example:
   * ```
   * switch(
   *   condition(
   *     test = feature.has(const("color1")) and feature.has(const("color2")),
   *     output = interpolate(
   *       linear(),
   *       zoom(),
   *       1 to feature.get(const("color1")).convertToColor(),
   *       20 to feature.get(const("color2")).convertToColor()
   *     ),
   *   ),
   *   condition(
   *     test = feature.has(const("color")),
   *     output = feature.get(const("color")).convertToColor(),
   *   ),
   *   fallback = const(Color.Red),
   * )
   * ```
   *
   * If the feature has both a "color1" and "color2" property, the result is an interpolation
   * between these two colors based on the zoom level. Otherwise, if the feature has a "color"
   * property, that color is returned. If the feature has none of the three, the color red is
   * returned.
   */
  public fun <T : ExpressionValue> switch(
    vararg conditions: Condition<T>,
    fallback: Expression<T>,
  ): Expression<T> =
    callFn(
        "case",
        *conditions.foldToArgs { (test, output) ->
          add(test)
          add(output)
        },
        fallback,
      )
      .cast()

  public data class Condition<T : ExpressionValue>
  internal constructor(
    internal val test: Expression<BooleanValue>,
    internal val output: Expression<T>,
  )

  /** Create a [Condition], see [case] */
  public fun <T : ExpressionValue> condition(
    test: Expression<BooleanValue>,
    output: Expression<T>,
  ): Condition<T> = Condition(test, output)

  /**
   * Selects the output from the given [cases] whose label value matches the [input], or the
   * [fallback] value if no match is found.
   *
   * Each label must be unique. If the input type does not match the type of the labels, the result
   * will be the [fallback] value.
   *
   * Example:
   * ```
   * switch(
   *   input = feature.get(const("building_type")).asString(),
   *   case(
   *     label = "residential",
   *     output = const(Color.Cyan),
   *   ),
   *   case(
   *     label = listOf("commercial", "industrial"),
   *     output = const(Color.Yellow),
   *   ),
   *   fallback = const(Color.Red),
   * )
   * ```
   *
   * If the feature has a property "building_type" with the value "residential", cyan is returned.
   * Otherwise, if the value of that property is either "commercial" or "industrial", yellow is
   * returned. If none of that is true, the fallback is returned, i.e. red.
   */
  public fun <I : MatchableValue, O : ExpressionValue> switch(
    input: Expression<I>,
    vararg cases: Case<I, O>,
    fallback: Expression<O>,
  ): Expression<O> =
    callFn(
        "match",
        input,
        *cases.foldToArgs { (label, output) ->
          add(label)
          add(output)
        },
        fallback,
      )
      .cast()

  public data class Case<@Suppress("unused") I : MatchableValue, O : ExpressionValue>
  internal constructor(internal val label: Expression<*>, internal val output: Expression<O>)

  /** Create a [Case], see [switch] */
  public fun <O : ExpressionValue> case(
    label: String,
    output: Expression<O>,
  ): Case<StringValue, O> = Case(const(label), output)

  /** Create a [Case], see [switch] */
  public fun <O : ExpressionValue> case(label: Number, output: Expression<O>): Case<FloatValue, O> =
    Case(const(label.toFloat()), output)

  /** Create a [Case], see [switch] */
  @JvmName("stringsCase")
  public fun <O : ExpressionValue> case(
    label: List<String>,
    output: Expression<O>,
  ): Case<StringValue, O> = Case(Expression.ofList(label.map(::const)), output)

  /** Create a [Case], see [switch] */
  @JvmName("numbersCase")
  public fun <O : ExpressionValue> case(
    label: List<Number>,
    output: Expression<O>,
  ): Case<FloatValue, O> = Case(Expression.ofList(label.map { const(it.toFloat()) }), output)

  /**
   * Evaluates each expression in [values] in turn until the first non-null value is obtained, and
   * returns that value.
   */
  public fun <T : ExpressionValue> coalesce(vararg values: Expression<T>): Expression<T> =
    callFn("coalesce", *values).cast()

  /** Returns whether this expression is equal to [other]. */
  public infix fun Expression<EquatableValue>.eq(
    other: Expression<EquatableValue>
  ): Expression<BooleanValue> = callFn("==", this, other).cast()

  /**
   * Returns whether the [left] string expression is equal to the [right] string expression. An
   * optional [collator] (see [ExpressionScope.collator] function) can be specified to control
   * locale-dependent string comparisons.
   */
  public fun eq(
    left: Expression<StringValue>,
    right: Expression<StringValue>,
    collator: Expression<CollatorValue>,
  ): Expression<BooleanValue> = callFn("==", left, right, collator).cast()

  /** Returns whether this expression is not equal to [other]. */
  public infix fun Expression<EquatableValue>.neq(
    other: Expression<EquatableValue>
  ): Expression<BooleanValue> = callFn("!=", this, other).cast()

  /**
   * Returns whether the [left] string expression is not equal to the [right] string expression. An
   * optional [collator] (see [ExpressionScope.collator]) can be specified to control
   * locale-dependent string comparisons.
   */
  public fun neq(
    left: Expression<StringValue>,
    right: Expression<StringValue>,
    collator: Expression<CollatorValue>,
  ): Expression<BooleanValue> = callFn("!=", left, right, collator).cast()

  /**
   * Returns whether this expression is strictly greater than [other].
   *
   * Strings are compared lexicographically (`"b" > "a"`).
   */
  public infix fun <T> Expression<ComparableValue<T>>.gt(
    other: Expression<ComparableValue<T>>
  ): Expression<BooleanValue> = callFn(">", this, other).cast()

  /**
   * Returns whether the [left] string expression is strictly greater than the [right] string
   * expression. An optional [collator] (see [ExpressionScope.collator]) can be specified to control
   * locale-dependent string comparisons.
   *
   * Strings are compared lexicographically (`"b" > "a"`).
   */
  public fun gt(
    left: Expression<StringValue>,
    right: Expression<StringValue>,
    collator: Expression<CollatorValue>,
  ): Expression<BooleanValue> = callFn(">", left, right, collator).cast()

  /**
   * Returns whether this expression is strictly less than [other].
   *
   * Strings are compared lexicographically (`"a" < "b"`).
   */
  public infix fun <T> Expression<ComparableValue<T>>.lt(
    other: Expression<ComparableValue<T>>
  ): Expression<BooleanValue> = callFn("<", this, other).cast()

  /**
   * Returns whether the [left] string expression is strictly less than the [right] string
   * expression. An optional [collator] (see [ExpressionScope.collator]) can be specified to control
   * locale-dependent string comparisons.
   *
   * Strings are compared lexicographically (`"a" < "b"`).
   */
  public fun lt(
    left: Expression<StringValue>,
    right: Expression<StringValue>,
    collator: Expression<CollatorValue>,
  ): Expression<BooleanValue> = callFn("<", left, right, collator).cast()

  /**
   * Returns whether this expression is greater than or equal to [other].
   *
   * Strings are compared lexicographically (`"b" >= "a"`).
   */
  public infix fun <T> Expression<ComparableValue<T>>.gte(
    other: Expression<ComparableValue<T>>
  ): Expression<BooleanValue> = callFn(">=", this, other).cast()

  /**
   * Returns whether the [left] string expression is greater than or equal to the [right] string
   * expression. An optional [collator] (see [ExpressionScope.collator]) can be specified to control
   * locale-dependent string comparisons.
   *
   * Strings are compared lexicographically (`"b" >= "a"`).
   */
  public fun gte(
    left: Expression<StringValue>,
    right: Expression<StringValue>,
    collator: Expression<CollatorValue>,
  ): Expression<BooleanValue> = callFn(">=", left, right, collator).cast()

  /**
   * Returns whether this string expression is less than or equal to [other].
   *
   * Strings are compared lexicographically (`"a" <= "b"`).
   */
  public infix fun <T> Expression<ComparableValue<T>>.lte(
    other: Expression<ComparableValue<T>>
  ): Expression<BooleanValue> = callFn("<=", this, other).cast()

  /**
   * Returns whether the [left] string expression is less than or equal to the [right] string
   * expression. An optional [collator] (see [ExpressionScope.collator]) can be specified to control
   * locale-dependent string comparisons.
   *
   * Strings are compared lexicographically (`"a" < "b"`).
   */
  public fun lte(
    left: Expression<StringValue>,
    right: Expression<StringValue>,
    collator: Expression<CollatorValue>,
  ): Expression<BooleanValue> = callFn("<=", left, right, collator).cast()

  /** Returns whether all [expressions] are `true`. */
  public fun all(vararg expressions: Expression<BooleanValue>): Expression<BooleanValue> =
    callFn("all", *expressions).cast()

  /** Returns whether both this and [other] expressions are `true`. */
  public infix fun Expression<BooleanValue>.and(
    other: Expression<BooleanValue>
  ): Expression<BooleanValue> = all(this, other)

  /** Returns whether any [expressions] are `true`. */
  public fun any(vararg expressions: Expression<BooleanValue>): Expression<BooleanValue> =
    callFn("any", *expressions).cast()

  /** Returns whether any of this or the [other] expressions are `true`. */
  public infix fun Expression<BooleanValue>.or(
    other: Expression<BooleanValue>
  ): Expression<BooleanValue> = any(this, other)

  /** Negates this expression. */
  @JvmName("notOperator")
  public operator fun Expression<BooleanValue>.not(): Expression<BooleanValue> =
    callFn("!", this).cast()

  /**
   * Returns true if the evaluated feature is fully contained inside a boundary of the input
   * geometry, false otherwise. The input value can be a valid GeoJSON of type Polygon,
   * MultiPolygon, Feature, or FeatureCollection. Supported features for evaluation:
   * - Point: Returns false if a point is on the boundary or falls outside the boundary.
   * - LineString: Returns false if any part of a line falls outside the boundary, the line
   *   intersects the boundary, or a line's endpoint is on the boundary.
   */
  public fun within(geometry: Expression<GeoJsonValue>): Expression<BooleanValue> =
    callFn("within", geometry).cast()

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
  public fun <T : ExpressionValue> step(
    input: Expression<FloatValue>,
    fallback: Expression<T>,
    vararg stops: Pair<Number, Expression<T>>,
  ): Expression<T> =
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
      .cast()

  private fun <T, V : InterpolateableValue<T>> interpolateImpl(
    name: String,
    type: Expression<InterpolationValue>,
    input: Expression<FloatValue>,
    vararg stops: Pair<Number, Expression<V>>,
  ): Expression<V> =
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
      .cast()

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
  public fun <T, V : InterpolateableValue<T>> interpolate(
    type: Expression<InterpolationValue>,
    input: Expression<FloatValue>,
    vararg stops: Pair<Number, Expression<V>>,
  ): Expression<V> = interpolateImpl("interpolate", type, input, *stops)

  /**
   * Produces continuous, smooth results by interpolating between pairs of input and output values
   * ([stops]), given the [input] value. Works like [interpolate], but the interpolation is
   * performed in the
   * [Hue-Chroma-Luminance color space](https://en.wikipedia.org/wiki/HCL_color_space).
   *
   * Example:
   * ```
   * interpolateHcl(
   *   linear(),
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
  @JsOnlyApi
  public fun interpolateHcl(
    type: Expression<InterpolationValue>,
    input: Expression<FloatValue>,
    vararg stops: Pair<Number, Expression<ColorValue>>,
  ): Expression<ColorValue> = interpolateImpl("interpolate-hcl", type, input, *stops)

  /**
   * Produces continuous, smooth results by interpolating between pairs of input and output values
   * ([stops]), given the [input] value. Works like [interpolate], but the interpolation is
   * performed in the [CIELAB color space](https://en.wikipedia.org/wiki/CIELAB_color_space).
   */
  @JsOnlyApi
  public fun interpolateLab(
    type: Expression<InterpolationValue>,
    input: Expression<FloatValue>,
    vararg stops: Pair<Number, Expression<ColorValue>>,
  ): Expression<ColorValue> = interpolateImpl("interpolate-lab", type, input, *stops)

  /** Interpolates linearly between the pairs of stops. */
  public fun linear(): Expression<InterpolationValue> = callFn("linear").cast()

  /**
   * Interpolates exponentially between the stops.
   *
   * @param [base] controls the rate at which the output increases: higher values make the output
   *   increase more towards the high end of the range. With values close to 1 the output increases
   *   linearly.
   */
  public fun exponential(base: Expression<FloatValue>): Expression<InterpolationValue> =
    callFn("exponential", base).cast()

  /**
   * Interpolates using the cubic bezier curve defined by the given control points between the pairs
   * of stops.
   */
  public fun cubicBezier(
    x1: Expression<FloatValue>,
    y1: Expression<FloatValue>,
    x2: Expression<FloatValue>,
    y2: Expression<FloatValue>,
  ): Expression<InterpolationValue> = callFn("cubic-bezier", x1, y1, x2, y2).cast()

  // endregion

  // region Math

  /** Returns mathematical constant ln(2) = natural logarithm of 2. */
  public fun ln2(): Expression<FloatValue> = callFn("ln2").cast()

  /** Returns the mathematical constant π */
  public fun pi(): Expression<FloatValue> = callFn("pi").cast()

  /** Returns the mathematical constant e */
  public fun e(): Expression<FloatValue> = callFn("e").cast()

  /** Returns the sum of this number expression with [other]. */
  public operator fun <U, V : ScalarValue<U>> Expression<V>.plus(
    other: Expression<V>
  ): Expression<V> = callFn("+", this, other).cast()

  /** Returns the product of this number expression with [other]. */
  @JvmName("timesUnitLeft")
  public operator fun <U, V : ScalarValue<U>> Expression<V>.times(
    other: Expression<FloatValue>
  ): Expression<V> = callFn("*", this, other).cast()

  /** Returns the product of this number expression with [other]. */
  @JvmName("timesUnitRight")
  public operator fun <U, V : ScalarValue<U>> Expression<FloatValue>.times(
    other: Expression<V>
  ): Expression<V> = callFn("*", this, other).cast()

  /** Returns the product of this number expression with [other]. */
  public operator fun Expression<FloatValue>.times(
    other: Expression<FloatValue>
  ): Expression<FloatValue> = callFn("*", this, other).cast()

  /** Returns the result of subtracting [other] from this number expression. */
  public operator fun <U, V : ScalarValue<U>> Expression<V>.minus(
    other: Expression<ScalarValue<U>>
  ): Expression<V> = callFn("-", this, other).cast()

  /** Negates this number expression. */
  public operator fun <U, V : ScalarValue<U>> Expression<V>.unaryMinus(): Expression<V> =
    callFn("-", this).cast()

  /** Returns the result of floating point division of this number expression by [divisor]. */
  @JvmName("divUnitBoth")
  public operator fun <U, V : ScalarValue<U>> Expression<V>.div(
    divisor: Expression<V>
  ): Expression<FloatValue> = callFn("/", this, divisor).cast()

  /** Returns the result of floating point division of this number expression by [divisor]. */
  @JvmName("divUnitLeftOnly")
  public operator fun <U, V : ScalarValue<U>> Expression<V>.div(
    divisor: Expression<FloatValue>
  ): Expression<V> = callFn("/", this, divisor).cast()

  /** Returns the result of floating point division of this number expression by [divisor]. */
  public operator fun Expression<FloatValue>.div(
    divisor: Expression<FloatValue>
  ): Expression<FloatValue> = callFn("/", this, divisor).cast()

  /** Returns the remainder after integer division of this number expression by [divisor]. */
  public operator fun <U, V : ScalarValue<U>> Expression<V>.rem(
    divisor: Expression<IntValue>
  ): Expression<V> = callFn("%", this, divisor).cast()

  /** Returns the result of raising this number expression to the power of [exponent]. */
  public fun Expression<FloatValue>.pow(exponent: Expression<FloatValue>): Expression<FloatValue> =
    callFn("^", this, exponent).cast()

  /** Returns the square root of [value]. */
  public fun sqrt(value: Expression<FloatValue>): Expression<FloatValue> =
    callFn("sqrt", value).cast()

  /** Returns the base-ten logarithm of [value]. */
  public fun log10(value: Expression<FloatValue>): Expression<FloatValue> =
    callFn("log10", value).cast()

  /** Returns the natural logarithm of [value]. */
  public fun ln(value: Expression<FloatValue>): Expression<FloatValue> = callFn("ln", value).cast()

  /** Returns the base-two logarithm of [value]. */
  public fun log2(value: Expression<FloatValue>): Expression<FloatValue> =
    callFn("log2", value).cast()

  /** Returns the sine of [value]. */
  public fun sin(value: Expression<FloatValue>): Expression<FloatValue> =
    callFn("sin", value).cast()

  /** Returns the cosine of [value]. */
  public fun cos(value: Expression<FloatValue>): Expression<FloatValue> =
    callFn("cos", value).cast()

  /** Returns the tangent of [value]. */
  public fun tan(value: Expression<FloatValue>): Expression<FloatValue> =
    callFn("tan", value).cast()

  /** Returns the arcsine of [value]. */
  public fun asin(value: Expression<FloatValue>): Expression<FloatValue> =
    callFn("asin", value).cast()

  /** Returns the arccosine of [value]. */
  public fun acos(value: Expression<FloatValue>): Expression<FloatValue> =
    callFn("acos", value).cast()

  /** Returns the arctangent of [value]. */
  public fun atan(value: Expression<FloatValue>): Expression<FloatValue> =
    callFn("atan", value).cast()

  /** Returns the smallest of all given [numbers]. */
  public fun <U, V : ScalarValue<U>> min(vararg numbers: Expression<V>): Expression<V> =
    callFn("min", *numbers).cast()

  /** Returns the greatest of all given [numbers]. */
  public fun <U, V : ScalarValue<U>> max(vararg numbers: Expression<V>): Expression<V> =
    callFn("max", *numbers).cast()

  /** Returns the absolute value of [value], i.e. always a positive value. */
  public fun <U, V : ScalarValue<U>> abs(value: Expression<V>): Expression<V> =
    callFn("abs", value).cast()

  /**
   * Rounds [value] to the nearest integer. Halfway values are rounded away from zero.
   *
   * For example `round(const(-1.5))` evaluates to `-2`.
   */
  public fun round(value: Expression<FloatValue>): Expression<IntValue> =
    callFn("round", value).cast()

  /** Returns the smallest integer that is greater than or equal to [value]. */
  public fun ceil(value: Expression<FloatValue>): Expression<IntValue> =
    callFn("ceil", value).cast()

  /** Returns the largest integer that is less than or equal to [value]. */
  public fun floor(value: Expression<FloatValue>): Expression<IntValue> =
    callFn("floor", value).cast()

  /** Returns the shortest distance in meters between the evaluated feature and [geometry]. */
  public fun distance(geometry: Expression<GeoJsonValue>): Expression<FloatValue> =
    callFn("distance", geometry).cast()

  // endregion

  // region Color

  /**
   * Returns a four-element list, containing the color's red, green, blue, and alpha components, in
   * that order.
   */
  public fun Expression<ColorValue>.toRgbaComponents(): Expression<VectorValue<Number>> =
    callFn("to-rgba", this).cast()

  /**
   * Creates a color value from [red], [green], and [blue] components, which must range between 0
   * and 255, and optionally an [alpha] component which must range between 0 and 1.
   *
   * If any component is out of range, the expression is an error.
   */
  public fun rgbColor(
    red: Expression<IntValue>,
    green: Expression<IntValue>,
    blue: Expression<IntValue>,
    alpha: Expression<FloatValue>? = null,
  ): Expression<ColorValue> =
    if (alpha != null) {
        callFn("rgba", red, green, blue, alpha)
      } else {
        callFn("rgb", red, green, blue)
      }
      .cast()

  // endregion

  // region Feature data

  /** Object to access feature-related data, see [feature] */
  public object FeatureScope {
    /**
     * Returns the value corresponding to the given [key] in the current feature's properties or
     * `null` if it is not present.
     */
    public fun get(key: Expression<StringValue>): Expression<*> = callFn("get", key)

    /** Tests for the presence of a property value [key] in the current feature's properties. */
    public fun has(key: Expression<StringValue>): Expression<BooleanValue> =
      callFn("has", key).cast()

    /**
     * Gets the feature properties object. Note that in some cases, it may be more efficient to use
     * [get]`("property_name")` directly.
     */
    public fun properties(): Expression<MapValue<*>> = callFn("properties").cast()

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
     * When `source.promoteId` is not provided, features are identified by their `id` attribute,
     * which must be an integer or a string that can be cast to an integer. When `source.promoteId`
     * is provided, features are identified by their `promoteId` property, which may be a number,
     * string, or any primitive data type. Note that [state] can only be used with layer properties
     * that support data-driven styling.
     */
    // TODO:  document which layer properties support feature state expressions on which platforms
    @JsOnlyApi
    public fun <T : ExpressionValue> state(key: Expression<StringValue>): Expression<T> =
      callFn("feature-state", key).cast()

    /** Gets the feature's geometry type. */
    public fun type(): Expression<GeometryType> = callFn("geometry-type").cast()

    /** Gets the feature's id, if it has one. */
    public fun <T : ExpressionValue> id(): Expression<T> = callFn("id").cast()

    /**
     * Gets the progress along a gradient line. Can only be used in the `gradient` property of a
     * line layer, see [LineLayer][dev.sargunv.maplibrecompose.compose.layer.LineLayer].
     */
    public fun lineProgress(value: Expression<FloatValue>): Expression<FloatValue> =
      callFn("line-progress", value).cast()

    /**
     * Gets the value of a cluster property accumulated so far. Can only be used in the
     * `clusterProperties` option of a clustered GeoJSON source, see
     * [GeoJsonOptions][dev.sargunv.maplibrecompose.core.source.GeoJsonOptions].
     */
    public fun <T : ExpressionValue> accumulated(key: Expression<StringValue>): Expression<T> =
      callFn("accumulated", key).cast()
  }

  /** Accesses to feature-related data */
  public val feature: FeatureScope = FeatureScope

  // endregion

  // region Zoom

  /**
   * Gets the current zoom level. Note that in layer style properties, [zoom] may only appear as the
   * input to a top-level [step] or [interpolate] (, [interpolateHcl], [interpolateLab], ...)
   * expression.
   */
  public fun zoom(): Expression<FloatValue> = callFn("zoom").cast()

  // endregion

  // region Heatmap

  /**
   * Gets the kernel density estimation of a pixel in a heatmap layer, which is a relative measure
   * of how many data points are crowded around a particular pixel. Can only be used in the
   * expression for the `color` parameter in a
   * [HeatmapLayer][dev.sargunv.maplibrecompose.compose.layer.HeatmapLayer].
   */
  public fun heatmapDensity(): Expression<FloatValue> = callFn("heatmap-density").cast()

  // endregion

  // region Utils

  @Suppress("UNCHECKED_CAST")
  /** Casts this expression to the specified type without any runtime check. Use with caution. */
  public fun <T : ExpressionValue> Expression<*>.cast(): Expression<T> = this as Expression<T>

  private fun callFn(function: String, vararg args: Expression<*>): Expression<*> =
    Expression.ofList(
      buildList {
        add(const(function))
        args.forEach { add(it) }
      }
    )

  private inline fun buildOptions(block: MutableMap<String, Expression<*>>.() -> Unit) =
    Expression.ofMap(mutableMapOf<String, Expression<*>>().apply(block).mapValues { it.value })

  private inline fun <T> Array<T>.foldToArgs(
    block: MutableList<Expression<*>>.(element: T) -> Unit
  ) =
    fold(mutableListOf<Expression<*>>()) { acc, element -> acc.apply { block(element) } }
      .toTypedArray()

  private inline fun <T> List<T>.foldToArgs(
    block: MutableList<Expression<*>>.(element: T) -> Unit
  ) =
    fold(mutableListOf<Expression<*>>()) { acc, element -> acc.apply { block(element) } }
      .toTypedArray()

  // endregion
}
