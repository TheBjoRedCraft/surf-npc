package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.SNpcPropertyType
import dev.slne.surf.npc.api.rotation.SNpcRotation
import dev.slne.surf.npc.bukkit.rotation.BukkitSNpcRotation
import java.util.UUID

class NpcRotationPropertyType(override val id: String) : SNpcPropertyType {
    override fun encode(value: Any): String {
        require(value is SNpcRotation) { "Expected SNpcRotation, got ${value::class}" }
        return "${value.yaw}:${value.pitch}"
    }

    override fun decode(value: String): SNpcRotation {
        return value.split(":").let {
            require(it.size == 2) { "Invalid rotation format: $value" }

            val yaw = it[0].toFloatOrNull() ?: throw IllegalArgumentException("Invalid yaw value: ${it[0]}")
            val pitch = it[1].toFloatOrNull() ?: throw IllegalArgumentException("Invalid pitch value: ${it[1]}")

            BukkitSNpcRotation(yaw, pitch)
        }
    }
}


