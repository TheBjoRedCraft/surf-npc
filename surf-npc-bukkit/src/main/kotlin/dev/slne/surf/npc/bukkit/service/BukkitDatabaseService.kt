package dev.slne.surf.npc.bukkit.service

import com.github.shynixn.mccoroutine.folia.launch
import com.google.auto.service.AutoService
import dev.slne.surf.database.DatabaseManager
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.api.npc.SNpcPropertyType
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.bukkit.npc.BukkitSNpc
import dev.slne.surf.npc.bukkit.npc.BukkitSNpcData
import dev.slne.surf.npc.bukkit.npc.BukkitSNpcLocation
import dev.slne.surf.npc.bukkit.plugin
import dev.slne.surf.npc.bukkit.property.BukkitSNpcProperty
import dev.slne.surf.npc.bukkit.rotation.BukkitSNpcRotation
import dev.slne.surf.npc.bukkit.serverId
import dev.slne.surf.npc.bukkit.skin.BukkitSNpcSkinData
import dev.slne.surf.npc.bukkit.util.miniMessage
import dev.slne.surf.npc.bukkit.util.skinDataDefault
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.npc.core.service.DatabaseService
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.kyori.adventure.util.Services
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Path
import java.util.UUID

@AutoService(DatabaseService::class)
class BukkitDatabaseService : DatabaseService, Services.Fallback {
    object Npcs: Table("npc_list") {
        val npcId = integer("npc_id").uniqueIndex()
        val serverId = integer("server_id")
        val npcUuid = varchar("uuid", 36).transform({ UUID.fromString(it) }, { it.toString() })
        val nameTagId = integer("name_tag_id")

        val displayName = text("display_name")
        val internalName = varchar("internal_name", 64).uniqueIndex()
        val locationX = double("location_x")
        val locationY = double("location_y")
        val locationZ = double("location_z")
        val world = text("world")

        var global = bool("global").default(true)

        override val primaryKey = PrimaryKey(npcId)
    }

    object NpcProperties: Table("npc_properties") {
        val npcId = integer("npc_id").uniqueIndex()
        val serverId = integer("server_id")
        val key = text("key")
        val value = text("value")
        val type = text("type")

        override val primaryKey = PrimaryKey(npcId)
    }

    object NpcViewers: Table("npc_viewers") {
        val npcId = integer("npc_id").uniqueIndex()
        val serverId = integer("server_id")
        val viewerUuid = varchar("viewer_uuid", 36).transform({ UUID.fromString(it) }, { it.toString() })

        override val primaryKey = PrimaryKey(npcId)
    }

    object NpcRotation: Table("npc_rotation") {
        val npcId = integer("npc_id").uniqueIndex()
        val serverId = integer("server_id")
        val rotationType = text("rotation_type")
        val rotationYaw = float("rotation_yaw")
        val rotationPitch = float("rotation_pitch")

        override val primaryKey = PrimaryKey(npcId)
    }

    object NpcSkins: Table("npc_skins") {
        val npcId = integer("npc_id").uniqueIndex()
        val serverId = integer("server_id")
        val skinName = text("skin_name")
        val skinValue = text("skin_value")
        val skinSignature = text("skin_signature")

        override val primaryKey = PrimaryKey(npcId)
    }

    override fun connect(path: Path) {
        DatabaseManager(path, path).databaseProvider.connect()

        transaction {
            SchemaUtils.create(
                Npcs,
                NpcProperties,
                NpcViewers,
                NpcRotation,
                NpcSkins
            )
        }
    }

