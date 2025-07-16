package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.SNpcPropertyType

class StringPropertyType(override val id: String) : SNpcPropertyType {
    override fun encode(value: Any): String {
        require(value is String) { "Expected String, got ${value::class}" }
        return value
    }

    override fun decode(value: String): String {
        return value
    }
}


