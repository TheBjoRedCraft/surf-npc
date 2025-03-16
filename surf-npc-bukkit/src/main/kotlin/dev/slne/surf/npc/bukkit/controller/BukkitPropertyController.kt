package dev.slne.surf.npc.bukkit.controller

import PropertyController
import com.google.auto.service.AutoService
import dev.slne.surf.npc.core.npc.CoreNpc
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.kyori.adventure.util.Services.Fallback

@AutoService(PropertyController::class)
class BukkitPropertyController(): PropertyController, Fallback {
    override fun hasProperty(npc: CoreNpc, key: String): Boolean {
        return npc.getProperty(key).isNotBlank()
    }

    override fun getProperty(npc: CoreNpc, key: String): String {
        return npc.getProperties()[key] ?: ""
    }

    override fun addProperty(npc: CoreNpc, key: String, value: String) {
        npc.getProperties()[key] = value
    }

    override fun removeProperty(npc: CoreNpc, key: String) {
        npc.getProperties().remove(key)
    }

    override fun setProperty(npc: CoreNpc, key: String, value: String) {
        npc.getProperties()[key] = value
    }

    override fun getProperties(npc: CoreNpc): Object2ObjectMap<String, String> {
        val properties = Object2ObjectOpenHashMap<String, String>()

        npc.properties.forEach {
            properties[it.key] = it.value
        }

        return properties
    }

    override fun clearProperties(npc: CoreNpc) {
        npc.getProperties().clear()
    }

    override fun hasProperties(npc: CoreNpc): Boolean {
        return npc.getProperties().isNotEmpty()
    }
}