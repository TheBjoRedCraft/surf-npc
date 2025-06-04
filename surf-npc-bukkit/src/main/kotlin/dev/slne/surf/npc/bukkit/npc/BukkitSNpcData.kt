package dev.slne.surf.npc.bukkit.npc

import dev.slne.surf.npc.api.npc.SNpcData
import dev.slne.surf.npc.api.npc.SNpcLocation
import dev.slne.surf.npc.api.rotation.SNpcRotation
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.api.skin.SNpcSkinData
import net.kyori.adventure.text.Component

data class BukkitSNpcData (
    override var displayName: Component,
    override val internalName: String,
    override var skin: SNpcSkinData,
    override var location: SNpcLocation,
    override var rotationType: SNpcRotationType,
    override var fixedRotation: SNpcRotation?,
    override var global: Boolean
) : SNpcData