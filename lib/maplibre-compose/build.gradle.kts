@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
  id(libs.plugins.kotlin.multiplatform.get().pluginId)
  id(libs.plugins.kotlin.cocoapods.get().pluginId)
  id(libs.plugins.android.library.get().pluginId)
  id(libs.plugins.compose.get().pluginId)
  id(libs.plugins.kotlin.composeCompiler.get().pluginId)
  id(libs.plugins.mavenPublish.get().pluginId)
  id("library-conventions")
}

android {
  namespace = "dev.sargunv.maplibrecompose"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.android.minSdk.get().toInt()
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  @Suppress("UnstableApiUsage") testOptions { animationsDisabled = true }
}

kotlin {
  explicitApi()

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
    ios.deploymentTarget = "12.0"
    pod("MapLibre", libs.versions.maplibre.ios.get())
  }

  compilerOptions {
    allWarningsAsErrors = true
    freeCompilerArgs.addAll("-Xexpect-actual-classes", "-Xconsistent-data-class-copy-visibility")
  }

  sourceSets {
    listOf(iosMain, iosArm64Main, iosSimulatorArm64Main, iosX64Main).forEach {
      it { languageSettings { optIn("kotlinx.cinterop.ExperimentalForeignApi") } }
    }

    commonMain.dependencies {
      api(compose.runtime)
      api(compose.foundation)
      api(compose.ui)
      api(libs.kermit)
      api(libs.spatialk.geojson)
    }

    androidMain.dependencies { api(libs.maplibre.android) }

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

composeCompiler { reportsDestination = layout.buildDirectory.dir("compose/reports") }

mavenPublishing {
  pom {
    name = "MapLibre Compose"
    description = "Add interactive vector tile maps to your Compose app"
    url = "https://github.com/sargunv/maplibre-compose"
  }
}
