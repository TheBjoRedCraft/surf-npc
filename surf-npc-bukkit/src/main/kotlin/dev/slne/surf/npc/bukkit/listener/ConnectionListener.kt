package dev.slne.surf.npc.bukkit.listener

import dev.slne.surf.npc.api.event.NpcCollisionEvent
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.api.event.NpcSpawnEvent
import dev.slne.surf.npc.core.controller.npcController
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class ConnectionListener : Listener {
    @EventHandler
    fun onConnect(event: PlayerJoinEvent) {
        val player = event.player

        npcController.getNpcs().filter { it.data.global }.forEach {
            it.spawn(player.uniqueId)
        }
    }

    @EventHandler
    fun onNpcCollision(event: NpcCollisionEvent) {
        val npc = event.npc
        val player = event.player

        player.sendText {
            info("Du bist mit dem NPC '${npc.data.internalName}' kollidiert.")
        }
    }

    @EventHandler
    fun onNpcSpawn(event: NpcSpawnEvent) {
        val npc = event.npc
        val player = event.player

        player.sendText {
            info("Der NPC '${npc.data.internalName}' wurde gespawnt.")
        }
    }

    @EventHandler
    fun onNpcInteract(event: NpcInteractEvent) {
        val npc = event.npc
        val player = event.player

        player.sendText {
            info("Du hast den NPC '${npc.data.internalName}' angeklickt.")
        }
    }
}