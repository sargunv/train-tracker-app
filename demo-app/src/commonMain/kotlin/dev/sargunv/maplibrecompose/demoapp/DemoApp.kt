package dev.sargunv.maplibrecompose.demoapp

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.sargunv.maplibrecompose.demoapp.demos.AnimatedLayerDemo

interface Demo {
  val name: String
  val description: String

  @Composable fun Component(navigateUp: () -> Unit)
}

class Menu : Demo {
  override val name = "Menu"
  override val description = "Menu"

  @Composable
  override fun Component(navigateUp: () -> Unit) {
    DemoApp()
  }
}

val DEMOS =
  listOf(
    //  Demo(
    //    "Edge-to-edge",
    //    "Fill the entire screen with a map, and add padding to ornaments to place them
    // correctly.",
    //    ::EdgeToEdgeDemo
    //  ),
    //  Demo(
    //    "Style switcher",
    //    "Switch between different map styles at runtime.",
    //    { demo, navigateUp -> StyleSwitcherDemo(demo, navigateUp) }
    //  ),
    //  Demo(
    //    "Clustering and interaction",
    //    "Add points to the map and configure clustering with expressions.",
    //    { ClusteredPointsDemo() }
    //  ),
    //  Demo(
    //    "Animated layer",
    //    "Change layer properties at runtime.",
    //    { AnimatedLayerDemo() }
    //  ),
    //  Demo(
    //    "Camera state",
    //    "Read camera position as state.",
    //    { CameraStateDemo() }
    //  ),
    //  Demo(
    //    "Camera follow",
    //    "Make the camera follow a point on the map.",
    //    { CameraFollowDemo() }
    //  ),
    //  Demo(
    //    "Frame rate",
    //    "Change the frame rate of the map.",
    //    { FrameRateDemo() }
    //  ),
    AnimatedLayerDemo
  )

@Composable
fun DemoApp(navController: NavHostController = rememberNavController()) {
  MaterialTheme {
    NavHost(
      navController = navController,
      startDestination = "start",
      enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start) },
      exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start) },
      popEnterTransition = {
        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End)
      },
      popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End) },
    ) {
      composable("start") {
        SimpleScaffold("MapLibre Compose Demos", navigateUp = null) {
          Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            DEMOS.forEach { demo ->
              Column {
                ListItem(
                  modifier = Modifier.clickable(onClick = { navController.navigate(demo.name) }),
                  headlineContent = { Text(text = demo.name) },
                  supportingContent = { Text(text = demo.description) },
                )
                HorizontalDivider()
              }
            }
          }
        }
      }
      DEMOS.forEach { demo ->
        composable(demo.name) { demo.Component { navController.popBackStack() } }
      }
    }
  }
}
