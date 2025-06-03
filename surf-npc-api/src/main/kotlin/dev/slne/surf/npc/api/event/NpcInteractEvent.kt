package dev.slne.surf.npc.api.event

import dev.slne.surf.npc.api.npc.SNpc
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class NpcInteractEvent (
    val npc: SNpc
) : Event(), Cancellable {
    private val handlerList = HandlerList()

    var cancelled = false

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }
}