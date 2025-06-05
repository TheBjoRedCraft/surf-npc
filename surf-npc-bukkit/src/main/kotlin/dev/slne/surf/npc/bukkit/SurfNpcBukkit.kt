package dev.slne.surf.npc.bukkit

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListenerPriority
import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.npc.bukkit.command.NpcCommand
import dev.slne.surf.npc.bukkit.listener.ConnectionListener
import dev.slne.surf.npc.bukkit.listener.NpcListener
import dev.slne.surf.npc.core.service.databaseService
import dev.slne.surf.surfapi.core.api.util.random
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

class SurfNpcBukkit : SuspendingJavaPlugin() {
    override fun onEnable() {
        PacketEvents.getAPI().eventManager.registerListener(NpcListener(), PacketListenerPriority.NORMAL)
        Bukkit.getPluginManager().registerEvents(ConnectionListener(), this)

        handleServerConfiguration()

        databaseService.connect(this.dataPath)
        databaseService.loadNpcs()


        NpcCommand("npc").register()
    }

    override fun onDisable() {
        databaseService.saveNpcs()
    }

    fun handleServerConfiguration() {
        this.saveDefaultConfig()
        val startValue = config.getString("server-name")

        if(startValue == "0") {
            config.set("server-name", random.nextInt())
            saveConfig()
        }
    }
}

val plugin get() = JavaPlugin.getPlugin(SurfNpcBukkit::class.java)
val serverId: Int get() = plugin.config.getInt("server-name") ?: error("Invalid plugin configuration: 'server-name' is not set.")