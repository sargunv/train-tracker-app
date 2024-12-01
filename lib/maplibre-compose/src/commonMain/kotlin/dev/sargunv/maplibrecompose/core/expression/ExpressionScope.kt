package dev.sargunv.maplibrecompose.core.expression

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import io.github.dellisd.spatialk.geojson.Geometry
import kotlin.jvm.JvmName

@Suppress("INAPPLICABLE_JVM_NAME")
public interface ExpressionScope {

  // basic types: https://maplibre.org/maplibre-style-spec/types/

  public fun const(string: String): Expression<String> = Expression.ofString(string)

  public fun const(float: Float): Expression<Number> = Expression.ofFloat(float)

  public fun const(bool: Boolean): Expression<Boolean> = Expression.ofBoolean(bool)

  // corresponds to "point" in the style spec
  public fun const(offset: Offset): Expression<Offset> = Expression.ofOffset(offset)

  // corresponds to "padding" in the style spec
  public fun const(padding: PaddingValues.Absolute): Expression<PaddingValues.Absolute> =
    Expression.ofPadding(padding)

  public fun <T : LayerPropertyEnum> const(value: T): Expression<T> =
    Expression.ofLayerPropertyEnum(value)

  @Suppress("UNCHECKED_CAST")
  public fun <T> nil(): Expression<T> = Expression.ofNull() as Expression<T>

  public fun const(color: Color): Expression<Color> = Expression.ofColor(color)

  // expressions: https://maplibre.org/maplibre-style-spec/expressions/

  // variable binding

  /**
   * Binds expressions to named variables, which can then be referenced in the result expression
   * using [var].
   */
  public fun <T> let(name: String, value: Expression<*>, expression: Expression<T>): Expression<T> =
    callFn("let", const(name), value, expression)

  /** References variable bound using [let]. */
  public fun <T> `var`(name: String): Expression<T> = callFn("var", const(name))

  // types

  /** Produces a literal array value. */
  public fun <T> literal(values: List<Expression<T>>): Expression<List<T>> =
    callFn("literal", Expression.ofList(values))

  /** Produces a literal object value. */
  public fun <T> literal(values: Map<String, Expression<T>>): Expression<Map<String, T>> =
    callFn("literal", Expression.ofMap(values))

  /**
   * Asserts that the input is an array (optionally with a specific item type and length). If, when
   * the input expression is evaluated, it is not of the asserted type, then this assertion will
   * cause the whole expression to be aborted.
   */
  public fun <T> array(
    value: Expression<*>,
    type: Expression<String>? = null,
    length: Expression<Number>? = null,
  ): Expression<List<T>> {
    val args = buildList {
      type?.let { add(const("array")) }
      length?.let { add(const("array")) }
    }
    return callFn("array", value, *args.toTypedArray())
  }

  /** Returns a string describing the type of the given value. */
  public fun `typeof`(expression: Expression<*>): Expression<String> = callFn("typeof", expression)

  /**
   * Asserts that the input value is a string. If multiple values are provided, each one is
   * evaluated in order until a string is obtained. If none of the inputs are strings, the
   * expression is an error.
   */
  public fun string(value: Expression<*>, vararg fallbacks: Expression<*>): Expression<String> =
    callFn("string", value, *fallbacks)

  /**
   * Asserts that the input value is a number. If multiple values are provided, each one is
   * evaluated in order until a number is obtained. If none of the inputs are numbers, the
   * expression is an error.
   */
  public fun number(value: Expression<*>, vararg fallbacks: Expression<*>): Expression<Number> =
    callFn("number", value, *fallbacks)

  /**
   * Asserts that the input value is a boolean. If multiple values are provided, each one is
   * evaluated in order until a boolean is obtained. If none of the inputs are booleans, the
   * expression is an error.
   */
  public fun boolean(value: Expression<*>, vararg fallbacks: Expression<*>): Expression<Boolean> =
    callFn("boolean", value, *fallbacks)

