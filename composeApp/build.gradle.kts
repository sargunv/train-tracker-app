@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.application)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.kotlin.cocoapods)
  alias(libs.plugins.serialization)
  alias(libs.plugins.sqldelight)
  alias(libs.plugins.spotless)
  alias(libs.plugins.buildkonfig)
}

version = "0.1.0"

kotlin {
  androidTarget { compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }
  iosArm64()
  iosSimulatorArm64()
  iosX64()

  cocoapods {
    summary = "Some description for the Shared Module"
    homepage = "Link to the Shared Module homepage"
    ios.deploymentTarget = "15.3"
    podfile = project.file("../iosApp/Podfile")
    framework { baseName = "composeApp" }
    pod("zipzap", libs.versions.zipzap.get())
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
        optIn("androidx.compose.material3.ExperimentalMaterial3Api")
        optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
      }
    }
    androidMain.dependencies {
      implementation(libs.androidx.activity.compose)
      implementation(libs.koin.android)
      implementation(libs.kotlinx.coroutines.android)
      implementation(libs.kotlinx.io.core)
      implementation(libs.ktor.client.android)
      implementation(libs.sqldelight.driver.android)
    }
    iosMain.dependencies {
      implementation(libs.kotlinx.io.core)
      implementation(libs.ktor.client.darwin)
      implementation(libs.sqldelight.driver.native)
    }
    commonMain.dependencies {
      implementation(compose.components.resources)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.runtime)
      implementation(compose.ui)
      implementation(libs.androidx.lifecycle.runtime.compose)
      implementation(libs.androidx.lifecycle.viewmodel)
      implementation(libs.koin.core)
      implementation(libs.koin.compose)
      implementation(libs.koin.compose.viewmodel)
      implementation(libs.kotlinx.coroutines.core)
      implementation(libs.kotlinx.io.core)
      implementation(libs.kotlinx.serialization.core)
      implementation(libs.ktor.client.core)
      implementation(libs.sqldelight.runtime)

      implementation(project(":kotlin-csv"))
      implementation(project(":multiplatform-zip"))
      implementation(project(":maplibre-kmp"))
    }
    commonTest.dependencies { implementation(libs.kotlin.test) }
  }
}

android {
  namespace = "dev.sargunv.traintracker"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "dev.sargunv.traintracker"
    minSdk = libs.versions.android.minSdk.get().toInt()
    targetSdk = libs.versions.android.targetSdk.get().toInt()
    versionCode = 1
    versionName = "1.0"
  }
  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
  buildTypes { getByName("release") { isMinifyEnabled = false } }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}

compose.resources { packageOfResClass = "dev.sargunv.traintracker.generated" }

buildkonfig {
  packageName = "dev.sargunv.traintracker.generated"

  val props = gradleLocalProperties(rootDir, providers)
  require(props.containsKey("PROTOMAPS_KEY")) { "PROTOMAPS_KEY not found in local.properties" }

  defaultConfigs {
    buildConfigField(FieldSpec.Type.STRING, "PROTOMAPS_KEY", props.getProperty("PROTOMAPS_KEY"))
  }
}

sqldelight {
  databases {
    create("GtfsDb") {
      packageName.set("dev.sargunv.traintracker.gtfs")
      srcDirs("src/commonMain/sqldelight/GtfsDb")
      verifyMigrations.set(true)
      generateAsync.set(false)
    }
  }
}

spotless {
  kotlin {
    target("src/**/*.kt")
    ktfmt().googleStyle()
  }
}
