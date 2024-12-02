package dev.sargunv.maplibrecompose.core

public data class GestureSettings(
  val isTiltGesturesEnabled: Boolean = true,
  val isZoomGesturesEnabled: Boolean = true,
  val isRotateGesturesEnabled: Boolean = true,
  val isScrollGesturesEnabled: Boolean = true,
)
