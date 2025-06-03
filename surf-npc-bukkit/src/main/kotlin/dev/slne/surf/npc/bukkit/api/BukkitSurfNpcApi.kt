package dev.slne.surf.npc.bukkit.api

import dev.slne.surf.npc.api.SurfNpcApi
import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcData
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.api.result.NpcDeletionResult
import dev.slne.surf.npc.api.rotation.SNpcRotation
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.api.skin.SNpcSkinData
import dev.slne.surf.npc.core.controller.npcController
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import java.util.UUID

class BukkitSurfNpcApi : SurfNpcApi {
    override fun createNpc(data: SNpcData) {
        npcController.createNpc(data)
    }

    override fun deleteNpc(npc: SNpc): Boolean {
        return npcController.deleteNpc(npc) == NpcDeletionResult.SUCCESS
    }

    override fun showNpc(npc: SNpc, uuid: UUID) {
        npcController.showNpc(npc, uuid)
    }

    override fun hideNpc(npc: SNpc, uuid: UUID) {
        npcController.hideNpc(npc, uuid)
    }

    override fun setSkin(
        npc: SNpc,
        skin: SNpcSkinData
    ) {
        npcController.setSkin(npc, skin)
    }

    override fun setRotationType(
        npc: SNpc,
        rotationType: SNpcRotationType
    ) {
        npcController.setRotationType(npc, rotationType)
    }

    override fun setRotation(
        npc: SNpc,
        rotation: SNpcRotation
    ) {
        npcController.setRotation(npc, rotation)
    }

    override fun getProperties(npc: SNpc): ObjectSet<SNpcProperty> {
        return npcController.getProperties(npc)
    }

    override fun getNpc(id: Int): SNpc? {
        return npcController.getNpc(id)
    }

    override fun getNpc(name: String): SNpc? {
        return npcController.getNpc(name)
    }

    override fun getNpcs(): ObjectList<SNpc> {
        return npcController.getNpcs()
    }

    override fun despawnAllNpcs() {
        npcController.despawnAllNpcs()
    }
}