package dev.slne.surf.npc.api.npc

interface SNpcProperty<T : Any> {
    val key: String
    val value: String
    val type: SNpcPropertyType<T>

    fun getTypedValue(): T = type.decode(value)
}
