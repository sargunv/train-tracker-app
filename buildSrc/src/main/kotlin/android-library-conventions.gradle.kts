plugins { id("com.android.library") }

android {
  defaultConfig {
    minSdk = project.properties["androidMinSdk"]!!.toString().toInt()
    compileSdk = project.properties["androidCompileSdk"]!!.toString().toInt()
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  @Suppress("UnstableApiUsage") testOptions { animationsDisabled = true }
}
