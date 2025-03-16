package dev.slne.surf.npc.api

import dev.slne.surf.npc.api.npc.SNpc
import it.unimi.dsi.fastutil.objects.ObjectList
import java.util.UUID

interface SurfNpcApi {
    fun getNpc(id: UUID): SNpc?
    fun getNpcs(): ObjectList<SNpc>
    fun spawnNpc(npc: SNpc)
    fun despawnNpc(npc: SNpc)
    fun despawnAllNpcs()
}