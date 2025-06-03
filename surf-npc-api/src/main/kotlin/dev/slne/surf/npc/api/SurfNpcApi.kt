package dev.slne.surf.npc.api

import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcData
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.api.rotation.SNpcRotation
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.api.skin.SNpcSkinData
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import java.util.UUID

interface SurfNpcApi {
    fun createNpc(data: SNpcData)
    fun deleteNpc(npc: SNpc): Boolean

    fun showNpc(npc: SNpc, uuid: UUID)
    fun hideNpc(npc: SNpc, uuid: UUID)

    fun setSkin(npc: SNpc, skin: SNpcSkinData)
    fun setRotationType(npc: SNpc, rotationType: SNpcRotationType)
    fun setRotation(npc: SNpc, rotation: SNpcRotation)

    fun getProperties(npc: SNpc): ObjectSet<SNpcProperty>

    fun getNpc(id: Int): SNpc?
    fun getNpc(name: String): SNpc?
    fun getNpcs(): ObjectList<SNpc>
    fun despawnAllNpcs()

    companion object {
        /**
         * The instance of the SurfNpcApi.
         */
        val INSTANCE = requiredService<SurfNpcApi>()
    }
}

val surfNpcApi get() = SurfNpcApi.INSTANCE