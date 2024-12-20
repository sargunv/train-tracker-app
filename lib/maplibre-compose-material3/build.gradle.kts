@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
  id("library-conventions")
  id("android-library-conventions")
  id(libs.plugins.kotlin.multiplatform.get().pluginId)
  id(libs.plugins.kotlin.cocoapods.get().pluginId)
  id(libs.plugins.android.library.get().pluginId)
  id(libs.plugins.compose.get().pluginId)
  id(libs.plugins.mavenPublish.get().pluginId)
}

android { namespace = "dev.sargunv.maplibrecompose.material3" }

mavenPublishing {
  pom {
    name = "MapLibre Compose Material 3"
    description = "Material 3 extensions for MapLibre Compose."
    url = "https://github.com/sargunv/maplibre-compose"
  }
}

kotlin {
  androidTarget {
    compilerOptions { jvmTarget.set(JvmTarget.JVM_11) }
    instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    publishLibraryVariants("release", "debug")
  }
  iosArm64()
  iosSimulatorArm64()
  iosX64()

  cocoapods {
    noPodspec()
    ios.deploymentTarget = project.properties["iosDeploymentTarget"]!!.toString()
    pod("MapLibre", libs.versions.maplibre.ios.get())
  }

  sourceSets {
    commonMain.dependencies {
      implementation(compose.material3)
      implementation(compose.components.resources)
      api(project(":lib:maplibre-compose"))
    }

    commonTest.dependencies {
      implementation(kotlin("test"))
      implementation(kotlin("test-common"))
      implementation(kotlin("test-annotations-common"))
      @OptIn(ExperimentalComposeLibrary::class) implementation(compose.uiTest)
    }

    androidUnitTest.dependencies { implementation(compose.desktop.currentOs) }

    androidInstrumentedTest.dependencies {
      implementation(compose.desktop.uiTestJUnit4)
      implementation(libs.androidx.composeUi.testManifest)
    }
  }
}

compose.resources { packageOfResClass = "dev.sargunv.maplibrecompose.material3.generated" }
