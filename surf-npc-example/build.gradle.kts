plugins {
    kotlin("jvm")
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.retrooper:packetevents-spigot:2.7.0")
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.npc.example.ExamplePlugin")
    authors.add("TheBjoRedCraft")
}



kotlin {
    jvmToolchain(21)
}