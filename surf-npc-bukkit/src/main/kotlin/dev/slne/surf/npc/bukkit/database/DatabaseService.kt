package dev.slne.surf.npc.bukkit.database

import dev.slne.surf.database.DatabaseProvider
import dev.slne.surf.npc.api.npc.SNpc
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.bukkit.SurfNpcBukkit.Companion.instance
import dev.slne.surf.npc.core.npc.CoreNpc
import it.unimi.dsi.fastutil.objects.ObjectArraySet
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.io.path.div

object DatabaseService {
    private val loadedNpcs = ObjectArraySet<SNpc>()

    object Npcs : Table() {
        val id = char("id", 36).transform({ UUID.fromString(it) }, { it.toString() })
        val entityId = integer("entity_id")
        val name = text("name")
        val displayName = text("display_name").transform( { instance.gson.fromJson(it, Component::class.java) }, { instance.gson.toJson(it) })
        val location = text("location").transform( { instance.gson.fromJson(it, Location::class.java) }, { instance.gson.toJson(it) })
        val skin = text("skin")
        val properties = text("properties").transform( { instance.gson.fromJson(it, ObjectSet::class.java) as ObjectSet<*>}, { instance.gson.toJson(it)})

        override val primaryKey = PrimaryKey(id)
    }

    fun connect() {
        DatabaseProvider(instance.dataPath, instance.dataPath / "storage").connect()

        transaction {
            SchemaUtils.create(Npcs)
        }
    }

    suspend fun fetchNpcs() {
        newSuspendedTransaction {
            loadedNpcs.clear()

            Npcs.selectAll().forEach {
                val id = it[Npcs.id]
                val entityId = it[Npcs.entityId]
                val name = it[Npcs.name]
                val displayName = it[Npcs.displayName]
                val location = it[Npcs.location]
                val skin = it[Npcs.skin]
                val properties = it[Npcs.properties]

                loadedNpcs.add(CoreNpc(id, name, displayName, location, skin, properties as ObjectSet<SNpcProperty>, entityId))
            }
        }
    }

    suspend fun saveNpcs() {
        newSuspendedTransaction {
            loadedNpcs.forEach { npc ->
                Npcs.replace {
                    it[id] = npc.id
                    it[entityId] = npc.entityId
                    it[name] = npc.name
                    it[displayName] = npc.displayName
                    it[location] = npc.location
                    it[skin] = npc.skin
                    it[properties] = npc.properties
                }
            }
        }
    }

    fun getLoadedNpcs(): ObjectSet<SNpc> {
        return loadedNpcs
    }
}