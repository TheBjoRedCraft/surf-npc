package dev.slne.surf.npc.bukkit.storage
import com.google.auto.service.AutoService

import dev.slne.surf.npc.api.npc.SNpcPropertyType
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.bukkit.npc.BukkitSNpc
import dev.slne.surf.npc.bukkit.npc.BukkitSNpcData
import dev.slne.surf.npc.bukkit.npc.BukkitSNpcLocation
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.property.BukkitSNpcProperty
import dev.slne.surf.npc.bukkit.rotation.BukkitSNpcRotation
import dev.slne.surf.npc.bukkit.skin.BukkitSNpcSkinData
import dev.slne.surf.npc.bukkit.util.miniMessage
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.npc.core.service.StorageService

import dev.slne.surf.surfapi.core.api.config.surfConfigApi
import dev.slne.surf.surfapi.core.api.util.logger
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf

import net.kyori.adventure.util.Services
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.util.UUID

@AutoService(StorageService::class)
class BukkitStorageService : StorageService, Services.Fallback {
    override fun initialize() {
        surfConfigApi.createSpongeYmlConfig<StorageConfig>(StorageConfig::class.java, plugin.dataPath, "storage.yml")
    }

    override fun loadNpcs() {
        val config = surfConfigApi.getSpongeConfig(StorageConfig::class.java)

        config.npcs.forEach { npcEntry ->
            npcController.registerNpc(BukkitSNpc(
                npcEntry.npcId,
                BukkitSNpcData(
                    miniMessage.deserialize(npcEntry.displayName),
                    npcEntry.internalName,
                    BukkitSNpcSkinData(
                        npcEntry.skin.skinOwner,
                        npcEntry.skin.skinValue,
                        npcEntry.skin.skinSignature
                    ),
                    BukkitSNpcLocation(
                        npcEntry.location.x,
                        npcEntry.location.y,
                        npcEntry.location.z,
                        npcEntry.location.world
                    ),
                    SNpcRotationType.valueOf(npcEntry.rotationConfig.rotationType),
                    BukkitSNpcRotation(
                        npcEntry.rotationConfig.rotationYaw,
                        npcEntry.rotationConfig.rotationPitch
                    ),
                    npcEntry.global
                ),
                npcEntry.properties.mapTo(mutableObjectSetOf()) {
                    BukkitSNpcProperty(
                        it.propertyKey,
                        it.propertyValue,
                        SNpcPropertyType.valueOf(it.propertyType)
                    )
                },
                npcEntry.viewers.mapTo(mutableObjectSetOf()) { it },
                npcEntry.npcUuid,
                npcEntry.nameTagId,
                npcEntry.nameTagUuid
            ))
        }

        logger().atInfo().log("Successfully loaded ${npcController.getNpcs().size}/${config.npcs.size} Npcs!")
    }

    override fun saveNpcs() {
        val configManager = surfConfigApi.getSpongeConfigManagerForConfig(StorageConfig::class.java)
        val config = configManager.config


        config.npcs = npcController.getNpcs().map { npc ->
            StorageNpcConfig(
                npcId = npc.id,
                npcUuid = npc.npcUuid,
                nameTagId = npc.nameTagId,
                nameTagUuid = npc.nameTagUuid,
                displayName = miniMessage.serialize(npc.data.displayName),
                internalName = npc.data.internalName,
                global = npc.data.global,
                skin = StorageNpcSkinConfig(
                    skinOwner = npc.data.skin.ownerName,
                    skinValue = npc.data.skin.value,
                    skinSignature = npc.data.skin.signature
                ),
                location = StorageNpcLocationConfig(
                    x = npc.data.location.x,
                    y = npc.data.location.y,
                    z = npc.data.location.z,
                    world = npc.data.location.world
                ),
                rotationConfig = StorageNpcRotationConfig(
                    rotationType = npc.data.rotationType.name,
                    rotationYaw = npc.data.fixedRotation?.yaw ?: 0f,
                    rotationPitch = npc.data.fixedRotation?.pitch ?: 0f
                ),
                properties = npc.properties.map {
                    StorageNpcPropertyConfig(
                        propertyType = it.type.name,
                        propertyKey = it.key,
                        propertyValue = it.value
                    )
                }.toSet(),
                viewers = npc.viewers.toList()
            )
        }
        configManager.save()

        logger().atInfo().log("Successfully saved ${config.npcs.size}/${npcController.getNpcs().size} Npcs!")
    }


    @ConfigSerializable
    data class StorageConfig (
        var npcs: List<StorageNpcConfig>
    )

    @ConfigSerializable
    data class StorageNpcConfig (
        val npcId: Int,
        val npcUuid: UUID,
        val nameTagId: Int,
        val nameTagUuid: UUID,

        val displayName: String,
        val internalName: String,
        val global: Boolean,
        val skin: StorageNpcSkinConfig,
        val location: StorageNpcLocationConfig,
        val rotationConfig: StorageNpcRotationConfig,
        val properties: Set<StorageNpcPropertyConfig>,
        val viewers: List<UUID>
    )

    @ConfigSerializable
    data class StorageNpcSkinConfig (
        val skinOwner: String,
        val skinValue: String,
        val skinSignature: String
    )

    @ConfigSerializable
    data class StorageNpcLocationConfig (
        val x: Double,
        val y: Double,
        val z: Double,
        val world: String
    )

    @ConfigSerializable
    data class StorageNpcRotationConfig (
        val rotationType: String,
        val rotationYaw: Float,
        val rotationPitch: Float
    )

    @ConfigSerializable
    data class StorageNpcPropertyConfig (
        val propertyType: String,
        val propertyKey: String,
        val propertyValue: String
    )
}