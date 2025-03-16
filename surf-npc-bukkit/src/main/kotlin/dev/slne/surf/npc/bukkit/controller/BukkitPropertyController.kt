package dev.slne.surf.npc.bukkit.controller

import PropertyController
import com.google.auto.service.AutoService
import dev.slne.surf.npc.core.npc.CoreNpc
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import net.kyori.adventure.util.Services.Fallback

@AutoService(PropertyController::class)
class BukkitPropertyController(): PropertyController, Fallback {
    override fun hasProperty(npc: CoreNpc, key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getProperty(npc: CoreNpc, key: String): String {
        TODO("Not yet implemented")
    }

    override fun addProperty(npc: CoreNpc, key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun removeProperty(npc: CoreNpc, key: String) {
        TODO("Not yet implemented")
    }

    override fun setProperty(npc: CoreNpc, key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun getProperties(npc: CoreNpc): Object2ObjectMap<String, String> {
        TODO("Not yet implemented")
    }

    override fun clearProperties(npc: CoreNpc) {
        TODO("Not yet implemented")
    }

    override fun hasProperties(npc: CoreNpc): Boolean {
        TODO("Not yet implemented")
    }
}