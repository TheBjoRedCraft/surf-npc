package dev.slne.surf.npc.bukkit.controller

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.entity.data.EntityData
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType
import com.github.retrooper.packetevents.protocol.player.GameMode
import com.github.retrooper.packetevents.protocol.player.User
import com.github.retrooper.packetevents.protocol.player.UserProfile
import com.github.retrooper.packetevents.util.crypto.SignatureData
import com.github.retrooper.packetevents.wrapper.play.server.*
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo.PlayerData
import com.google.auto.service.AutoService
import dev.slne.surf.npc.api.result.*
import dev.slne.surf.npc.bukkit.database.DatabaseService
import dev.slne.surf.npc.core.controller.NpcController
import dev.slne.surf.npc.core.npc.CoreNpc
import dev.slne.surf.surfapi.core.api.util.random
import dev.slne.surf.surfapi.core.api.util.toObjectList
import it.unimi.dsi.fastutil.objects.ObjectList
import net.kyori.adventure.util.Services.Fallback
import org.bukkit.Bukkit
import org.bukkit.Location
import org.gradle.internal.extensions.stdlib.uncheckedCast
import java.util.*

@AutoService(NpcController::class)
class BukkitNpcController(): NpcController, Fallback {
    override fun create(npc: CoreNpc): NpcCreationResult {
        val spawnResult = this.spawn(npc)

        DatabaseService.getLoadedNpcs().add(npc)

        if(spawnResult == NpcSpawnResult.SUCCESS) {
            return NpcCreationResult.SUCCESS
        }

        if(spawnResult == NpcSpawnResult.FAILED_ALREADY_SPAWNED) {
            return NpcCreationResult.FAILED_ALREADY_EXISTS
        }

        if(spawnResult == NpcSpawnResult.FAILED_NO_LOCATION) {
            return NpcCreationResult.FAILED_INVALID_LOCATION
        }

        return NpcCreationResult.FAILED_OTHER
    }

    override fun remove(npc: CoreNpc): NpcDeletionResult {
        val despawnResult = this.despawn(npc)

        DatabaseService.getLoadedNpcs().remove(npc)

        if(despawnResult == NpcDespawnResult.SUCCESS) {
            return NpcDeletionResult.SUCCESS
        }

        if(despawnResult == NpcDespawnResult.FAILED_ALREADY_SPAWNED) {
            return NpcDeletionResult.FAILED_NOT_FOUND
        }

        return NpcDeletionResult.FAILED_OTHER
    }

    override fun getNpc(id: UUID): CoreNpc? {
        return DatabaseService.getLoadedNpcs().find { it.id == id }
    }

    override fun getNpcs(): ObjectList<CoreNpc> {
        return DatabaseService.getLoadedNpcs().toObjectList()
    }

    override fun spawn(npc: CoreNpc): NpcSpawnResult {
        val userProfile = UserProfile(npc.id, npc.name)

        val playerInfoPacket = WrapperPlayServerPlayerInfo(
            WrapperPlayServerPlayerInfo.Action.ADD_PLAYER,
            PlayerData (
                npc.displayName,
                userProfile,
                GameMode.CREATIVE,
                0
            )
        )

        val spawnPacket = WrapperPlayServerSpawnPlayer(
            npc.entityId,
            npc.id,
            npc.location.toNMSLocation()
        )

        for (player in Bukkit.getOnlinePlayers()) {
            PacketEvents.getAPI().playerManager.sendPacket(player, playerInfoPacket)
            PacketEvents.getAPI().playerManager.sendPacket(player, spawnPacket)
        }

        return NpcSpawnResult.SUCCESS
    }


    override fun respawn(npc: CoreNpc): NpcRespawnResult {
        val despawnResult = this.despawn(npc)
        val spawnResult = this.spawn(npc)

        if(despawnResult == NpcDespawnResult.SUCCESS && spawnResult == NpcSpawnResult.SUCCESS) {
            return NpcRespawnResult.SUCCESS
        }

        if(despawnResult == NpcDespawnResult.FAILED_ALREADY_SPAWNED && spawnResult == NpcSpawnResult.FAILED_ALREADY_SPAWNED) {
            return NpcRespawnResult.FAILED_ALREADY_SPAWNED
        }

        if(despawnResult == NpcDespawnResult.FAILED_NO_LOCATION && spawnResult == NpcSpawnResult.FAILED_NO_LOCATION) {
            return NpcRespawnResult.FAILED_NO_LOCATION
        }

        return NpcRespawnResult.FAILED_OTHER
    }

    override fun despawn(npc: CoreNpc): NpcDespawnResult {
        val userProfile = UserProfile(npc.id, npc.name)

        val removePlayerPacket = WrapperPlayServerPlayerInfo(
            WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER,
            PlayerData(
                npc.displayName,
                userProfile,
                GameMode.CREATIVE,
                0
            )
        )

        val destroyPacket = WrapperPlayServerDestroyEntities(npc.entityId)

        for (player in Bukkit.getOnlinePlayers()) {
            PacketEvents.getAPI().playerManager.sendPacket(player, destroyPacket)
            PacketEvents.getAPI().playerManager.sendPacket(player, removePlayerPacket)
        }

        return NpcDespawnResult.SUCCESS
    }



    private fun Location.toNMSLocation(): com.github.retrooper.packetevents.protocol.world.Location {
        return com.github.retrooper.packetevents.protocol.world.Location(this.x, this.y, this.z, this.yaw, this.pitch)
    }
}