package dev.slne.surf.npc.core.npc

import PropertyController
import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcProperty
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import org.bukkit.Location
import java.util.*

class CoreNpc (
    override val id: UUID,
    override val name: String,
    override val displayName: Component,
    override val location: Location,
    override val skin: String,
    override val properties: ObjectSet<SNpcProperty>
) : SNpc {
    override fun addProperty(key: String, value: String) {
        PropertyController.INSTANCE.addProperty(this, key, value)
    }

    override fun getProperty(key: String): String {
        return PropertyController.INSTANCE.getProperty(this, key)
    }

    override fun removeProperty(key: String) {
        PropertyController.INSTANCE.removeProperty(this, key)
    }

    override fun hasProperty(key: String): Boolean {
        return PropertyController.INSTANCE.hasProperty(this, key)
    }

    override fun clearProperties() {
        PropertyController.INSTANCE.clearProperties(this)
    }

    override fun hasProperties(): Boolean {
        return PropertyController.INSTANCE.hasProperties(this)
    }

    override fun getProperties(): Object2ObjectMap<String, String> {
        return PropertyController.INSTANCE.getProperties(this)
    }
}