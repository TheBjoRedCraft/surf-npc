package dev.slne.surf.npc.api.event

import dev.slne.surf.npc.api.npc.SNpc
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class NpcCollisionEvent (
    val npc: SNpc
): Event() {
    private val handlerList = HandlerList()

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}