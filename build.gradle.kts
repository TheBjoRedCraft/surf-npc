import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

buildscript {
    repositories {
        gradlePluginPortal()
        maven("https://repo.slne.dev/repository/maven-public/") { name = "maven-public" }
    }
    dependencies {
        classpath("dev.slne.surf:surf-api-gradle-plugin:1.21.7+")
    }
}

allprojects {
    group = "dev.slne"
    version = "1.0-SNAPSHOT"

    tasks.withType<ShadowJar> {
        archiveClassifier = ""
    }
}