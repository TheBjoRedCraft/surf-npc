package dev.slne.surf.npc.core.property

import dev.slne.surf.npc.api.npc.SNpcPropertyType
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet

interface PropertyTypeRegistry {
    fun register(type: SNpcPropertyType)
    fun unregister(type: SNpcPropertyType)
    fun get(id: String): SNpcPropertyType?
    fun getIds(): ObjectSet<String>


    companion object {
        /**
         * The instance of the StorageService.
         */
        val INSTANCE = requiredService<PropertyTypeRegistry>()
    }
}

/**
 * The instance of the DatabaseService
 */
val propertyTypeRegistry get() = PropertyTypeRegistry.INSTANCE