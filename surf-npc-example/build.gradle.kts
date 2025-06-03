plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    compileOnly(project(":surf-npc-api")) /* Provided by surf-npc-bukkit at runtime */
    compileOnly(libs.packetevents.spigot)
}
surfPaperPluginApi {
    mainClass("dev.slne.surf.npc.example.SurfNpcExamplePlugin")
    authors.add("red")
}