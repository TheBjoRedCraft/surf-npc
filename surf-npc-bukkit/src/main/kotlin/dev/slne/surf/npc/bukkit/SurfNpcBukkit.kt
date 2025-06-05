package dev.slne.surf.npc.bukkit

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListenerPriority
import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin

import dev.slne.surf.npc.bukkit.command.NpcCommand
import dev.slne.surf.npc.bukkit.listener.ConnectionListener
import dev.slne.surf.npc.bukkit.listener.NpcListener
import dev.slne.surf.npc.core.service.storageService

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class SurfNpcBukkit : SuspendingJavaPlugin() {
    override fun onEnable() {
        PacketEvents.getAPI().eventManager.registerListener(NpcListener(), PacketListenerPriority.NORMAL)
        Bukkit.getPluginManager().registerEvents(ConnectionListener(), this)

        storageService.initialize()
        storageService.loadNpcs()

        NpcCommand("npc").register()
    }

    override fun onDisable() {
        storageService.saveNpcs()
    }
}

val plugin get() = JavaPlugin.getPlugin(SurfNpcBukkit::class.java)
val serverId: Int get() = plugin.config.getInt("server-name")