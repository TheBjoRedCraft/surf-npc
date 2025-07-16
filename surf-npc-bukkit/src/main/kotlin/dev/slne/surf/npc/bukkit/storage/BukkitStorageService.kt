package dev.slne.surf.npc.bukkit.storage

import com.google.auto.service.AutoService
import dev.slne.surf.npc.bukkit.npc.BukkitSNpc
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.npc.core.service.StorageService
import dev.slne.surf.surfapi.core.api.util.logger
import dev.slne.surf.npc.api.npc.*
import dev.slne.surf.npc.api.rotation.SNpcRotation
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.api.skin.SNpcSkinData
import dev.slne.surf.npc.bukkit.property.BukkitSNpcProperty
import dev.slne.surf.npc.bukkit.util.miniMessage
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.toMutableObjectSet

import net.kyori.adventure.util.Services
import org.bukkit.configuration.file.YamlConfiguration
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID

@AutoService(StorageService::class)
class BukkitStorageService : StorageService, Services.Fallback {
    private val npcFolder: Path
        get() = plugin.dataPath.resolve("npcs")

    override fun initialize() {
        if (!Files.exists(npcFolder)) {
            Files.createDirectories(npcFolder)
        }
    }

    override fun loadNpcs() {
        val files = Files.list(npcFolder).filter { it.toString().endsWith(".yml") }.toList()

        files.forEach { path ->
            val config = YamlConfiguration.loadConfiguration(path.toFile())
            try {
                val id = config.getInt("npc.data.id")
                val uuid = UUID.fromString(config.getString("npc.data.uuid")!!)
                val nameTagId = config.getInt("npc.data.nameTagId")
                val nameTagUuid = UUID.fromString(config.getString("npc.data.nameTagUuid"))
                val displayName = miniMessage.deserialize(config.getString("npc.data.displayName") ?: "")
                val internalName = config.getString("npc.data.internalName")!!

                val skin: SNpcSkinData = object : SNpcSkinData {
                    override val ownerName = config.getString("npc.data.skin.owner") ?: "Unknown"
                    override val value = config.getString("npc.data.skin.value") ?: error("Skin value cannot be null")
                    override val signature = config.getString("npc.data.skin.signature") ?: error("Skin signature cannot be null")
                }

                val location: SNpcLocation = object : SNpcLocation {
                    override val x = config.getDouble("npc.data.location.x")
                    override val y = config.getDouble("npc.data.location.y")
                    override val z = config.getDouble("npc.data.location.z")
                    override val world = config.getString("npc.data.location.world")!!
                }

                val rotationType = SNpcRotationType.valueOf(config.getString("npc.data.rotationType")!!)
                val fixedRotation: SNpcRotation? = if (rotationType == SNpcRotationType.FIXED) {
                    object : SNpcRotation {
                        override val yaw = config.getDouble("npc.data.fixedRotation.yaw").toFloat()
                        override val pitch = config.getDouble("npc.data.fixedRotation.pitch").toFloat()
                    }
                } else null

                val global = config.getBoolean("npc.data.global")
                val viewers = config.getStringList("npc.data.viewers").map(UUID::fromString).toMutableObjectSet()

                val data = object : SNpcData {
                    override var displayName = displayName
                    override val internalName = internalName
                    override var skin = skin
                    override var location = location
                    override var rotationType = rotationType
                    override var fixedRotation = fixedRotation
                    override var global = global
                }

                val npc = BukkitSNpc(
                    id = id,
                    npcUuid = uuid,
                    nameTagId = nameTagId,
                    nameTagUuid = nameTagUuid,
                    data = data,
                    viewers = viewers,
                    properties = mutableObjectSetOf<SNpcProperty>()
                )

                val propsSection = config.getConfigurationSection("npc.data.properties") ?: return@forEach
                val sectionKeys = propsSection.getKeys(false)

                sectionKeys.forEach { key ->
                    val valueString = config.getString("npc.data.properties.$key.value") ?: return@forEach
                    val typeString = config.getString("npc.data.properties.$key.type") ?: return@forEach

                    val type = propertyTypeRegistry.get(typeString)
                        ?: error("Unknown property type: $typeString")
                    val value = type.decode(valueString)

                    npc.addProperty(BukkitSNpcProperty(
                        key, value, type
                    ))
                }

                npcController.registerNpc(npc)
            } catch (e: Exception) {
                logger().atWarning().log("Failed to load NPC from file ${path.fileName}: ${e.message}")
            }
        }

        logger().atInfo().log("Successfully loaded ${npcController.getNpcs().size} NPCs from ${files.size} files!")
    }

    override fun saveNpcs() {
        Files.list(npcFolder)
            .filter { it.toString().endsWith(".yml") }
            .forEach(Files::delete)

        npcController.getNpcs().forEach { npc ->
            val file = npcFolder.resolve("${npc.data.internalName}.yml").toFile()
            val config = YamlConfiguration()

            config.set("npc.data.id", npc.id)
            config.set("npc.data.uuid", npc.npcUuid.toString())
            config.set("npc.data.nameTagId", npc.nameTagId)
            config.set("npc.data.nameTagUuid", npc.nameTagUuid.toString())
            config.set("npc.data.displayName", miniMessage.serialize(npc.data.displayName))
            config.set("npc.data.internalName", npc.data.internalName)

            config.set("npc.data.skin.owner", npc.data.skin.ownerName)
            config.set("npc.data.skin.value", npc.data.skin.value)
            config.set("npc.data.skin.signature", npc.data.skin.signature)

            config.set("npc.data.location.x", npc.data.location.x)
            config.set("npc.data.location.y", npc.data.location.y)
            config.set("npc.data.location.z", npc.data.location.z)
            config.set("npc.data.location.world", npc.data.location.world)

            config.set("npc.data.rotationType", npc.data.rotationType.name)
            npc.data.fixedRotation?.let {
                config.set("npc.data.fixedRotation.yaw", it.yaw)
                config.set("npc.data.fixedRotation.pitch", it.pitch)
            }

            config.set("npc.data.global", npc.data.global)
            config.set("npc.data.viewers", npc.viewers.map(UUID::toString))

            npc.properties.forEach {
                val path = "npc.data.properties.${it.key}"
                config.set("$path.value", it.type.encode(it.value))
                config.set("$path.type", it.type.id)
            }

            config.save(file)
        }

        logger().atInfo().log("Successfully saved ${npcController.getNpcs().size} NPCs to separate files!")
    }
}