  /**
   * Asserts that the input value is an object. If multiple values are provided, each one is
   * evaluated in order until an object is obtained. If none of the inputs are objects, the
   * expression is an error.
   */
  public fun <T> `object`(
    value: Expression<*>,
    vararg fallbacks: Expression<*>,
  ): Expression<Map<String, T>> = callFn("object", value, *fallbacks)

  /**
   * Returns a collator for use in locale-dependent comparison operations. The [caseSensitive] and
   * [diacriticSensitive] options default to false. The [locale] argument specifies the IETF
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
   * Returns a formatted string for displaying mixed-format text in the text-field property. The
   * input may contain a string literal or expression, including an 'image' expression. Strings may
   * be followed by a style override object that supports the following properties:
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

  public fun format(value: Expression<String>): Expression<TFormatted> =
    callFn("format", value, buildOptions {})

  public data class FormatStyle(
    val textFont: Expression<String>? = null,
    val textColor: Expression<String>? = null,
    val fontScale: Expression<Number>? = null,
  )

  /**
   * Returns an image type for use in icon-image, *-pattern entries and as a section in the [format]
   * expression. If set, the image argument will check that the requested image exists in the style
   * and will return either the resolved image name or null, depending on whether or not the image
   * is currently in the style. This validation process is synchronous and requires the image to
   * have been added to the style before requesting it in the image argument.
   */
  public fun image(value: Expression<String>): Expression<TResolvedImage> = callFn("image", value)

  /**
   * Converts the input number into a string representation using the providing formatting rules. If
   * set, the locale argument specifies the locale to use, as a BCP 47 language tag. If set, the
   * currency argument specifies an ISO 4217 code to use for currency-style formatting. If set, the
   * min-fraction-digits and max-fraction-digits arguments specify the minimum and maximum number of
   * fractional digits to include.
   */
  public fun numberFormat(
    number: Expression<Number>,
    locale: Expression<String>? = null,
    currency: Expression<String>? = null,
    minFractionDigits: Expression<Number>? = null,
    maxFractionDigits: Expression<Number>? = null,
  ): Expression<String> =
    callFn(
      "number-format",
      number,
      buildOptions {
        locale?.let { put("locale", it) }
        currency?.let { put("currency", it) }
        minFractionDigits?.let { put("min-fraction-digits", it) }
        maxFractionDigits?.let { put("max-fraction-digits", it) }
      },
    )

  /**
   * Converts the input value to a string. If the input is null, the result is "". If the input is a
   * boolean, the result is "true" or "false". If the input is a number, it is converted to a string
   * as specified by the "NumberToString" algorithm of the ECMAScript Language Specification. If the
   * input is a color, it is converted to a string of the form "rgba(r,g,b,a)", where r, g, and b
   * are numerals ranging from 0 to 255, and a ranges from 0 to 1. Otherwise, the input is converted
   * to a string in the format specified by the JSON.stringify function of the ECMAScript Language
   * Specification.
   */
  public fun toString(value: Expression<*>): Expression<String> = callFn("to-string", value)

  /**
   * Converts the input value to a number, if possible. If the input is null or false, the result
   * is 0. If the input is true, the result is 1. If the input is a string, it is converted to a
   * number as specified by the "ToNumber Applied to the String Type" algorithm of the ECMAScript
   * Language Specification. If multiple values are provided, each one is evaluated in order until
   * the first successful conversion is obtained. If none of the inputs can be converted, the
   * expression is an error.
   */
  public fun toNumber(value: Expression<*>, vararg fallbacks: Expression<*>): Expression<Number> =
    callFn("to-number", value, *fallbacks)

  /**
   * Converts the input value to a boolean. The result is false when then input is an empty string,
   * 0, false, null, or NaN; otherwise it is true.
   */
  public fun toBoolean(value: Expression<*>): Expression<Boolean> = callFn("to-boolean", value)

  /**
   * Converts the input value to a color. If multiple values are provided, each one is evaluated in
   * order until the first successful conversion is obtained. If none of the inputs can be
   * converted, the expression is an error.
   */
  public fun toColor(value: Expression<*>, vararg fallbacks: Expression<*>): Expression<Color> =
    callFn("to-color", value, *fallbacks)

  // lookup

