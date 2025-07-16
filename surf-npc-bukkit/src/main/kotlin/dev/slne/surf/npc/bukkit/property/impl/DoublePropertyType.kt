package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.SNpcPropertyType

class DoublePropertyType(override val id: String) : SNpcPropertyType {
    override fun encode(value: Any): String {
        require(value is Double) { "Expected Double, got ${value::class}" }
        return value.toString()
    }

    override fun decode(value: String): Double {
        return value.toDouble()
    }
}


