package dev.slne.surf.npc.bukkit.controller

import com.google.auto.service.AutoService
import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.result.NpcDeletionResult
import dev.slne.surf.npc.api.result.NpcRespawnResult
import dev.slne.surf.npc.core.controller.NpcController
import dev.slne.surf.npc.core.npc.CoreNpc
import it.unimi.dsi.fastutil.objects.ObjectList
import net.kyori.adventure.util.Services.Fallback
import java.util.*

@AutoService(NpcController::class)
class BukkitNpcController(): NpcController, Fallback {
    override fun create(npc: CoreNpc): NpcCreationResult {
        TODO("Not yet implemented")
    }

    override fun remove(npc: CoreNpc): NpcDeletionResult {
        TODO("Not yet implemented")
    }

    override fun getNpc(id: UUID): CoreNpc? {
        TODO("Not yet implemented")
    }

    override fun getNpcs(): ObjectList<CoreNpc> {
        TODO("Not yet implemented")
    }

    override fun respawn(npc: CoreNpc): NpcRespawnResult {
        TODO("Not yet implemented")
    }
}