  /** Retrieves an item from an array. */
  public fun <T> at(index: Expression<Number>, array: Expression<List<T>>): Expression<T> =
    callFn("at", index, array)

  @JvmName("getAtIndex")
  public operator fun <T> Expression<List<T>>.get(index: Expression<Number>): Expression<T> =
    at(index, this)

  /** Determines whether an item exists in an array. */
  @JvmName("inList")
  public fun `in`(needle: Expression<*>, haystack: Expression<List<*>>): Expression<Boolean> =
    callFn("in", needle, haystack)

  /** Determines whether a substring exists in a string. */
  @JvmName("inString")
  public fun `in`(needle: Expression<String>, haystack: Expression<String>): Expression<Boolean> =
    callFn("in", needle, haystack)

  @JvmName("inListInfix")
  public infix fun Expression<*>.`in`(other: Expression<List<*>>): Expression<Boolean> =
    `in`(this, other)

  @JvmName("inStringInfix")
  public infix fun Expression<String>.`in`(other: Expression<String>): Expression<Boolean> =
    `in`(this, other)

  /**
   * Returns the first position at which an item can be found in an array or a substring can be
   * found in a string, or -1 if the input cannot be found. Accepts an optional index from where to
   * begin the search. In a string, a UTF-16 surrogate pair counts as a single position.
   */
  public fun indexOf(
    value: Expression<*>,
    array: Expression<List<*>>,
    start: Expression<Number>? = null,
  ): Expression<Number> {
    val args = buildList {
      add(value)
      add(array)
      start?.let { add(it) }
    }
    return callFn("index-of", *args.toTypedArray())
  }

  /**
   * Returns a substring from a string from a specified start index, or between a start index and an
   * end index if set. The return value is inclusive of the start index but not of the end index. A
   * UTF-16 surrogate pair counts as a single position.
   */
  @JvmName("sliceString")
  public fun slice(
    value: Expression<String>,
    start: Expression<Number>,
    length: Expression<Number>? = null,
  ): Expression<String> {
    val args = buildList {
      add(value)
      add(start)
      length?.let { add(it) }
    }
    return callFn("slice", *args.toTypedArray())
  }

  /**
   * Returns an item from an list from a specified start index, or between a start index and an end
   * index if set. The return value is inclusive of the start index but not of the end index.
   */
  @JvmName("sliceList")
  public fun <T> slice(
    value: Expression<List<T>>,
    start: Expression<Number>,
    length: Expression<Number>? = null,
  ): Expression<T> {
    val args = buildList {
      add(value)
      add(start)
      length?.let { add(it) }
    }
    return callFn("slice", *args.toTypedArray())
  }

  /**
   * Retrieves a property value from the current feature's properties, or from another object if a
   * second argument is provided. Returns null if the requested property is missing.
   */
  public fun <T> get(
    key: Expression<String>,
    obj: Expression<Map<String, *>>? = null,
  ): Expression<T> {
    val args = obj?.let { listOf(key, it) } ?: listOf(key)
    return callFn("get", *args.toTypedArray())
  }

  /**
   * Tests for the presence of an property value in the current feature's properties, or from
   * another object if a second argument is provided.
   */
  public fun has(
    key: Expression<String>,
    obj: Expression<Map<String, *>>? = null,
  ): Expression<Boolean> {
    val args = obj?.let { listOf(key, it) } ?: listOf(key)
    return callFn("has", *args.toTypedArray())
  }

  /**
   * Gets the length of an array or string. In a string, a UTF-16 surrogate pair counts as a single
   * position.
   */
  @JvmName("lengthOfString")
  public fun length(value: Expression<String>): Expression<Number> = callFn("length", value)

  /**
   * Gets the length of an array or string. In a string, a UTF-16 surrogate pair counts as a single
   * position.
   */
  @JvmName("lengthOfList")
  public fun length(value: Expression<List<*>>): Expression<Number> = callFn("length", value)

  // decision

