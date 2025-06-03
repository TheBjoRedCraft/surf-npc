package dev.slne.surf.npc.api.npc

import dev.slne.surf.npc.api.rotation.SNpcRotation
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.api.skin.SNpcSkinData
import net.kyori.adventure.text.Component

interface SNpcData {
    val name: Component
    val skin: SNpcSkinData
    val location: SNpcLocation

    val rotationType: SNpcRotationType
    val fixedRotation: SNpcRotation?
}