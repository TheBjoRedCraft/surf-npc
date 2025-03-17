package dev.slne.surf.npc.api.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class NpcCollisionEvent(): Event() {
    private val handlerList = HandlerList()

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}