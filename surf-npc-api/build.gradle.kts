plugins {
    kotlin("jvm")
    id("com.gradleup.shadow") version "9.0.0-beta10"
}

group = "dev.slne"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven { url = uri("https://repo.codemc.io/repository/maven-releases/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    implementation("com.github.retrooper:packetevents-spigot:2.7.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-folia-api:2.21.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-folia-core:2.21.0")
}

tasks.shadowJar {
    relocate("io.github.retrooper.packetevents", "dev.slne.surf.npc.libs")
}

kotlin {
    jvmToolchain(21)
}