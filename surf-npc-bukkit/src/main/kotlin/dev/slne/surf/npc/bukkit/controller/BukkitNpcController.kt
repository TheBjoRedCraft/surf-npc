package dev.slne.surf.npc.bukkit.controller

import com.google.auto.service.AutoService
import dev.slne.surf.npc.api.event.NpcCreateEvent
import dev.slne.surf.npc.api.event.NpcDeleteEvent
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
import dev.slne.surf.npc.bukkit.npc.BukkitSNpc
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.core.controller.NpcController
import dev.slne.surf.surfapi.bukkit.api.util.forEachPlayer
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.random
import dev.slne.surf.surfapi.core.api.util.toObjectList
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.util.Services
import org.bukkit.Bukkit
import java.util.UUID

@AutoService(NpcController::class)
class BukkitNpcController : NpcController, Services.Fallback {
    val npcs = mutableObjectSetOf<SNpc>()

    override fun createNpc(data: SNpcData): NpcCreationResult {
        val id = random.nextInt()
        val nameTagId = random.nextInt()
        val uuid = UUID.randomUUID()
        val nameTagUuid = UUID.randomUUID()

        if(this.getNpc(id) != null) {
            return NpcCreationResult.FAILED_ALREADY_EXISTS
        }

        if(this.getNpc(data.internalName) != null) {
            return NpcCreationResult.FAILED_ALREADY_EXISTS
        }

        val npc = BukkitSNpc (
            id,
            data,
            mutableObjectSetOf<SNpcProperty>(),
            mutableObjectSetOf<UUID>(),
            uuid,
            nameTagId,
            nameTagUuid
        )

        this.registerNpc(npc)

        if(npc.data.global) {
            forEachPlayer {
                npc.spawn(it.uniqueId)
            }
        }

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            Bukkit.getPluginManager().callEvent(NpcCreateEvent (
                npc
            ))
        }, 1L)



        return NpcCreationResult.SUCCESS
    }

    override fun deleteNpc(npc: SNpc): NpcDeletionResult {
        if(!this.unregisterNpc(npc)) {
            return NpcDeletionResult.FAILED_NOT_FOUND
        }

        if(npc.data.global) {
            forEachPlayer { player ->
                npc.despawn(player.uniqueId)
            }
        } else {
            npc.viewers.forEach { viewer ->
                npc.despawn(viewer)
            }
        }

        npc.clearProperties()
        npc.viewers.clear()

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            Bukkit.getPluginManager().callEvent(NpcDeleteEvent (
                npc
            ))
        }, 1L)

        return NpcDeletionResult.SUCCESS
    }

    override fun registerNpc(npc: SNpc) {
        npcs.add(npc)
    }

    override fun unregisterNpc(npc: SNpc): Boolean {
        return npcs.remove(npc)
    }

    override fun showNpc(
        npc: SNpc,
        uuid: UUID
    ): NpcSpawnResult {
        if(!npcs.contains(npc)) {
            return NpcSpawnResult.FAILED_NOT_EXIST
        }

        if(npc.viewers.contains(uuid)) {
            return NpcSpawnResult.FAILED_ALREADY_SPAWNED
        }

        npc.spawn(uuid)

        return NpcSpawnResult.SUCCESS
    }

    override fun hideNpc(
        npc: SNpc,
        uuid: UUID
    ): NpcDeletionResult {
        if(!npcs.contains(npc)) {
            return NpcDeletionResult.FAILED_NOT_FOUND
        }

        if(!npc.viewers.contains(uuid)) {
            return NpcDeletionResult.FAILED_NOT_SPAWNED
        }

        npc.despawn(uuid)

        return NpcDeletionResult.SUCCESS
    }

    override fun reShowNpc(
        npc: SNpc,
        uuid: UUID
    ): NpcRespawnResult {
        if(!npcs.contains(npc)) {
            return NpcRespawnResult.FAILED_NOT_EXIST
        }

        if(npc.viewers.contains(uuid)) {
            return NpcRespawnResult.FAILED_ALREADY_SPAWNED
        }

        npc.despawn(uuid)
        npc.spawn(uuid)

        return NpcRespawnResult.SUCCESS
    }

    override fun setSkin(
        npc: SNpc,
        skin: SNpcSkinData
    ) {
        npc.data.skin = skin
    }

    override fun setRotationType(
        npc: SNpc,
        rotationType: SNpcRotationType
    ) {
        npc.data.rotationType = rotationType
    }

    override fun setRotation(
        npc: SNpc,
        rotation: SNpcRotation
    ) {
        npc.data.fixedRotation = rotation
    }

    override fun getNpc(id: Int): SNpc? {
        return npcs.find { it.id == id }
    }

    override fun getNpc(internalName: String): SNpc? {
        return npcs.find { it.data.internalName.trim().lowercase() == internalName.trim().lowercase() }
    }

    override fun getNpcs(): ObjectList<SNpc> {
        return npcs.toObjectList()
    }

    override fun despawnAllNpcs(): Int {
        val count = npcs.size

        npcs.forEach {
            if(it.data.global) {
                forEachPlayer { player ->
                    it.despawn(player.uniqueId)
                }
            } else {
                it.viewers.forEach { viewer ->
                    it.despawn(viewer)
                }
            }

            it.clearProperties()
            it.viewers.clear()
            it.delete()
        }

        npcs.clear()

        return count
    }

    override fun getProperties(npc: SNpc): ObjectSet<SNpcProperty> {
        return npc.properties
    }

    override fun addProperty(
        npc: SNpc,
        property: SNpcProperty
    ): Boolean {
        if(npc.hasProperty(property.key)) {
            return false
        }

        npc.addProperty(property)
        return true
    }
}