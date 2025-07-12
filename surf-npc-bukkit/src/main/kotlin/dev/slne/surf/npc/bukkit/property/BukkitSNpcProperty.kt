package dev.slne.surf.npc.bukkit.property

import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.api.npc.SNpcPropertyType

data class BukkitSNpcProperty<T : Any>(
    override val key: String,
    override val value: String,
    override val type: SNpcPropertyType<T>
) : SNpcProperty<T>
