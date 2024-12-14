package dev.sargunv.maplibrecompose.core.util

@RequiresOptIn(
  level = RequiresOptIn.Level.WARNING,
  message = "This API is only available on platforms using MapLibre Native.",
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
public annotation class NativeOnlyApi

@RequiresOptIn(
  level = RequiresOptIn.Level.WARNING,
  message = "This API is only available on platforms using MapLibre JS.",
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
public annotation class JsOnlyApi