  /**
   * Selects the first output whose corresponding test condition evaluates to true, or the fallback
   * value otherwise.
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

  public infix fun <Output> Expression<Boolean>.then(
    output: Expression<Output>
  ): CaseBranch<Output> = CaseBranch(this, output)

  /**
   * Selects the output whose label value matches the input value, or the fallback value if no match
   * is found. The input can be any expression (e.g. ["get", "building_type"]). Each label must be
   * either:
   * - a single string; or
   * - a list of strings. The input matches if any of the values in the array matches, similar to
   *   the "in" operator.
   *
   * Each label must be unique. If the input type does not match the type of the labels, the result
   * will be the fallback value.
   */
  @JvmName("matchStrings")
  public fun <T> match(
    input: Expression<String>,
    fallback: Expression<T>,
    vararg branches: MatchBranch<String, T>,
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
   * Selects the output whose label value matches the input value, or the fallback value if no match
   * is found. The input can be any expression (e.g. ["get", "building_type"]). Each label must be
   * either:
   * - a single number; or
   * - a list of numbers. The input matches if any of the values in the array matches, similar to
   *   the "in" operator.
   *
   * Each label must be unique. If the input type does not match the type of the labels, the result
   * will be the fallback value.
   */
  @JvmName("matchNumbers")
  public fun <T> match(
    input: Expression<Number>,
    fallback: Expression<T>,
    vararg branches: MatchBranch<Number, T>,
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

  public infix fun <Output> String.then(output: Expression<Output>): MatchBranch<String, Output> =
    MatchBranch(const(this), output)

  public infix fun <Output> Number.then(output: Expression<Output>): MatchBranch<Number, Output> =
    MatchBranch(const(this.toFloat()), output)

  @JvmName("stringsThen")
  public infix fun <Output> List<String>.then(
    output: Expression<Output>
  ): MatchBranch<String, Output> =
    MatchBranch(Expression.ofList(this.map { const(it.toFloat()) }), output)

  @JvmName("numbersThen")
  public infix fun <Output> List<Number>.then(
    output: Expression<Output>
  ): MatchBranch<Number, Output> =
    MatchBranch(Expression.ofList(this.map { const(it.toFloat()) }), output)

  /**
   * Evaluates each expression in turn until the first non-null value is obtained, and returns that
   * value.
   */
  public fun <T> coalesce(vararg values: Expression<T?>): Expression<T> =
    callFn("coalesce", *values)

  public infix fun Expression<*>.eq(other: Expression<*>): Expression<Boolean> =
    callFn("==", this, other)

  public fun eq(
    left: Expression<String>,
    right: Expression<String>,
    collator: Expression<TCollator>? = null,
  ): Expression<Boolean> = callFn("==", left, right, *buildArgs { collator?.let { add(it) } })

  public infix fun Expression<*>.neq(other: Expression<*>): Expression<Boolean> =
    callFn("!=", this, other)

  public fun neq(
    left: Expression<String>,
    right: Expression<String>,
    collator: Expression<TCollator>? = null,
  ): Expression<Boolean> = callFn("!=", left, right, *buildArgs { collator?.let { add(it) } })

  @JvmName("gtNumber")
  public infix fun Expression<Number>.gt(other: Expression<Number>): Expression<Boolean> =
    callFn(">", this, other)

  @JvmName("gtString")
  public infix fun Expression<String>.gt(other: Expression<String>): Expression<Boolean> =
    callFn(">", this, other)

  public fun gt(
    left: Expression<String>,
    right: Expression<String>,
    collator: Expression<TCollator>? = null,
  ): Expression<Boolean> = callFn(">", left, right, *buildArgs { collator?.let { add(it) } })

  @JvmName("ltNumber")
  public infix fun Expression<Number>.lt(other: Expression<Number>): Expression<Boolean> =
    callFn("<", this, other)

  @JvmName("ltString")
  public infix fun Expression<String>.lt(other: Expression<String>): Expression<Boolean> =
    callFn("<", this, other)

  public fun lt(
    left: Expression<String>,
    right: Expression<String>,
    collator: Expression<TCollator>? = null,
  ): Expression<Boolean> = callFn("<", left, right, *buildArgs { collator?.let { add(it) } })

  @JvmName("gteNumber")
  public infix fun Expression<Number>.gte(other: Expression<Number>): Expression<Boolean> =
    callFn(">=", this, other)

  @JvmName("gteString")
  public infix fun Expression<String>.gte(other: Expression<String>): Expression<Boolean> =
    callFn(">=", this, other)

  public fun gte(
    left: Expression<String>,
    right: Expression<String>,
    collator: Expression<TCollator>? = null,
  ): Expression<Boolean> = callFn(">=", left, right, *buildArgs { collator?.let { add(it) } })

  @JvmName("lteNumber")
  public infix fun Expression<Number>.lte(other: Expression<Number>): Expression<Boolean> =
    callFn("<=", this, other)

  @JvmName("lteString")
  public infix fun Expression<String>.lte(other: Expression<String>): Expression<Boolean> =
    callFn("<=", this, other)

  public fun lte(
    left: Expression<String>,
    right: Expression<String>,
    collator: Expression<TCollator>? = null,
  ): Expression<Boolean> = callFn("<=", left, right, *buildArgs { collator?.let { add(it) } })

  public fun all(vararg expressions: Expression<Boolean>): Expression<Boolean> =
    callFn("all", *expressions)

  public fun any(vararg expressions: Expression<Boolean>): Expression<Boolean> =
    callFn("any", *expressions)

  public fun not(expression: Expression<Boolean>): Expression<Boolean> = callFn("!", expression)

  @JvmName("notOperator")
  public operator fun Expression<Boolean>.not(): Expression<Boolean> = not(this)

  public fun within(geometry: Expression<Geometry>): Expression<Boolean> =
    callFn("within", geometry)

  // ramps, scales, curves

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

  @JvmName("interpolateNumber")
  public fun interpolate(
    type: Expression<TInterpolationType>,
    input: Expression<Number>,
    vararg stops: Pair<Number, Expression<Number>>,
  ): Expression<Number> = interpolateImpl("interpolate", type, input, *stops)

  @JvmName("interpolateColor")
  public fun interpolate(
    type: Expression<TInterpolationType>,
    input: Expression<Number>,
    vararg stops: Pair<Number, Expression<Color>>,
  ): Expression<Color> = interpolateImpl("interpolate", type, input, *stops)

  @JvmName("interpolatePoint")
  public fun interpolate(
    type: Expression<TInterpolationType>,
    input: Expression<Number>,
    vararg stops: Pair<Number, Expression<Offset>>,
  ): Expression<Offset> = interpolateImpl("interpolate", type, input, *stops)

  @JvmName("interpolateNumbers")
  public fun interpolate(
    type: Expression<TInterpolationType>,
    input: Expression<Number>,
    vararg stops: Pair<Number, Expression<List<Number>>>,
  ): Expression<List<Number>> = interpolateImpl("interpolate", type, input, *stops)

  public fun interpolateHcl(
    type: Expression<TInterpolationType>,
    input: Expression<Number>,
    vararg stops: Pair<Number, Expression<Color>>,
  ): Expression<Color> = interpolateImpl("interpolate-hcl", type, input, *stops)

  public fun interpolateLab(
    type: Expression<TInterpolationType>,
    input: Expression<Number>,
    vararg stops: Pair<Number, Expression<Color>>,
  ): Expression<Color> = interpolateImpl("interpolate-lab", type, input, *stops)

  public fun exponential(base: Expression<Number>): Expression<TInterpolationType> =
    callFn("exponential", base)

  public fun linear(): Expression<TInterpolationType> = callFn("linear")

  public fun cubicBezier(
    x1: Expression<Number>,
    y1: Expression<Number>,
    x2: Expression<Number>,
    y2: Expression<Number>,
  ): Expression<TInterpolationType> = callFn("cubic-bezier", x1, y1, x2, y2)

  // math

  public fun ln2(): Expression<Number> = callFn("ln2")

  public fun pi(): Expression<Number> = callFn("pi")

  public fun e(): Expression<Number> = callFn("e")

  public fun sum(vararg numbers: Expression<Number>): Expression<Number> = callFn("+", *numbers)

  public fun product(vararg numbers: Expression<Number>): Expression<Number> = callFn("*", *numbers)

  public operator fun Expression<Number>.plus(other: Expression<Number>): Expression<Number> =
    sum(this, other)

  public operator fun Expression<Number>.times(other: Expression<Number>): Expression<Number> =
    product(this, other)

  public operator fun Expression<Number>.minus(other: Expression<Number>): Expression<Number> =
    callFn("-", this, other)

  public operator fun Expression<Number>.unaryMinus(): Expression<Number> = callFn("-", this)

  public operator fun Expression<Number>.div(b: Expression<Number>): Expression<Number> =
    callFn("/", this, b)

  public operator fun Expression<Number>.rem(b: Expression<Number>): Expression<Number> =
    callFn("%", this, b)

  public fun Expression<Number>.pow(exponent: Expression<Number>): Expression<Number> =
    callFn("^", this, exponent)

  public fun sqrt(value: Expression<Number>): Expression<Number> = callFn("sqrt", value)

  public fun log10(value: Expression<Number>): Expression<Number> = callFn("log10", value)

  public fun ln(value: Expression<Number>): Expression<Number> = callFn("ln", value)

  public fun log2(value: Expression<Number>): Expression<Number> = callFn("log2", value)

  public fun sin(value: Expression<Number>): Expression<Number> = callFn("sin", value)

  public fun cos(value: Expression<Number>): Expression<Number> = callFn("cos", value)

  public fun tan(value: Expression<Number>): Expression<Number> = callFn("tan", value)

  public fun asin(value: Expression<Number>): Expression<Number> = callFn("asin", value)

  public fun acos(value: Expression<Number>): Expression<Number> = callFn("acos", value)

  public fun atan(value: Expression<Number>): Expression<Number> = callFn("atan", value)

  public fun min(vararg numbers: Expression<Number>): Expression<Number> = callFn("min", *numbers)

  public fun max(vararg numbers: Expression<Number>): Expression<Number> = callFn("max", *numbers)

  public fun round(value: Expression<Number>): Expression<Number> = callFn("round", value)

  public fun abs(value: Expression<Number>): Expression<Number> = callFn("abs", value)

  public fun ceil(value: Expression<Number>): Expression<Number> = callFn("ceil", value)

  public fun floor(value: Expression<Number>): Expression<Number> = callFn("floor", value)

  public fun distance(value: Expression<Geometry>): Expression<Number> = callFn("distance", value)

  // color

  public fun toRgba(color: Expression<Color>): Expression<List<Number>> = callFn("to-rgba", color)

  public fun rgba(
    red: Expression<Number>,
    green: Expression<Number>,
    blue: Expression<Number>,
    alpha: Expression<Number>,
  ): Expression<Color> = callFn("rgba", red, green, blue, alpha)

  public fun rgb(
    red: Expression<Number>,
    green: Expression<Number>,
    blue: Expression<Number>,
  ): Expression<Color> = callFn("rgb", red, green, blue)

  // feature data

  public fun <T> properties(): Expression<Map<String, T>> = callFn("properties")

  public fun <T> featureState(key: Expression<String>): Expression<T> = callFn("feature-state", key)

  public fun geometryType(): Expression<String> = callFn("geometry-type")

  public fun <T> id(): Expression<T> = callFn("id")

  public fun lineProgress(value: Expression<Number>): Expression<Number> =
    callFn("line-progress", value)

  public fun <T> accumulated(key: Expression<String>): Expression<T> = callFn("accumulated", key)

  // zoom

  public fun zoom(): Expression<Number> = callFn("zoom")

  // heatmap

  public fun heatmapDensity(): Expression<Number> = callFn("heatmap-density")

  // string

  public fun isSupportedScript(script: Expression<String>): Expression<Boolean> =
    callFn("is-supported-script", script)

  public fun upcase(string: Expression<String>): Expression<String> = callFn("upcase", string)

  public fun downcase(string: Expression<String>): Expression<String> = callFn("downcase", string)

  public fun concat(vararg strings: Expression<String>): Expression<String> =
    callFn("concat", *strings)

  public fun resolvedLocale(collator: Expression<TCollator>): Expression<String> =
    callFn("resolved-locale", collator)

  // utils

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
}
