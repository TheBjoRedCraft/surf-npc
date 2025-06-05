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
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
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

                newSuspendedTransaction {
                    Npcs.selectAll().where(Npcs.serverId eq serverIdentifier).forEach { row ->
                        val npcId = row[Npcs.npcId]
                        val npcUUID = row[Npcs.npcUuid]
                        val internalName = row[Npcs.internalName]
                        val nameTagId = row[Npcs.nameTagId]
                        val displayName = miniMessage.deserialize(row[Npcs.displayName])
                        val locationX = row[Npcs.locationX]
                        val locationY = row[Npcs.locationY]
                        val locationZ = row[Npcs.locationZ]
                        val world = row[Npcs.world]
                        val global = row[Npcs.global]

                        val skinRow = NpcSkins.selectAll().where(NpcSkins.npcId eq npcId and (NpcSkins.serverId eq serverIdentifier)).firstOrNull()
                        val propertyRow = NpcProperties.selectAll().where(NpcProperties.npcId eq npcId and (NpcProperties.serverId eq serverIdentifier))
                        val rotationRow = NpcRotation.selectAll().where(NpcRotation.npcId eq npcId and (NpcRotation.serverId eq serverIdentifier)).firstOrNull()
                        val viewers = NpcViewers.selectAll().where(NpcViewers.npcId eq npcId and (NpcViewers.serverId eq serverIdentifier))
                            .map { it[NpcViewers.viewerUuid] }
                            .toMutableSet()

                        val skin = if (skinRow != null) {
                            BukkitSNpcSkinData(
                                ownerName = skinRow[NpcSkins.skinName],
                                value = skinRow[NpcSkins.skinValue],
                                signature = skinRow[NpcSkins.skinSignature]
                            )
                        } else {
                            skinDataDefault()
                        }

                        val rotationType = if (rotationRow != null) {
                            SNpcRotationType.valueOf(rotationRow[NpcRotation.rotationType])
                        } else {
                            SNpcRotationType.FIXED
                        }

                        val rotation = if (rotationRow != null) {
                            BukkitSNpcRotation(
                                yaw = rotationRow[NpcRotation.rotationYaw],
                                pitch = rotationRow[NpcRotation.rotationPitch]
                            )
                        } else {
                            BukkitSNpcRotation(0f, 0f)
                        }

                        val properties = propertyRow.map {
                            BukkitSNpcProperty(
                                key = it[NpcProperties.key],
                                value = it[NpcProperties.value],
                                type = SNpcPropertyType.valueOf(it[NpcProperties.type])
                            )
                        }.toMutableSet()

                        npcController.registerNpc(BukkitSNpc(
                            id = npcId,
                            npcUuid = npcUUID,
                            nameTagId = nameTagId,
                            data = BukkitSNpcData(
                                displayName = displayName,
                                internalName = internalName,
                                skin = skin,
                                location = BukkitSNpcLocation(locationX, locationY, locationZ, world),
                                rotationType = rotationType,
                                fixedRotation = rotation,
                                global = global
                            ),
                            properties = properties.mapTo(mutableObjectSetOf()) { it },
                            viewers = mutableObjectSetOf(*viewers.toTypedArray())
                        ))
                    }
                }
            }
        }
    }

    override fun saveNpcs() {
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