package dev.slne.surf.npc.api.event

import dev.slne.surf.npc.api.npc.SNpc
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.UUID

class NpcDespawnEvent (
    val npc: SNpc,
    val player: Player
) : Event() {
    override fun getHandlers(): HandlerList = handlerList

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}