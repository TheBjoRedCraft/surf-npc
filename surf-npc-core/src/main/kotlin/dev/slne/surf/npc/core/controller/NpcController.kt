package dev.slne.surf.npc.core.controller

import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcData
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.result.NpcDeletionResult
import dev.slne.surf.npc.api.result.NpcRespawnResult
import dev.slne.surf.npc.api.result.NpcSpawnResult
import dev.slne.surf.npc.api.rotation.SNpcRotation
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.api.skin.SNpcSkinData
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.util.UUID

interface NpcController {
    fun createNpc(data: SNpcData) : NpcCreationResult
    fun deleteNpc(npc: SNpc) : NpcDeletionResult

    fun registerNpc(npc: SNpc)
    fun unregisterNpc(npc: SNpc): Boolean

    fun showNpc(npc: SNpc, uuid: UUID) : NpcSpawnResult
    fun hideNpc(npc: SNpc, uuid: UUID) : NpcDeletionResult
    fun reShowNpc(npc: SNpc, uuid: UUID): NpcRespawnResult

    fun setSkin(npc: SNpc, skin: SNpcSkinData)
    fun setRotationType(npc: SNpc, rotationType: SNpcRotationType)
    fun setRotation(npc: SNpc, rotation: SNpcRotation)

    fun getNpc(id: Int): SNpc?
    fun getNpc(internalName: String): SNpc?
    fun getNpcs(): ObjectList<SNpc>
    fun despawnAllNpcs(): Int
    fun getProperties(npc: SNpc): ObjectSet<SNpcProperty<*>>
    fun addProperty(npc: SNpc, property: SNpcProperty<*>): Boolean
    fun removeProperty(npc: SNpc, key: String): Boolean


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