package dev.slne.surf.npc.bukkit

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListenerPriority
import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.npc.api.npc.SNpcPropertyType

import dev.slne.surf.npc.bukkit.command.NpcCommand
import dev.slne.surf.npc.bukkit.listener.ConnectionListener
import dev.slne.surf.npc.bukkit.listener.NpcListener
import dev.slne.surf.npc.bukkit.property.impl.BooleanPropertyType
import dev.slne.surf.npc.bukkit.property.impl.ComponentPropertyType
import dev.slne.surf.npc.bukkit.property.impl.DoublePropertyType
import dev.slne.surf.npc.bukkit.property.impl.FloatPropertyType
import dev.slne.surf.npc.bukkit.property.impl.IntPropertyType
import dev.slne.surf.npc.bukkit.property.impl.LocationPropertyType
import dev.slne.surf.npc.bukkit.property.impl.LongPropertyType
import dev.slne.surf.npc.bukkit.property.impl.NamedTextColorPropertyType
import dev.slne.surf.npc.bukkit.property.impl.StringPropertyType
import dev.slne.surf.npc.bukkit.property.impl.UuidPropertyType
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

        propertyTypeRegistry.register(BooleanPropertyType(SNpcPropertyType.Types.BOOLEAN))
        propertyTypeRegistry.register(ComponentPropertyType(SNpcPropertyType.Types.COMPONENT))
        propertyTypeRegistry.register(FloatPropertyType(SNpcPropertyType.Types.FLOAT))
        propertyTypeRegistry.register(IntPropertyType(SNpcPropertyType.Types.INT))
        propertyTypeRegistry.register(LongPropertyType(SNpcPropertyType.Types.LONG))
        propertyTypeRegistry.register(StringPropertyType(SNpcPropertyType.Types.STRING))
        propertyTypeRegistry.register(DoublePropertyType(SNpcPropertyType.Types.DOUBLE))
        propertyTypeRegistry.register(LocationPropertyType(SNpcPropertyType.Types.LOCATION))
        propertyTypeRegistry.register(UuidPropertyType(SNpcPropertyType.Types.UUID))
        propertyTypeRegistry.register(NamedTextColorPropertyType(SNpcPropertyType.Types.NAMED_TEXT_COLOR))

        NpcCommand("npc").register()
    }

    override fun onDisable() {
        storageService.saveNpcs()
    }
}

val plugin get() = JavaPlugin.getPlugin(BukkitMain::class.java)