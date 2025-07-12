package dev.slne.surf.npc.bukkit.property

import dev.slne.surf.npc.api.npc.SNpcPropertyType

object PropertyTypeRegistry {
    private val types = mutableMapOf<Class<*>, SNpcPropertyType<*>>()

    fun <T : Any> register(type: SNpcPropertyType<T>) {
        types[type.classType] = type
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(clazz: Class<T>): SNpcPropertyType<T>? {
        return types[clazz] as? SNpcPropertyType<T>
    }
}

