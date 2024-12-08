@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import fr.brouillard.oss.jgitver.Strategies
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.composeCompiler)
  alias(libs.plugins.compose)
  alias(libs.plugins.kotlin.cocoapods)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.spotless)
  alias(libs.plugins.jgitver)
}

jgitver {
  strategy(Strategies.PATTERN)
  nonQualifierBranches("main")
}

android {
  namespace = "dev.sargunv.maplibrecompose.demoapp"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "dev.sargunv.maplibrecompose.demoapp"
    minSdk = libs.versions.android.minSdk.get().toInt()
    targetSdk = libs.versions.android.targetSdk.get().toInt()
    versionCode = 1
    versionName = project.version.toString()
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
  buildTypes { getByName("release") { isMinifyEnabled = false } }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  @Suppress("UnstableApiUsage") testOptions { animationsDisabled = true }
}

kotlin {
  androidTarget {
    compilerOptions { jvmTarget.set(JvmTarget.JVM_11) }
    instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
  }
  iosArm64()
  iosSimulatorArm64()
  iosX64()

  cocoapods {
    summary = "PLACEHOLDER SUMMARY"
    homepage = "PLACEHOLDER HOMEPAGE"
    ios.deploymentTarget = libs.versions.ios.deploymentTarget.get()
    podfile = project.file("../iosApp/Podfile")
    framework {
      baseName = "DemoApp"
      version = "0.0.0"
    }
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
      languageSettings { optIn("androidx.compose.material3.ExperimentalMaterial3Api") }
    }

    commonMain.dependencies {
      implementation(compose.components.resources)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.runtime)
      implementation(compose.ui)
      implementation(libs.androidx.navigation.compose)
      implementation(libs.ktor.client.core)
      implementation(libs.ktor.client.contentNegotiation)
      implementation(libs.ktor.serialization.kotlinxJson)
      implementation(project(":lib:maplibre-compose"))
    }

    androidMain.dependencies {
      implementation(libs.androidx.activity.compose)
      implementation(libs.ktor.client.okhttp)
    }

    iosMain.dependencies { implementation(libs.ktor.client.darwin) }

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

compose.resources { packageOfResClass = "dev.sargunv.maplibrecompose.demoapp.generated" }

composeCompiler {
  reportsDestination = layout.buildDirectory.dir("compose/reports")
  featureFlags = setOf(ComposeFeatureFlag.StrongSkipping)
}

spotless {
  kotlinGradle { ktfmt().googleStyle() }
  kotlin {
    target("src/**/*.kt")
    ktfmt().googleStyle()
  }
}
