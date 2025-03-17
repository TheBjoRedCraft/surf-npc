package dev.slne.surf.npc.core.controller

import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.result.*
import dev.slne.surf.npc.core.npc.CoreNpc
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectList
import java.util.*

interface NpcController {
    /**
     * Creates a new NPC.
     *
     * @param npc The NPC to create.
     * @return The result of the NPC creation.
     */
    fun create(npc: SNpc): NpcCreationResult

    /**
     * Removes an existing NPC.
     *
     * @param npc The NPC to remove.
     * @return The result of the NPC deletion.
     */
    fun remove(npc: SNpc): NpcDeletionResult

    /**
     * Retrieves an NPC by its ID.
     *
     * @param id The ID of the NPC.
     * @return The NPC with the specified ID, or null if not found.
     */
    fun getNpc(id: UUID): SNpc?

    /**
     * Retrieves an NPC by its entity ID.
     *
     * @param entityId The entity ID of the NPC.
     * @return The NPC with the specified entity ID, or null if not found.
     */
    fun getNpc(entityId: Int): SNpc?

    /**
     * Retrieves an NPC by its name.
     *
     * @param name The name of the NPC.
     * @return The NPC with the specified name, or null if not found.
     */
    fun getNpc(name: String): SNpc?

    /**
     * Retrieves all NPCs.
     *
     * @return A list of all NPCs.
     */
    fun getNpcs(): ObjectList<SNpc>

    /**
     * Spawns an NPC.
     *
     * @param npc The NPC to spawn.
     * @return The result of the NPC spawn.
     */
    fun spawn(npc: SNpc): NpcSpawnResult

    /**
     * Respawns an NPC.
     *
     * @param npc The NPC to respawn.
     * @return The result of the NPC respawn.
     */
    fun respawn(npc: SNpc): NpcRespawnResult

    /**
     * Despawns an NPC.
     *
     * @param npc The NPC to despawn.
     * @return The result of the NPC despawn.
     */
    fun despawn(npc: SNpc): NpcDespawnResult

    companion object {
        /**
         * The instance of the NpcController.
         */
        val INSTANCE = requiredService<NpcController>()
    }
}

/**
 * The instance of the NpcController
 */
val npcController get() = NpcController.INSTANCE