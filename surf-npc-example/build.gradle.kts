plugins {
    kotlin("jvm")
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.packetevents)
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.npc.example.ExamplePlugin")
    authors.add("red")
}



kotlin {
    jvmToolchain(21)
}