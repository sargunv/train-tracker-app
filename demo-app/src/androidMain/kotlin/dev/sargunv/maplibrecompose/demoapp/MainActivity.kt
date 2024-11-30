package dev.sargunv.maplibrecompose.demoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
