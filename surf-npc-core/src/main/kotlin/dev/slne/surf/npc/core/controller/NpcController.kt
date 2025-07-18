package dev.slne.surf.npc.core.controller

import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.result.NpcDeletionResult
import dev.slne.surf.npc.api.result.NpcRespawnResult
import dev.slne.surf.npc.api.result.NpcSpawnResult
import dev.slne.surf.npc.api.npc.rotation.NpcRotation
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import java.util.UUID

interface NpcController {
    fun createNpc(internalName: String, displayName: Component, skinData: NpcSkin, location: NpcLocation, rotationType: NpcRotationType, rotation: NpcRotation, global: Boolean) : NpcCreationResult
    fun deleteNpc(npc: Npc) : NpcDeletionResult

    fun registerNpc(npc: Npc)
    fun unregisterNpc(npc: Npc): Boolean

    fun showNpc(npc: Npc, uuid: UUID) : NpcSpawnResult
    fun hideNpc(npc: Npc, uuid: UUID) : NpcDeletionResult
    fun reShowNpc(npc: Npc, uuid: UUID): NpcRespawnResult

    fun setSkin(npc: Npc, skin: NpcSkin)
    fun setRotationType(npc: Npc, rotationType: NpcRotationType)
    fun setRotation(npc: Npc, rotation: NpcRotation)

    fun getNpc(id: Int): Npc?
    fun getNpc(internalName: String): Npc?
    fun getNpcs(): ObjectList<Npc>
    fun despawnAllNpcs(): Int
    fun getProperties(npc: Npc): ObjectSet<NpcProperty>
    fun addProperty(npc: Npc, property: NpcProperty): Boolean
    fun removeProperty(npc: Npc, key: String): Boolean


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