plugins {
    kotlin("jvm")
    id("dev.slne.surf.surfapi.gradle.core")
}

repositories {
    mavenCentral()

}

dependencies {
    api(project(":surf-npc-api"))

    compileOnly(libs.paper)
}

kotlin {
    jvmToolchain(21)
}