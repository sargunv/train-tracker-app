@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.cocoapods)
  alias(libs.plugins.android.library)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.spotless)
}

version = "0.1.0"

kotlin {
  explicitApi()

  androidTarget { compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }
  iosArm64()
  iosSimulatorArm64()
  iosX64()

  cocoapods {
    noPodspec()
    ios.deploymentTarget = "12.0"
    pod("MapLibre", libs.versions.maplibre.ios.get())
  }

  sourceSets {
    all {
      compilerOptions {
        freeCompilerArgs.apply {
          add("-Xexpect-actual-classes")
          add("-Xconsistent-data-class-copy-visibility")
        }
      }
      languageSettings {
        optIn("androidx.compose.ui.ExperimentalComposeUiApi")
        optIn("kotlinx.cinterop.ExperimentalForeignApi")
      }
    }
    androidMain.dependencies {
      implementation(libs.maplibre.android)
      implementation(libs.maplibre.android.plugin.annotation)
    }
    commonMain.dependencies {
      implementation(compose.runtime)
      implementation(compose.foundation)
      api(libs.kermit)
      api(compose.ui)
      api(libs.spatialk.geojson)
    }
  }
}

android {
  namespace = "dev.sargunv.maplibrekmp"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}

spotless {
  kotlin {
    target("src/**/*.kt")
    ktfmt().googleStyle()
  }
}
