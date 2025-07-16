package dev.slne.surf.npc.bukkit.property

import com.google.auto.service.AutoService
import dev.slne.surf.npc.api.npc.SNpcPropertyType
import dev.slne.surf.npc.core.property.PropertyTypeRegistry
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import net.kyori.adventure.util.Services

@AutoService(PropertyTypeRegistry::class)
class BukkitPropertyTypeRegistry : PropertyTypeRegistry, Services.Fallback {
    val types = mutableObjectSetOf<SNpcPropertyType>()

    override fun register(type: SNpcPropertyType) {
        val existing = types.firstOrNull { it.id == type.id }

        if (existing != null) {
            types.remove(existing)
        }

        types.add(type)
    }

    override fun get(id: String): SNpcPropertyType? {
        return types.firstOrNull() { it.id == id}
    }
}

