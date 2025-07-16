package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.SNpcPropertyType

class IntPropertyType(override val id: String) : SNpcPropertyType {
    override fun encode(value: Any): String {
        require(value is Int) { "Expected Int, got ${value::class}" }
        return value.toString()
    }

    override fun decode(value: String): Int {
        return value.toInt()
    }
}


