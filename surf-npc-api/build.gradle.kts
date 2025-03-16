plugins {
    kotlin("jvm")
    id("dev.slne.surf.surfapi.gradle.core")
}

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly(libs.paper)
}

kotlin {
    jvmToolchain(21)
}