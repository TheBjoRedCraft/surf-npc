package dev.slne.surf.npc.bukkit.skin

import dev.slne.surf.npc.api.skin.SNpcSkinData

data class BukkitSNpcSkinData (
    override val ownerName: String,
    override val value: String,
    override val signature: String
) : SNpcSkinData