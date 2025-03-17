package dev.slne.surf.npc.bukkit

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.google.gson.Gson
import dev.slne.surf.npc.bukkit.database.DatabaseService

class SurfNpcBukkit():  SuspendingJavaPlugin() {
    val gson = Gson()

    companion object {
        lateinit var instance: SurfNpcBukkit
    }

    override fun onLoad() {
        instance = this

        saveDefaultConfig()
    }

    override fun onEnable() {
        DatabaseService.connect()
    }

    override suspend fun onEnableAsync() {
        DatabaseService.fetchNpcs()
    }

    override suspend fun onDisableAsync() {
        DatabaseService.saveNpcs()
    }
}