package dev.slne.surf.npc.api

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.PacketEventsAPI
import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder

object SurfNpcApi {
    private var instance: SuspendingJavaPlugin? = null
    var loaded = false;

    fun initialize(plugin: SuspendingJavaPlugin) {
        instance = plugin

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(plugin))
        PacketEvents.getAPI().load()

        loaded = true
    }

    fun enable() {
        PacketEvents.getAPI().init()
    }

    fun shutdown() {
        loaded = false
        instance = null

        PacketEvents.getAPI().terminate()
    }

    fun getInstance(): SuspendingJavaPlugin? {
        return instance
    }
}