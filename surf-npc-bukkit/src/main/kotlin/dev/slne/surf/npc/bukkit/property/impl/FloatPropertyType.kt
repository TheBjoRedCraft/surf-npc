package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.SNpcPropertyType

class FloatPropertyType(override val id: String) : SNpcPropertyType {
    override fun encode(value: Any): String {
        require(value is Float) { "Expected Float, got ${value::class}" }
        return value.toString()
    }

    override fun decode(value: String): Float {
        return value.toFloat()
    }
}


