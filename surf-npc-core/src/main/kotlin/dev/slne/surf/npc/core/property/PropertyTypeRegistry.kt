package dev.slne.surf.npc.core.property

import dev.slne.surf.npc.api.npc.SNpcPropertyType
import dev.slne.surf.npc.core.service.StorageService
import dev.slne.surf.surfapi.core.api.util.requiredService

interface PropertyTypeRegistry {
    fun <T : Any> register(type: SNpcPropertyType<T>)
    fun <T : Any> get(clazz: Class<T>): SNpcPropertyType<T>?

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