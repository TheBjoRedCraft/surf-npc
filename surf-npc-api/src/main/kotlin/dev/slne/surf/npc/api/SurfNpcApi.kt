package dev.slne.surf.npc.api

import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectList
import java.util.UUID

interface SurfNpcApi {
    fun getNpc(id: UUID): SNpc?
    fun getNpcs(): ObjectList<SNpc>
    fun spawnNpc(npc: SNpc)
    fun despawnNpc(npc: SNpc)
    fun despawnAllNpcs()

    companion object {
        /**
         * The instance of the SurfNpcApi.
         */
        val INSTANCE = requiredService<SurfNpcApi>()
    }
}

val surfNpcApi get() = SurfNpcApi.INSTANCE