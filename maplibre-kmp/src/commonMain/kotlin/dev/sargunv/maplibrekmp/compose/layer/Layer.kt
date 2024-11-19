package dev.sargunv.maplibrekmp.compose.layer

/** Frame of reference for offsetting geometry */
public object TranslateAnchor {
  /** Offset is relative to the map */
  public const val Map: String = "map"

  /** Offset is relative to the viewport */
  public const val Viewport: String = "viewport"
}
