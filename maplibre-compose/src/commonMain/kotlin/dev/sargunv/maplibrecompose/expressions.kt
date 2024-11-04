package dev.sargunv.maplibrecompose

@Suppress("MemberVisibilityCanBePrivate")
public object ExpressionDsl {
  /**
   * Binds expressions to named variables, which can then be referenced in the result expression
   * using [var].
   */
  public fun <Input, Output> let(
    name: String,
    value: Expression<Input>,
    expression: Expression<Output>,
  ): Expression<Output> = call("let", literal(name), value, expression)

  /** References variable bound using [let]. */
  public fun <T> `var`(name: String): Expression<T> = call("var", literal(name))

  /** Produces a literal string value. */
  public fun literal(value: String): Expression<String> = Expression(value)

  /** Produces a literal number value. */
  public fun literal(value: Number): Expression<Number> = Expression(value)

  /** Produces a literal boolean value. */
  public fun literal(value: Boolean): Expression<Boolean> = Expression(value)

  /** Produces a literal array value. */
  public fun <T> literal(values: List<Expression<T>>): Expression<List<T>> =
    call("literal", Expression(values))

  /** Produces a literal object value. */
  public fun literal(values: Map<String, Expression<*>>): Expression<Map<String, Expression<*>>> =
    call("literal", Expression(values))

  /** Produces a literal null value. */
  public fun `null`(): Expression<Nothing?> = Expression(null)

  /**
   * Asserts that the input is an array (optionally with a specific item type and length). If, when
   * the input expression is evaluated, it is not of the asserted type, then this assertion will
   * cause the whole expression to be aborted.
   */
  public fun <T> array(
    value: Expression<*>,
    type: String? = null,
    length: Int? = null,
  ): Expression<Array<T>> {
    val args = buildList {
      if (type != null) add(literal(type))
      if (length != null) add(literal(length))
    }
    return call("array", value, *args.toTypedArray())
  }

  /** Returns a string describing the type of the given value. */
  public fun `typeof`(expression: Expression<*>): Expression<String> = call("typeof", expression)

  /**
   * Asserts that the input value is a string. If multiple values are provided, each one is
   * evaluated in order until a string is obtained. If none of the inputs are strings, the
   * expression is an error.
   */
  public fun string(value: Expression<*>, vararg fallbacks: Expression<*>): Expression<String> =
    call("string", value, *fallbacks)

  /**
   * Asserts that the input value is a number. If multiple values are provided, each one is
   * evaluated in order until a number is obtained. If none of the inputs are numbers, the
   * expression is an error.
   */
  public fun number(value: Expression<*>, vararg fallbacks: Expression<*>): Expression<Number> =
    call("number", value, *fallbacks)

  /**
   * Asserts that the input value is a boolean. If multiple values are provided, each one is
   * evaluated in order until a boolean is obtained. If none of the inputs are booleans, the
   * expression is an error.
   */
  public fun boolean(value: Expression<*>, vararg fallbacks: Expression<*>): Expression<Boolean> =
    call("boolean", value, *fallbacks)

