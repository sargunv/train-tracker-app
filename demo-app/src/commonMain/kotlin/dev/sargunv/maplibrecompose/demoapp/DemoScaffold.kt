package dev.sargunv.maplibrecompose.demoapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DemoScaffold(
  title: String,
  navigateUp: (() -> Unit)?,
  alpha: Float = 0.7f,
  content: @Composable (PaddingValues) -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        colors =
          TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = alpha)
          ),
        title = { Text(title) },
        navigationIcon = {
          if (navigateUp != null) {
            IconButton(onClick = navigateUp) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back button",
              )
            }
          }
        },
      )
    },
    content = content,
  )
}

@Composable
fun SimpleDemoScaffold(demo: Demo, navigateUp: (() -> Unit)?, content: @Composable () -> Unit) {
  DemoScaffold(demo.name, navigateUp) {
    Box(modifier = Modifier.padding(it).consumeWindowInsets(it)) { content() }
  }
}

@Composable
fun SimpleScaffold(title: String, navigateUp: (() -> Unit)?, content: @Composable () -> Unit) {
  DemoScaffold(title, navigateUp) {
    Box(modifier = Modifier.padding(it).consumeWindowInsets(it)) { content() }
  }
}
