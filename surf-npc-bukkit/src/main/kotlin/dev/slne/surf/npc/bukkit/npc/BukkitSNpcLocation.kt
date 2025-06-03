package dev.slne.surf.npc.bukkit.npc

import dev.slne.surf.npc.api.npc.SNpcLocation

data class BukkitSNpcLocation (
    override val x: Double,
    override val y: Double,
    override val z: Double,
    override val world: String
) : SNpcLocation