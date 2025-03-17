package dev.slne.surf.npc.bukkit

import com.google.auto.service.AutoService
import dev.slne.surf.npc.api.SurfNpcApi
import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.core.controller.NpcController
import dev.slne.surf.npc.core.controller.npcController
import it.unimi.dsi.fastutil.objects.ObjectList
import net.kyori.adventure.util.Services.Fallback
import java.util.*

@AutoService(SurfNpcApi::class)
class BukkitSurfNpcApi(): SurfNpcApi, Fallback {
    override fun getNpc(id: UUID): SNpc? {
        return npcController.getNpc(id)
    }

    override fun getNpc(entityId: Int): SNpc? {
        return npcController.getNpc(entityId)
    }

    override fun getNpc(name: String): SNpc? {
        return npcController.getNpc(name)
    }

    override fun getNpcs(): ObjectList<SNpc> {
        return npcController.getNpcs()
    }

    override fun spawnNpc(npc: SNpc) {
        npcController.create(npc)
    }

    override fun despawnNpc(npc: SNpc) {
        npcController.remove(npc)
    }

    override fun despawnAllNpcs() {
        npcController.getNpcs().forEach(npcController::remove)
    }
}