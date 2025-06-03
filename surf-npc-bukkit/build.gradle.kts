plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-npc-core"))
    api(libs.surf.database)

    compileOnly(libs.packetevents.spigot)
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.npc.bukkit.SurfNpcBukkit")
    authors.add("red")

    generateLibraryLoader(false)
}