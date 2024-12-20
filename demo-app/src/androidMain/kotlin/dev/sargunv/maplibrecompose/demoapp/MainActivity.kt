package dev.sargunv.maplibrecompose.demoapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.maplibre.android.MapLibre
import org.maplibre.android.module.http.HttpRequestUtil

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    MapLibre.getInstance(this)
    HttpRequestUtil.setLogEnabled(false)
    enableEdgeToEdge()
    setContent { DemoApp() }
  }
}

@Composable
actual fun getDefaultColorScheme(isDark: Boolean): ColorScheme {
  val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
  return when {
    dynamicColor && isDark -> dynamicDarkColorScheme(LocalContext.current)
    dynamicColor && !isDark -> dynamicLightColorScheme(LocalContext.current)
    isDark -> darkColorScheme()
    else -> lightColorScheme()
  }
}
