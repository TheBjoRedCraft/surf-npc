package dev.slne.surf.npc.core.controller

import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.result.NpcDeletionResult
import dev.slne.surf.npc.api.result.NpcRespawnResult
import dev.slne.surf.npc.core.npc.CoreNpc
import it.unimi.dsi.fastutil.objects.ObjectList
import java.util.*

interface NpcController {
    fun create(npc: CoreNpc): NpcCreationResult
    fun remove(npc: CoreNpc): NpcDeletionResult
    fun getNpc(id: UUID): CoreNpc?
    fun getNpcs(): ObjectList<CoreNpc>

    fun respawn(npc: CoreNpc): NpcRespawnResult
}