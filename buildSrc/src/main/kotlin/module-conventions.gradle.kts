import fr.brouillard.oss.jgitver.Strategies

plugins { id("fr.brouillard.oss.gradle.jgitver") }

jgitver {
  strategy(Strategies.MAVEN)
  nonQualifierBranches("main")
}
