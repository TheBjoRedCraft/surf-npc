package dev.slne.surf.npc.api.npc

import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import net.kyori.adventure.text.Component
import org.bukkit.Location
import java.util.UUID

/**
 * Represents a non-player character (NPC) in the game.
 */
interface SNpc {
    /**
     * The unique identifier of the NPC.
     */
    val id: UUID

    /**
     * The name of the NPC.
     */
    val name: String

    /**
     * The display name of the NPC, shown to players.
     */
    val displayName: Component

    /**
     * The location of the NPC in the game world.
     */
    val location: Location

    /**
     * The skin of the NPC, represented as a string.
     */
    val skin: String

    /**
     * Adds a property to the NPC.
     *
     * @param key The key of the property.
     * @param value The value of the property.
     */
    fun addProperty(key: String, value: String)

    /**
     * Retrieves the value of a property.
     *
     * @param key The key of the property.
     * @return The value of the property.
     */
    fun getProperty(key: String): String

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

    /**
     * Retrieves all properties of the NPC.
     *
     * @return A map containing all properties of the NPC.
     */
    fun getProperties(): Object2ObjectMap<String, String>
}