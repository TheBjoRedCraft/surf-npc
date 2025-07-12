package dev.slne.surf.npc.api.npc

interface SNpcPropertyType<T : Any> {
    val classType: Class<T>
    fun encode(value: T): String
    fun decode(value: String): T
}

