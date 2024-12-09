package dev.sargunv.maplibrecompose.core.util

import android.content.Context
import android.os.Build
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

public actual object PlatformUtils {
  @Composable
  public actual fun getSystemRefreshRate(): Float {
    val context = LocalContext.current
    val display =
      if (Build.VERSION.SDK_INT >= 30) context.display
      else
        @Suppress("DEPRECATION")
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    return display?.refreshRate ?: 0f
  }
}
