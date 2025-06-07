package dev.slne.surf.npc.api

import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcLocation
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.api.npc.SNpcPropertyType
import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.result.NpcDeletionResult
import dev.slne.surf.npc.api.rotation.SNpcRotation
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.api.skin.SNpcSkinData
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import java.util.UUID

/**
 * API for managing NPCs (Non-Player Characters) in the Surf framework.
 */
interface SurfNpcApi {

    /**
     * Creates a new NPC.
     *
     * @param displayName The display name of the NPC.
     * @param internalName The internal name of the NPC.
     * @param skin The skin data of the NPC.
     * @param location The location of the NPC.
     * @param global Whether the NPC is global (default: true).
     * @param rotationType The rotation type of the NPC (default: FIXED).
     * @param fixedRotation The fixed rotation of the NPC, if applicable (default: null).
     * @return The result of the NPC creation.
     */
    fun createNpc(
        displayName: Component,
        internalName: String,
        skin: SNpcSkinData,
        location: SNpcLocation,
        global: Boolean = true,
        rotationType: SNpcRotationType = SNpcRotationType.FIXED,
        fixedRotation: SNpcRotation? = null
    ): NpcCreationResult

    /**
     * Deletes an existing NPC.
     *
     * @param npc The NPC to delete.
     * @return The result of the NPC deletion.
     */
    fun deleteNpc(npc: SNpc): NpcDeletionResult

    /**
     * Shows an NPC to a specific player.
     *
     * @param npc The NPC to show.
     * @param uuid The UUID of the player.
     */
    fun showNpc(npc: SNpc, uuid: UUID)

    /**
     * Hides an NPC from a specific player.
     *
     * @param npc The NPC to hide.
     * @param uuid The UUID of the player.
     */
    fun hideNpc(npc: SNpc, uuid: UUID)

    /**
     * Sets the skin of an NPC.
     *
     * @param npc The NPC to update.
     * @param skin The new skin data.
     */
    fun setSkin(npc: SNpc, skin: SNpcSkinData)

    /**
     * Sets the rotation type of an NPC.
     *
     * @param npc The NPC to update.
     * @param rotationType The new rotation type.
     */
    fun setRotationType(npc: SNpc, rotationType: SNpcRotationType)

    /**
     * Sets the rotation of an NPC.
     *
     * @param npc The NPC to update.
     * @param rotation The new rotation.
     */
    fun setRotation(npc: SNpc, rotation: SNpcRotation)

    /**
     * Retrieves the properties of an NPC.
     *
     * @param npc The NPC whose properties are to be retrieved.
     * @return A set of properties associated with the NPC.
     */
    fun getProperties(npc: SNpc): ObjectSet<SNpcProperty>

    /**
     * Adds a property to an NPC.
     *
     * @param npc The NPC to update.
     * @param property The property to add.
     * @return True if the property was added, false otherwise.
     */
    fun addProperty(npc: SNpc, property: SNpcProperty): Boolean

    /**
     * Removes a property from an NPC.
     *
     * @param npc The NPC to update.
     * @param property The property to remove.
     * @return True if the property was removed, false otherwise.
     */
    fun removeProperty(npc: SNpc, property: SNpcProperty): Boolean

    /**
     * Creates a new property for an NPC.
     *
     * @param key The key of the property.
     * @param value The value of the property.
     * @param type The type of the property.
     * @return The created property.
     */
    fun createProperty(
        key: String,
        value: String,
        type: SNpcPropertyType
    ): SNpcProperty

    /**
     * Retrieves an NPC by its ID.
     *
     * @param id The ID of the NPC.
     * @return The NPC with the specified ID, or null if not found.
     */
    fun getNpc(id: Int): SNpc?

    /**
     * Retrieves an NPC by its internal name.
     *
     * @param internalName The internal name of the NPC.
     * @return The NPC with the specified internal name, or null if not found.
     */
    fun getNpc(internalName: String): SNpc?

    /**
     * Retrieves all NPCs.
     *
     * @return A list of all NPCs.
     */
    fun getNpcs(): ObjectList<SNpc>

    /**
     * Despawns all NPCs.
     */
    fun despawnAllNpcs()

    /**
     * Creates a new rotation.
     *
     * @param yaw The yaw value of the rotation.
     * @param pitch The pitch value of the rotation.
     * @return The created rotation.
     */
    fun createRotation(
        yaw: Float,
        pitch: Float
    ): SNpcRotation

    /**
     * Creates new skin data.
     *
     * @param owner The owner of the skin.
     * @param value The value of the skin.
     * @param signature The signature of the skin.
     * @return The created skin data.
     */
    fun createSkinData(
        owner: String,
        value: String,
        signature: String
    ): SNpcSkinData

    /**
     * Creates a new location.
     *
     * @param x The x-coordinate of the location.
     * @param y The y-coordinate of the location.
     * @param z The z-coordinate of the location.
     * @param worldName The name of the world.
     * @return The created location.
     */
    fun createLocation(
        x: Double,
        y: Double,
        z: Double,
        worldName: String
    ): SNpcLocation

    companion object {
        /**
         * The instance of the SurfNpcApi.
         */
        val INSTANCE = requiredService<SurfNpcApi>()
    }
}

/**
 * A shortcut to access the SurfNpcApi instance.
 */
val surfNpcApi get() = SurfNpcApi.INSTANCE