package dev.slne.surf.npc.bukkit.storage

import com.google.auto.service.AutoService
import dev.slne.surf.npc.bukkit.npc.BukkitNpc
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.npc.core.service.StorageService
import dev.slne.surf.surfapi.core.api.util.logger
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.bukkit.property.BukkitNpcProperty
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
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
                val uuid = UUID.fromString(config.getString("npc.data.uuid"))
                val nameTagId = config.getInt("npc.data.nameTagId")
                val nameTagUuid = UUID.fromString(config.getString("npc.data.nameTagUuid") ?: error("NameTag UUID is missing in NPC config file: ${path.fileName}"))
                val internalName = config.getString("npc.data.internalName") ?: error("Internal name is missing in NPC config file: ${path.fileName}")

                val viewers = config.getStringList("npc.data.viewers").map(UUID::fromString).toMutableObjectSet()

                val npc = BukkitNpc(
                    id = id,
                    npcUuid = uuid,
                    nameTagId = nameTagId,
                    nameTagUuid = nameTagUuid,
                    viewers = viewers,
                    properties = mutableObject2ObjectMapOf<String, NpcProperty>(),
                    internalName = internalName
                )

                val propsSection = config.getConfigurationSection("npc.data.properties") ?: return@forEach
                val sectionKeys = propsSection.getKeys(false)

                sectionKeys.forEach { key ->
                    val valueString = config.getString("npc.data.properties.$key.value") ?: return@forEach
                    val typeString = config.getString("npc.data.properties.$key.type") ?: return@forEach

                    val type = propertyTypeRegistry.get(typeString)
                        ?: error("Unknown property type: $typeString")
                    val value = type.decode(valueString)

                    npc.addProperty(BukkitNpcProperty(
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
            val file = npcFolder.resolve("${npc.internalName}.yml").toFile()
            val config = YamlConfiguration()

            config.set("npc.data.id", npc.id)
            config.set("npc.data.uuid", npc.npcUuid.toString())
            config.set("npc.data.nameTagId", npc.nameTagId)
            config.set("npc.data.nameTagUuid", npc.nameTagUuid.toString())
            config.set("npc.data.internalName", npc.internalName)

            config.set("npc.data.viewers", npc.viewers.map(UUID::toString))

            npc.properties.values.forEach {
                val path = "npc.data.properties.${it.key}"
                config.set("$path.value", it.type.encode(it.value))
                config.set("$path.type", it.type.id)
            }

            config.save(file)
        }

        logger().atInfo().log("Successfully saved ${npcController.getNpcs().size} NPCs to separate files!")
    }
}