  /**
   * Asserts that the input value is an object. If multiple values are provided, each one is
   * evaluated in order until an object is obtained. If none of the inputs are objects, the
   * expression is an error.
   */
  public fun `object`(
    value: Expression<*>,
    vararg fallbacks: Expression<*>,
  ): Expression<Map<String, *>> = call("object", value, *fallbacks)

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
  ): Expression<CollatorValue> =
    call(
      "collator",
      options {
        if (caseSensitive != null) put("case-sensitive", caseSensitive)
        if (diacriticSensitive != null) put("diacritic-sensitive", diacriticSensitive)
        if (locale != null) put("locale", locale)
      },
    )

  /**
   * Returns a formatted string for displaying mixed-format text in the text-field property. The
   * input may contain a string literal or expression, including an 'image' expression. Strings may
   * be followed by a style override object that supports the following properties:
   */
  public fun format(vararg sections: Pair<Expression<*>, FormatStyle>): Expression<FormattedValue> {
    val args =
      sections.flatMap { (value, style) ->
        listOf(
          value,
          options {
            if (style.textFont != null) put("text-font", style.textFont)
            if (style.textColor != null) put("text-color", style.textColor)
            if (style.fontScale != null) put("font-scale", style.fontScale)
          },
        )
      }
    return call("format", *args.toTypedArray())
  }

  public data class FormatStyle(
    val textFont: Expression<String>? = null,
    val textColor: Expression<String>? = null,
    val fontScale: Expression<Number>? = null,
  )

  /**
   * Returns an image type for use in [iconImage], *-pattern entries and as a section in the
   * [format] expression. If set, the image argument will check that the requested image exists in
   * the style and will return either the resolved image name or null, depending on whether or not
   * the image is currently in the style. This validation process is synchronous and requires the
   * image to have been added to the style before requesting it in the image argument.
   */
  public fun image(value: Expression<String>): Expression<ImageValue> = call("image", value)

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
    call(
      "number-format",
      number,
      options {
        if (locale != null) put("locale", locale)
        if (currency != null) put("currency", currency)
        if (minFractionDigits != null) put("min-fraction-digits", minFractionDigits)
        if (maxFractionDigits != null) put("max-fraction-digits", maxFractionDigits)
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
  public fun toString(value: Expression<*>): Expression<String> = call("to-string", value)

  /**
   * Converts the input value to a number, if possible. If the input is null or false, the result
   * is 0. If the input is true, the result is 1. If the input is a string, it is converted to a
   * number as specified by the "ToNumber Applied to the String Type" algorithm of the ECMAScript
   * Language Specification. If multiple values are provided, each one is evaluated in order until
   * the first successful conversion is obtained. If none of the inputs can be converted, the
   * expression is an error.
   */
  public fun toNumber(value: Expression<*>, vararg fallbacks: Expression<*>): Expression<Number> =
    call("to-number", value, *fallbacks)

  /**
   * Converts the input value to a boolean. The result is false when then input is an empty string,
   * 0, false, null, or NaN; otherwise it is true.
   */
  public fun toBoolean(value: Expression<*>): Expression<Boolean> = call("to-boolean", value)

  /**
   * Converts the input value to a color. If multiple values are provided, each one is evaluated in
   * order until the first successful conversion is obtained. If none of the inputs can be
   * converted, the expression is an error.
   */
  public fun toColor(
    value: Expression<*>,
    vararg fallbacks: Expression<*>,
  ): Expression<ColorValue> = call("to-color", value, *fallbacks)

  // TODO above are in the right order from the docs, below are not

  public fun <Input : Number, Output> interpolate(
    type: Expression<InterpolationValue>,
    input: Expression<Input>,
    vararg stops: Pair<Input, Expression<Output>>,
  ): Expression<Output> {
    val args =
      stops.sortedBy { it.first.toDouble() }.flatMap { listOf(literal(it.first), it.second) }
    return call("interpolate", type, input, *args.toTypedArray())
  }

  public fun linear(): Expression<InterpolationValue> = call("linear")

  public fun exponential(base: Number): Expression<InterpolationValue> =
    call("exponential", literal(base))

  public fun cubicBezier(
    x1: Number,
    y1: Number,
    x2: Number,
    y2: Number,
  ): Expression<InterpolationValue> =
    call("cubic-bezier", literal(x1), literal(y1), literal(x2), literal(y2))

  public fun zoom(): Expression<Number> = call("zoom")

  public fun rgba(red: UByte, green: UByte, blue: UByte, alpha: Float): Expression<ColorValue> =
    call(
      "rgba",
      literal(red.toShort()),
      literal(green.toShort()),
      literal(blue.toShort()),
      literal(alpha),
    )

  public fun rgb(red: UByte, green: UByte, blue: UByte): Expression<ColorValue> =
    call("rgb", literal(red.toShort()), literal(green.toShort()), literal(blue.toShort()))

  public fun color(value: UInt): Expression<ColorValue> =
    rgba(
      (value shr 16).toUByte(),
      (value shr 8).toUByte(),
      value.toUByte(),
      ((value shr 24) and 0xFFu).toFloat() / 255f,
    )

  // utils below

  @Suppress("UNCHECKED_CAST")
  private fun <Return> call(function: String, vararg args: Expression<*>) =
    Expression(listOf(literal(function), *args)) as Expression<Return>

  private inline fun options(block: MutableMap<String, Expression<*>>.() -> Unit) =
    Expression(mutableMapOf<String, Expression<*>>().apply(block))
}

public fun <T> useExpressions(block: ExpressionDsl.() -> T): T = block(ExpressionDsl)

public class Expression<T> private constructor(internal val value: Any?) {
  public companion object {
    public operator fun invoke(string: String): Expression<String> = Expression(string)

    public operator fun invoke(number: Number): Expression<Number> = Expression(number)

    public operator fun invoke(bool: Boolean): Expression<Boolean> = Expression(bool)

    public operator fun invoke(nil: Nothing? = null): Expression<Nothing?> = Expression(nil)

    public operator fun invoke(list: List<Expression<*>>): Expression<List<*>> =
      Expression(list.map { it.value })

    public operator fun invoke(map: Map<String, Expression<*>>): Expression<Map<String, *>> =
      Expression(map.entries.associate { (key, value) -> key to value.value })
  }
}

// token classes for expression type safety

public class ColorValue private constructor()

public class CollatorValue private constructor()

public class FormattedValue private constructor()

public class ImageValue private constructor()

public class InterpolationValue private constructor()