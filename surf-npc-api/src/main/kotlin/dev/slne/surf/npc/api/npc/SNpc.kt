package dev.slne.surf.npc.api.npc

import dev.slne.surf.npc.api.skin.SNpcSkinData
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import org.bukkit.Location
import java.util.UUID

/**
 * Represents a non-player character (NPC) in the game.
 */
interface SNpc {
    val id: Int
    val data: SNpcData
    val properties: ObjectSet<SNpcProperty>

    fun show(uuid: UUID)
    fun hide(uuid: UUID)
    fun isVisible(uuid: UUID): Boolean

    fun delete()

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