package dev.slne.surf.npc.bukkit.npc

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.player.TextureProperty
import com.github.retrooper.packetevents.protocol.player.UserProfile
import com.github.retrooper.packetevents.util.Vector3d

import dev.slne.surf.npc.api.event.NpcDespawnEvent
import dev.slne.surf.npc.api.event.NpcSpawnEvent
import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcLocation
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.api.rotation.SNpcRotation
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.bukkit.createDestroyPacket
import dev.slne.surf.npc.bukkit.createEntityMetadataPacket
import dev.slne.surf.npc.bukkit.createNametagMetadataPacket
import dev.slne.surf.npc.bukkit.createNametagSpawnPacket
import dev.slne.surf.npc.bukkit.createPlayerInfoPacket
import dev.slne.surf.npc.bukkit.createPlayerInfoRemovePacket
import dev.slne.surf.npc.bukkit.createRotationPackets
import dev.slne.surf.npc.bukkit.createPlayerSpawnPacket
import dev.slne.surf.npc.bukkit.createTeamAddEntityPacket
import dev.slne.surf.npc.bukkit.createTeamCreatePacket
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.util.toLocation
import dev.slne.surf.npc.bukkit.util.toUser
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.surfapi.bukkit.api.glow.glowingApi

import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import java.util.UUID
import kotlin.math.atan2
import kotlin.math.sqrt

class BukkitSNpc (
    override val id: Int,
    override val properties: ObjectSet<SNpcProperty>,
    override val viewers: ObjectSet<UUID>,
    override val npcUuid: UUID,
    override val nameTagId: Int,
    override val nameTagUuid: UUID,
    override val internalName: String
) : SNpc {
    override fun spawn(uuid: UUID) {
        val packetEvents = PacketEvents.getAPI()
        val playerManager = packetEvents.playerManager

        val player = Bukkit.getPlayer(uuid) ?: return
        val user = playerManager.getUser(player)

        val displayName = this.getProperty(SNpcProperty.Internal.DISPLAYNAME)?.value as? Component ?: return
        val profile = UserProfile(npcUuid, internalName)

        val skinValue = this.getProperty(SNpcProperty.Internal.SKIN_TEXTURE)?.value as? String ?: return
        val skinSignature = this.getProperty(SNpcProperty.Internal.SKIN_SIGNATURE)?.value as? String ?: return

        val rotation = this.getProperty(SNpcProperty.Internal.ROTATION_FIXED)?.value as? SNpcRotation ?: return
        val location = this.getProperty(SNpcProperty.Internal.LOCATION)?.value as? SNpcLocation ?: return

        val glowing = this.getProperty("glowing")?.value as? Boolean ?: false
        val glowingColor = this.getProperty("glowing_color")?.value as? NamedTextColor ?: NamedTextColor.WHITE

        profile.textureProperties.add(TextureProperty(
            "textures",
            skinValue,
            skinSignature
        ))

        val rotationPair = Pair(
            rotation.yaw,
            rotation.pitch
        )

        user.sendPacket(createPlayerInfoPacket(profile, displayName))
        user.sendPacket(createPlayerSpawnPacket(id, npcUuid, location.toLocation() ?: error("Location is null for NPC: $internalName"), rotationPair.first, rotationPair.second))
        user.sendPacket(createEntityMetadataPacket(id))

        user.sendPacket(createTeamCreatePacket("npc_$id", displayName))
        user.sendPacket(createTeamAddEntityPacket("npc_$id", internalName))

        user.sendPacket(createNametagSpawnPacket(nameTagId, nameTagUuid, location.toLocation() ?: error("Location is null for NPC: $internalName")))
        user.sendPacket(createNametagMetadataPacket(nameTagId, displayName))

        if(glowing) {
            glowingApi.makeGlowing(id, "npc_$id", player, glowingColor)
        }

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            Bukkit.getPluginManager().callEvent(NpcSpawnEvent (
                this,
                player
            ))
        }, 1L)
    }

    override fun despawn(uuid: UUID) {
        val packetEvents = PacketEvents.getAPI()
        val playerManager = packetEvents.playerManager

        val player = Bukkit.getPlayer(uuid) ?: return
        val user = playerManager.getUser(player)

        user.sendPacket(createDestroyPacket(this.id, nameTagId))
        user.sendPacket(createPlayerInfoRemovePacket(npcUuid))

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            Bukkit.getPluginManager().callEvent(NpcDespawnEvent (
                this,
                player
            ))
        }, 1L)
    }

    override fun refresh() {
        for (user in viewers) {
            val displayName = this.getProperty(SNpcProperty.Internal.DISPLAYNAME)?.value as? Component ?: return
            val profile = UserProfile(npcUuid, internalName)

            val skinValue = this.getProperty(SNpcProperty.Internal.SKIN_TEXTURE)?.value as? String ?: return
            val skinSignature = this.getProperty(SNpcProperty.Internal.SKIN_SIGNATURE)?.value as? String ?: return

            profile.textureProperties.clear()
            profile.textureProperties.add(TextureProperty(
                "textures",
                skinValue,
                skinSignature
            ))

            user.toUser()?.sendPacket(createPlayerInfoPacket(profile, displayName))
        }
    }

    override fun refreshRotation(uuid: UUID) {
        val player = Bukkit.getPlayer(uuid) ?: return
        val user = PacketEvents.getAPI().playerManager.getUser(player)

        println("Refreshing rotation for NPC: $internalName")

        val rotationType = this.getProperty(SNpcProperty.Internal.ROTATION_TYPE)?.value as? SNpcRotationType ?: return
        val fixedRotation = this.getProperty(SNpcProperty.Internal.ROTATION_FIXED)?.value as? SNpcRotation ?: return
        val location = this.getProperty(SNpcProperty.Internal.LOCATION)?.value as? SNpcLocation ?: return

        println("Rotation Type: $rotationType")

        val yawPitch: Pair<Float, Float> = when (rotationType) {
            SNpcRotationType.FIXED -> {
                Pair(fixedRotation.yaw, fixedRotation.pitch)
            }

            SNpcRotationType.PER_PLAYER -> {
                val npcVec = Vector3d(location.x, location.y, location.z)
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

        println("Calculated Yaw: ${yawPitch.first}, Pitch: ${yawPitch.second}")

        val rotationPackets = createRotationPackets(id, yawPitch.first, yawPitch.second)

        println("Sending rotation packets for NPC: $internalName")

        user.sendPacket(rotationPackets.first)
        user.sendPacket(rotationPackets.second)

        println("Rotation packets sent for NPC: $internalName")
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