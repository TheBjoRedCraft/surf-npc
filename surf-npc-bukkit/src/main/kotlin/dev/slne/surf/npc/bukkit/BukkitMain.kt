package dev.slne.surf.npc.bukkit

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListenerPriority
import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin

import dev.slne.surf.npc.bukkit.command.NpcCommand
import dev.slne.surf.npc.bukkit.listener.ConnectionListener
import dev.slne.surf.npc.bukkit.listener.NpcListener
import dev.slne.surf.npc.bukkit.property.impl.BooleanPropertyType
import dev.slne.surf.npc.bukkit.property.impl.ComponentPropertyType
import dev.slne.surf.npc.bukkit.property.impl.FloatPropertyType
import dev.slne.surf.npc.bukkit.property.impl.IntPropertyType
import dev.slne.surf.npc.bukkit.property.impl.LongPropertyType
import dev.slne.surf.npc.bukkit.property.impl.StringPropertyType
import dev.slne.surf.npc.core.property.propertyTypeRegistry
import dev.slne.surf.npc.core.service.storageService

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class BukkitMain : SuspendingJavaPlugin() {
    override fun onEnable() {
        PacketEvents.getAPI().eventManager.registerListener(NpcListener(), PacketListenerPriority.NORMAL)
        Bukkit.getPluginManager().registerEvents(ConnectionListener(), this)

        storageService.initialize()
        storageService.loadNpcs()

        propertyTypeRegistry.register(BooleanPropertyType("boolean"))
        propertyTypeRegistry.register(ComponentPropertyType("component"))
        propertyTypeRegistry.register(FloatPropertyType("float"))
        propertyTypeRegistry.register(IntPropertyType("int"))
        propertyTypeRegistry.register(LongPropertyType("long"))
        propertyTypeRegistry.register(StringPropertyType("string"))

        NpcCommand("npc").register()
    }

    override fun onDisable() {
        storageService.saveNpcs()
    }
}

val plugin get() = JavaPlugin.getPlugin(BukkitMain::class.java)