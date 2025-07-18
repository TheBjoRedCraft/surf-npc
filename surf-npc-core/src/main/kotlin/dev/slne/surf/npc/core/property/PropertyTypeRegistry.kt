package dev.slne.surf.npc.core.property

import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet

interface PropertyTypeRegistry {
    fun register(type: NpcPropertyType)
    fun unregister(type: NpcPropertyType)
    fun get(id: String): NpcPropertyType?
    fun getIds(): ObjectSet<String>


    companion object {
        /**
         * The instance of the PropertyTypeRegistry.
         */
        val INSTANCE = requiredService<PropertyTypeRegistry>()
    }
}

/**
 * The instance of the PropertyTypeRegistry
 */
val propertyTypeRegistry get() = PropertyTypeRegistry.INSTANCE