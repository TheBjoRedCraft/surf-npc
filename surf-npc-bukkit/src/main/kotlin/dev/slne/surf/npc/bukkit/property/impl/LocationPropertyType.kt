package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.SNpcPropertyType
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.UUID

class LocationPropertyType(override val id: String) : SNpcPropertyType {
    override fun encode(value: Any): String {
        require(value is Location) { "Expected Location, got ${value::class}" }
        return "${value.world?.name}:${value.x}:${value.y}:${value.z}:${value.pitch}:${value.yaw}:${value.world?.uid}"
    }

    override fun decode(value: String): Location {
        val parts = value.split(":")
        require(parts.size == 7) { "Invalid format: expected 7 parts but got ${parts.size}" }

        return Location(
            Bukkit.getWorld(UUID.fromString(parts[6])) ?: throw IllegalArgumentException("World not found: ${parts[6]}"),
            parts[1].toDouble(),
            parts[2].toDouble(),
            parts[3].toDouble(),
            parts[4].toFloat(),
            parts[5].toFloat()
        )
    }
}


