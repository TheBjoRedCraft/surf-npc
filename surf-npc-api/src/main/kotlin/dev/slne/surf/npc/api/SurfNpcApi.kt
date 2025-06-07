package dev.slne.surf.npc.api

import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcLocation
import dev.slne.surf.npc.api.npc.SNpcProperty
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

interface SurfNpcApi {
    fun createNpc(displayName: Component,
                  internalName: String,
                  skin: SNpcSkinData,
                  location: SNpcLocation,
                  global: Boolean = true,
                  rotationType: SNpcRotationType = SNpcRotationType.FIXED,
                  fixedRotation: SNpcRotation? = null
    ): NpcCreationResult
    fun deleteNpc(npc: SNpc): NpcDeletionResult

    fun showNpc(npc: SNpc, uuid: UUID)
    fun hideNpc(npc: SNpc, uuid: UUID)

    fun setSkin(npc: SNpc, skin: SNpcSkinData)
    fun setRotationType(npc: SNpc, rotationType: SNpcRotationType)
    fun setRotation(npc: SNpc, rotation: SNpcRotation)

    fun getProperties(npc: SNpc): ObjectSet<SNpcProperty>
    fun addProperty(npc: SNpc, property: SNpcProperty): Boolean
    fun removeProperty(npc: SNpc, property: SNpcProperty): Boolean

    fun getNpc(id: Int): SNpc?
    fun getNpc(internalName: String): SNpc?
    fun getNpcs(): ObjectList<SNpc>
    fun despawnAllNpcs()

    fun createRotation(
        yaw: Float,
        pitch: Float
    ): SNpcRotation

    fun createSkinData(
        owner: String,
        value: String,
        signature: String
    ): SNpcSkinData

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

val surfNpcApi get() = SurfNpcApi.INSTANCE