package dev.slne.surf.npc.bukkit.property

import com.google.auto.service.AutoService
import dev.slne.surf.npc.api.npc.SNpcPropertyType
import dev.slne.surf.npc.core.property.PropertyTypeRegistry
import net.kyori.adventure.util.Services

@AutoService(PropertyTypeRegistry::class)
class BukkitPropertyTypeRegistry : PropertyTypeRegistry, Services.Fallback {
    private val types = mutableMapOf<Class<*>, SNpcPropertyType<*>>()

    override fun <T : Any> register(type: SNpcPropertyType<T>) {
        types[type.classType] = type
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(clazz: Class<T>): SNpcPropertyType<T>? {
        return types[clazz] as? SNpcPropertyType<T>
    }
}

