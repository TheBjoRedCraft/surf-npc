package dev.slne.surf.npc.bukkit.listener

import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.core.controller.npcController
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class ConnectionListener : Listener {
    @EventHandler
    fun onConnect(event: PlayerJoinEvent) {
        val player = event.player

        npcController.getNpcs().filter { it.getProperty(SNpcProperty.Internal.VISIBILITY_GLOBAL)?.value as? Boolean ?: false }.forEach {
            it.spawn(player.uniqueId)
        }
    }
}