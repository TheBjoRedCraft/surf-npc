plugins {
    kotlin("jvm")
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":surf-npc-core"))
    api(libs.surf.database)

    implementation(libs.mccoroutine.folia.api)
    implementation(libs.mccoroutine.folia.core)

    compileOnly(libs.packetevents)
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.npc.bukkit.SurfNpcBukkit")
    authors.add("red")

    generateLibraryLoader(false)
}

kotlin {
    jvmToolchain(21)
}