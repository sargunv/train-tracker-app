package dev.sargunv.maplibrecompose.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.interop.LocalUIViewController

public actual object PlatformUtils {
  @Composable
  public actual fun getSystemRefreshRate(): Float {
    return LocalUIViewController.current.view.window?.screen?.maximumFramesPerSecond?.toFloat()
      ?: 0f
  }
}
