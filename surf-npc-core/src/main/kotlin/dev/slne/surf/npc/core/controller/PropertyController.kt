import dev.slne.surf.npc.core.npc.CoreNpc
import it.unimi.dsi.fastutil.objects.Object2ObjectMap

/**
 * Interface for managing properties of NPCs.
 */
interface PropertyController {

    /**
     * Checks if the specified NPC has a property with the given key.
     *
     * @param npc The NPC to check.
     * @param key The key of the property to check for.
     * @return True if the NPC has the property, false otherwise.
     */
    fun hasProperty(npc: CoreNpc, key: String): Boolean

    /**
     * Retrieves the value of the property with the given key for the specified NPC.
     *
     * @param npc The NPC to retrieve the property from.
     * @param key The key of the property to retrieve.
     * @return The value of the property.
     */
    fun getProperty(npc: CoreNpc, key: String): String

    /**
     * Adds a property with the given key and value to the specified NPC.
     *
     * @param npc The NPC to add the property to.
     * @param key The key of the property to add.
     * @param value The value of the property to add.
     */
    fun addProperty(npc: CoreNpc, key: String, value: String)

    /**
     * Removes the property with the given key from the specified NPC.
     *
     * @param npc The NPC to remove the property from.
     * @param key The key of the property to remove.
     */
    fun removeProperty(npc: CoreNpc, key: String)

    /**
     * Sets the value of the property with the given key for the specified NPC.
     *
     * @param npc The NPC to set the property for.
     * @param key The key of the property to set.
     * @param value The new value of the property.
     */
    fun setProperty(npc: CoreNpc, key: String, value: String)

    /**
     * Retrieves all properties of the specified NPC.
     *
     * @param npc The NPC to retrieve the properties from.
     * @return A map containing all properties of the NPC.
     */
    fun getProperties(npc: CoreNpc): Object2ObjectMap<String, String>

    /**
     * Clears all properties of the specified NPC.
     *
     * @param npc The NPC to clear the properties of.
     */
    fun clearProperties(npc: CoreNpc)

    /**
     * Checks if the specified NPC has any properties.
     *
     * @param npc The NPC to check.
     * @return True if the NPC has any properties, false otherwise.
     */
    fun hasProperties(npc: CoreNpc): Boolean
}