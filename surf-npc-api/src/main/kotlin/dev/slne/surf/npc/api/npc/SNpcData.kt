package dev.slne.surf.npc.api.npc

import dev.slne.surf.npc.api.rotation.SNpcRotation
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.api.skin.SNpcSkinData
import net.kyori.adventure.text.Component

interface SNpcData {
    var displayName: Component
    val internalName: String
    var skin: SNpcSkinData
    var location: SNpcLocation

    var rotationType: SNpcRotationType
    var fixedRotation: SNpcRotation?

    var global: Boolean
}