package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.SNpcPropertyType
import java.util.UUID

class UuidPropertyType(override val id: String) : SNpcPropertyType {
    override fun encode(value: Any): String {
        require(value is UUID) { "Expected Uuid, got ${value::class}" }
        return value.toString()
    }

    override fun decode(value: String): UUID {
        return UUID.fromString(value)
    }
}


