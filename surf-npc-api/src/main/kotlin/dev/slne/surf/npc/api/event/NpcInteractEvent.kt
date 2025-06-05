package dev.slne.surf.npc.api.event

import dev.slne.surf.npc.api.npc.SNpc
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class NpcInteractEvent (
    val npc: SNpc,
    val player: Player
) : Event() {
    private val handlerList = HandlerList()

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HandlerList()
        }
    }
}