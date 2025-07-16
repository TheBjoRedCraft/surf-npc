package dev.slne.surf.npc.api.npc

interface SNpcPropertyType {
    val id: String
    fun encode(value: Any): String
    fun decode(value: String): Any
}

