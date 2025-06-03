package dev.slne.surf.npc.bukkit.rotation

import dev.slne.surf.npc.api.rotation.SNpcRotation

data class BukkitSNpcRotation (
    override val yaw: Float,
    override val pitch: Float
) : SNpcRotation