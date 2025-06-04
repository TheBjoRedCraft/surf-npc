package dev.slne.surf.npc.bukkit.npc

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.entity.data.EntityData
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.protocol.player.GameMode
import com.github.retrooper.packetevents.protocol.player.TextureProperty
import com.github.retrooper.packetevents.protocol.player.UserProfile
import com.github.retrooper.packetevents.protocol.world.Location
import com.github.retrooper.packetevents.util.Vector3d
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityHeadLook
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRotation
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity

import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcData
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.bukkit.util.toUser
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.surfapi.core.api.util.random

import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import java.util.Optional
import java.util.UUID
import kotlin.math.atan2
import kotlin.math.sqrt

class BukkitSNpc (
    override val id: Int,
    override val data: SNpcData,
    override val properties: ObjectSet<SNpcProperty>,
    override val viewers: ObjectSet<UUID>,
    override val npcUuid: UUID,
    override val nameTagId: Int
) : SNpc {
    override fun show(uuid: UUID) {
        val packetEvents = PacketEvents.getAPI()
        val playerManager = packetEvents.playerManager

        val player = Bukkit.getPlayer(uuid) ?: return
        val user = playerManager.getUser(player)
        val profile = UserProfile(npcUuid, data.internalName)

        profile.textureProperties.add(TextureProperty(
            "textures",
            data.skin.value,
            data.skin.signature
        ))

        val infoPacket = WrapperPlayServerPlayerInfoUpdate (
            WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER,
            WrapperPlayServerPlayerInfoUpdate.PlayerInfo (
                profile,
                false,
                0,
                GameMode.SURVIVAL,
                data.displayName,
                null
            )
        )

        val metaDataPacket = WrapperPlayServerEntityMetadata(
            id,
            listOf(
                EntityData(17, EntityDataTypes.BYTE, 0x7F.toByte()),
                EntityData(0, EntityDataTypes.BYTE, 0x02.toByte()),
            )
        )

        val rotationPair = Pair(
            data.fixedRotation?.yaw ?: 0f,
            data.fixedRotation?.pitch ?: 0f
        )

        val spawnPacket = WrapperPlayServerSpawnEntity (
            id,
            npcUuid,
            EntityTypes.PLAYER,
            Location(Vector3d(data.location.x, data.location.y, data.location.z), rotationPair.first, rotationPair.second),
            rotationPair.first,
            0,
            null
        )

        val spawnNametagPacket = WrapperPlayServerSpawnEntity (
            nameTagId,
            npcUuid,
            EntityTypes.TEXT_DISPLAY,
            Location(Vector3d(data.location.x, data.location.y + 1.25, data.location.z), rotationPair.first, rotationPair.second),
            rotationPair.first,
            0,
            null
        )

        val metaDataNameTagPacket = WrapperPlayServerEntityMetadata(
            nameTagId,
            listOf(
                EntityData(23, EntityDataTypes.ADV_COMPONENT, data.displayName)
            )
        )

        user.sendPacket(infoPacket)
        user.sendPacket(spawnPacket)
        user.sendPacket(metaDataPacket)

        user.sendPacket(spawnNametagPacket)
        user.sendPacket(metaDataNameTagPacket)
    }

    override fun hide(uuid: UUID) {
        val packetEvents = PacketEvents.getAPI()
        val playerManager = packetEvents.playerManager

        val player = Bukkit.getPlayer(uuid) ?: return
        val user = playerManager.getUser(player)

        val destroyPacket = WrapperPlayServerDestroyEntities (
            this.id, nameTagId
        )

        user.sendPacket(destroyPacket)
    }

    override fun refresh() {
        for (user in viewers) {
            val profile = UserProfile(npcUuid, PlainTextComponentSerializer.plainText().serialize(data.displayName))

            profile.textureProperties.clear()
            profile.textureProperties.add(TextureProperty(
                "textures",
                data.skin.value,
                data.skin.signature
            ))

            val infoPacket = WrapperPlayServerPlayerInfoUpdate (
                WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER,
                WrapperPlayServerPlayerInfoUpdate.PlayerInfo (
                    profile,
                    true,
                    0,
                    GameMode.SURVIVAL,
                    data.displayName,
                    null
                )
            )

            user.toUser()?.sendPacket(infoPacket)
        }
    }

    override fun refreshRotation(uuid: UUID) {
        val player = Bukkit.getPlayer(uuid) ?: return
        val user = PacketEvents.getAPI().playerManager.getUser(player)

        val yawPitch: Pair<Float, Float> = when (data.rotationType) {
            SNpcRotationType.FIXED -> {
                val fixedRotation = data.fixedRotation ?: return
                Pair(fixedRotation.yaw, fixedRotation.pitch)
            }

            SNpcRotationType.PER_PLAYER -> {
                val npcVec = Vector3d(data.location.x, data.location.y, data.location.z)
                val playerLoc = player.location

                val dx = playerLoc.x - npcVec.x
                val dz = playerLoc.z - npcVec.z
                val dy = playerLoc.y - npcVec.y

                val yaw = Math.toDegrees(atan2(-dx, dz)).toFloat()
                val horizontalDist = sqrt((dx * dx) + (dz * dz))
                val pitch = (-Math.toDegrees(atan2(dy, horizontalDist))).toFloat()

                Pair(yaw, pitch)
            }
        }

        val lookPacket = WrapperPlayServerEntityRotation(id, yawPitch.first, yawPitch.second, true)
        val headRotationPacket = WrapperPlayServerEntityHeadLook(id, yawPitch.first)

        user.sendPacket(lookPacket)
        user.sendPacket(headRotationPacket)
    }



    override fun delete() {
        npcController.deleteNpc(this)
    }

    override fun addProperty(property: SNpcProperty) {
        if (properties.any { it.key == property.key }) {
            return
        }
        properties.add(property)
    }

    override fun getProperty(key: String): SNpcProperty? {
        return properties.find { it.key == key }
    }

    override fun removeProperty(key: String) {
        properties.removeIf { it.key == key }
    }

    override fun hasProperty(key: String): Boolean {
        return properties.any { it.key == key }
    }

    override fun clearProperties() {
        properties.clear()
    }

    override fun hasProperties(): Boolean {
        return properties.isNotEmpty()
    }
}