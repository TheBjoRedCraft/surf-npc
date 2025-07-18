package dev.slne.surf.npc.api.npc

import dev.slne.surf.npc.api.npc.property.NpcProperty
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.bukkit.entity.Player
import java.util.UUID
import kotlin.reflect.KClass

/**
 * Represents a non-player character (NPC) in the game.
 */
interface Npc {
    val id: Int
    val internalName: String
    val npcUuid: UUID
    val nameTagId: Int
    val nameTagUuid: UUID
    val properties: Object2ObjectMap<String, NpcProperty>
    val viewers: ObjectSet<UUID>

    fun spawn(uuid: UUID)
    fun despawn(uuid: UUID)

    fun refresh()
    fun refreshRotation(uuid: UUID)

    fun delete()
    fun teleport(player: Player)

    /**
     * Adds a property to the NPC.
     *
     * @param property The property
     */
    fun addProperty(property: NpcProperty)

    /**
     * Retrieves the value of a property.
     *
     * @param key The key of the property.
     * @return The value of the property.
     */
    fun getProperty(key: String): NpcProperty?
    fun <T : Any> getPropertyValue(key: String, clazz: Class<T>): T?

    /**
     * Removes a property from the NPC.
     *
     * @param key The key of the property to remove.
     */
    fun removeProperty(key: String)

    /**
     * Checks if the NPC has a specific property.
     *
     * @param key The key of the property.
     * @return True if the property exists, false otherwise.
     */
    fun hasProperty(key: String): Boolean

    /**
     * Clears all properties from the NPC.
     */
    fun clearProperties()

    /**
     * Checks if the NPC has any properties.
     *
     * @return True if the NPC has properties, false otherwise.
     */
    fun hasProperties(): Boolean
}