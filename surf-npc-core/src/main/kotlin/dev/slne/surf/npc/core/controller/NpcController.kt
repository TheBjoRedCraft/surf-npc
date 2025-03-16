package dev.slne.surf.npc.core.controller

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
    fun create(npc: CoreNpc): NpcCreationResult

    /**
     * Removes an existing NPC.
     *
     * @param npc The NPC to remove.
     * @return The result of the NPC deletion.
     */
    fun remove(npc: CoreNpc): NpcDeletionResult

    /**
     * Retrieves an NPC by its ID.
     *
     * @param id The ID of the NPC.
     * @return The NPC with the specified ID, or null if not found.
     */
    fun getNpc(id: UUID): CoreNpc?

    /**
     * Retrieves all NPCs.
     *
     * @return A list of all NPCs.
     */
    fun getNpcs(): ObjectList<CoreNpc>

    /**
     * Spawns an NPC.
     *
     * @param npc The NPC to spawn.
     * @return The result of the NPC spawn.
     */
    fun spawn(npc: CoreNpc): NpcSpawnResult

    /**
     * Respawns an NPC.
     *
     * @param npc The NPC to respawn.
     * @return The result of the NPC respawn.
     */
    fun respawn(npc: CoreNpc): NpcRespawnResult

    /**
     * Despawns an NPC.
     *
     * @param npc The NPC to despawn.
     * @return The result of the NPC despawn.
     */
    fun despawn(npc: CoreNpc): NpcDespawnResult

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