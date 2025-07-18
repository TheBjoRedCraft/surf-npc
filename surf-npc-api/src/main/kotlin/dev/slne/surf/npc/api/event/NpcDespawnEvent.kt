package dev.slne.surf.npc.api.event

import dev.slne.surf.npc.api.npc.Npc
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class NpcDespawnEvent (
    val npc: Npc,
    val player: Player
) : Event() {
    override fun getHandlers(): HandlerList = handlerList

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}