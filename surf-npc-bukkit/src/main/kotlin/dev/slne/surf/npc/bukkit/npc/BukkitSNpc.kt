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
import dev.slne.surf.npc.api.npc.SNpcPropertyType
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
import dev.slne.surf.npc.bukkit.createTeleportPacket
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.property.BukkitSNpcProperty
import dev.slne.surf.npc.bukkit.rotation.BukkitSNpcRotation
import dev.slne.surf.npc.bukkit.util.toLocation
import dev.slne.surf.npc.bukkit.util.toUser
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.surfapi.bukkit.api.glow.glowingApi
import dev.slne.surf.surfapi.bukkit.api.util.forEachPlayer
import it.unimi.dsi.fastutil.objects.Object2ObjectMap

import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID
import kotlin.math.atan2
import kotlin.math.sqrt

class BukkitSNpc (
    override val id: Int,
    override val properties: Object2ObjectMap<String, SNpcProperty>,
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
        user.sendPacket(createPlayerSpawnPacket(id, npcUuid, location.toLocation(), rotationPair.first, rotationPair.second))
        user.sendPacket(createEntityMetadataPacket(id))

        user.sendPacket(createTeamCreatePacket("npc_$id", displayName))
        user.sendPacket(createTeamAddEntityPacket("npc_$id", internalName))

        user.sendPacket(createNametagSpawnPacket(nameTagId, nameTagUuid, location.toLocation()))
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

        val rotationType = if (this.getProperty(SNpcProperty.Internal.ROTATION_TYPE)?.value as? Boolean ?: error("Rotation type is not set for NPC: $internalName")) SNpcRotationType.PER_PLAYER else SNpcRotationType.FIXED
        val fixedRotation = this.getProperty(SNpcProperty.Internal.ROTATION_FIXED)?.value as? SNpcRotation ?: BukkitSNpcRotation(0f, 0f)
        val location = this.getProperty(SNpcProperty.Internal.LOCATION)?.value as? SNpcLocation ?: error("Location is not set for NPC: $internalName")

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

        val rotationPackets = createRotationPackets(id, yawPitch.first, yawPitch.second)

        user.sendPacket(rotationPackets.first)
        user.sendPacket(rotationPackets.second)
    }



    override fun delete() {
        npcController.deleteNpc(this)
    }

    override fun teleport(player: Player) {
        val location = player.location
        val global = this.getProperty(SNpcProperty.Internal.VISIBILITY_GLOBAL)?.value as? Boolean ?: false

        this.addProperty(BukkitSNpcProperty(
            SNpcProperty.Internal.LOCATION, BukkitSNpcLocation(location.x, location.y, location.z, location.world.name), propertyTypeRegistry.get(
                SNpcPropertyType.Types.NPC_LOCATION) ?: error("LOCATION property type not found")
        ))

        if(global) {
            forEachPlayer {
                val user = PacketEvents.getAPI().playerManager.getUser(it)
                user.sendPacket(createTeleportPacket(id, location))
                user.sendPacket(createTeleportPacket(nameTagId, location.clone().add(0.0, 2.0, 0.0)))
            }
            return
        }

        viewers.forEach {
            val target = Bukkit.getPlayer(it) ?: return@forEach
            val user = PacketEvents.getAPI().playerManager.getUser(target)

            user.sendPacket(createTeleportPacket(id, location))
            user.sendPacket(createTeleportPacket(nameTagId, location.clone().add(0.0, 2.0, 0.0)))
        }
    }

    override fun addProperty(property: SNpcProperty) {
        properties[property.key] = property
    }

    override fun getProperty(key: String): SNpcProperty? {
        return properties.values.find { it.key == key }
    }

    override fun removeProperty(key: String) {
        properties.remove(key)
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