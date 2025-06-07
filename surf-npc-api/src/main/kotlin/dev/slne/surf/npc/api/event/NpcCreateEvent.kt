package dev.slne.surf.npc.api.event

import dev.slne.surf.npc.api.npc.SNpc
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class NpcCreateEvent (
    val npc: SNpc
) : Event() {
    override fun getHandlers(): HandlerList = handlerList

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}