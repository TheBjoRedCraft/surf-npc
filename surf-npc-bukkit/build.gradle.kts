plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-npc-core"))
    implementation("com.cjcrafter:foliascheduler:0.7.0")
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.npc.bukkit.BukkitMain")
    authors.add("red")
    foliaSupported(true)

    generateLibraryLoader(false)
}

tasks.shadowJar {
    relocate("com.cjcrafter.foliascheduler", "dev.slne.surf.npc.bukkit.libs")
}