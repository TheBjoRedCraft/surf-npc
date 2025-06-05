plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-npc-core"))
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.npc.bukkit.SurfNpcBukkit")
    authors.add("red")

    generateLibraryLoader(false)
}