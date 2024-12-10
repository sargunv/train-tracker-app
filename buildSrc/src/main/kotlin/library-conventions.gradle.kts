plugins { id("maven-publish") }

group = "dev.sargunv.maplibre-compose"

publishing {
  repositories {
    maven {
      name = "GitHubPackages"
      setUrl("https://maven.pkg.github.com/sargunv/maplibre-compose")
      credentials {
        username = project.properties["githubUser"]?.toString()
        password = project.properties["githubToken"]?.toString()
      }
    }
  }
}