    override fun loadNpcs() {
        plugin.launch {
            withContext(Dispatchers.IO) {
                val serverIdentifier = serverId

                transaction {
                    val npcs = Npcs.selectAll().where(Npcs.serverId eq serverIdentifier).associateBy { it[Npcs.npcId] }
                    val propertiesMap = NpcProperties.selectAll().where(Npcs.serverId eq serverIdentifier).groupBy { it[NpcProperties.npcId] }
                    val viewersMap = NpcViewers.selectAll().where(Npcs.serverId eq serverIdentifier).groupBy { it[NpcViewers.npcId] }
                    val rotations = NpcRotation.selectAll().where(Npcs.serverId eq serverIdentifier).associateBy { it[NpcRotation.npcId] }
                    val skins = NpcSkins.selectAll().where(Npcs.serverId eq serverIdentifier).associateBy { it[NpcSkins.npcId] }

                    npcs.forEach { (id, row) ->
                        val data = BukkitSNpcData(
                            displayName = miniMessage.deserialize(row[Npcs.displayName]),
                            internalName = row[Npcs.internalName],
                            skin = skins[id]?.let {
                                BukkitSNpcSkinData (
                                    it[NpcSkins.skinName],
                                    it[NpcSkins.skinValue],
                                    it[NpcSkins.skinSignature]
                                )
                            } ?: skinDataDefault(),
                            location = BukkitSNpcLocation (
                                row[Npcs.locationX],
                                row[Npcs.locationY],
                                row[Npcs.locationZ],
                                row[Npcs.world]
                            ),
                            rotationType = rotations[id]?.get(NpcRotation.rotationType)?.let(SNpcRotationType::valueOf) ?: SNpcRotationType.FIXED,
                            fixedRotation = rotations[id]?.let {
                                BukkitSNpcRotation(it[NpcRotation.rotationYaw], it[NpcRotation.rotationPitch])
                            },
                            global = row[Npcs.global]
                        )

                        val props = propertiesMap[id].orEmpty().mapTo(mutableObjectSetOf()) {
                            BukkitSNpcProperty(it[NpcProperties.key], it[NpcProperties.value], SNpcPropertyType.valueOf(it[NpcProperties.type]))
                        }

                        val viewers = viewersMap[id].orEmpty().mapTo(mutableObjectSetOf()) {
                            it[NpcViewers.viewerUuid]
                        }

                        npcController.registerNpc(BukkitSNpc(
                            id = id,
                            data = data,
                            properties = props.mapTo(mutableObjectSetOf<SNpcProperty>()) { it },
                            viewers = viewers,
                            npcUuid = row[Npcs.npcUuid],
                            nameTagId = row[Npcs.nameTagId]
                        ))
                    }
                }
            }
        }
    }

    override fun saveNpcs() {
        plugin.launch {
            withContext(Dispatchers.IO) {
                val serverIdentifier = serverId
                transaction {
                    NpcViewers.deleteWhere { NpcViewers.serverId eq serverIdentifier }
                    NpcProperties.deleteWhere { NpcProperties.serverId eq serverIdentifier }
                    NpcRotation.deleteWhere { NpcRotation.serverId eq serverIdentifier }
                    NpcSkins.deleteWhere { NpcSkins.serverId eq serverIdentifier }
                    Npcs.deleteWhere { Npcs.serverId eq serverIdentifier }

                    npcController.getNpcs().forEach { npc ->
                        val data = npc.data as BukkitSNpcData
                        val skin = data.skin
                        val location = data.location
                        val rotation = data.fixedRotation

                        Npcs.insert {
                            it[npcId] = npc.id
                            it[internalName] = data.internalName
                            it[displayName] = miniMessage.serialize(data.displayName)
                            it[locationX] = location.x
                            it[locationY] = location.y
                            it[locationZ] = location.z
                            it[world] = location.world
                            it[global] = data.global
                            it[npcUuid] = npc.npcUuid
                            it[nameTagId] = npc.nameTagId
                            it[serverId] = serverIdentifier
                        }

                        NpcSkins.insert {
                            it[npcId] = npc.id
                            it[skinName] = skin.ownerName
                            it[skinValue] = skin.value
                            it[skinSignature] = skin.signature
                            it[serverId] = serverIdentifier
                        }

                        NpcRotation.insert {
                            it[npcId] = npc.id
                            it[rotationType] = data.rotationType.name
                            it[rotationYaw] = rotation?.yaw ?: 0f
                            it[rotationPitch] = rotation?.pitch ?: 0f
                            it[serverId]
                        }

                        npc.properties.forEach { prop ->
                            val bukkitProp = prop as BukkitSNpcProperty
                            NpcProperties.insert {
                                it[npcId] = npc.id
                                it[key] = bukkitProp.key
                                it[value] = bukkitProp.value
                                it[type] = bukkitProp.type.name
                                it[serverId] = serverIdentifier
                            }
                        }

                        npc.viewers.forEach { viewer ->
                            NpcViewers.insert {
                                it[npcId] = npc.id
                                it[viewerUuid] = viewer
                                it[serverId] = serverIdentifier
                            }
                        }
                    }
                }
            }
        }
    }
}