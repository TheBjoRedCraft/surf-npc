package dev.slne.surf.npc.api.dsl

import dev.slne.surf.npc.api.npc.SNpcLocation
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.rotation.SNpcRotation
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.api.skin.SNpcSkinData
import dev.slne.surf.npc.api.surfNpcApi
import net.kyori.adventure.text.Component

class NpcDslBuilder {
    lateinit var displayName: Component
    lateinit var internalName: String
    lateinit var skin: SNpcSkinData
    lateinit var location: SNpcLocation
    var global: Boolean = true
    var rotationType: SNpcRotationType = SNpcRotationType.PER_PLAYER
    var fixedRotation: SNpcRotation? = null

    fun skin(block: SkinBuilder.() -> Unit) {
        skin = SkinBuilder().apply(block).build()
    }

    fun location(block: LocationBuilder.() -> Unit) {
        location = LocationBuilder().apply(block).build()
    }

    fun fixedRotation(block: RotationBuilder.() -> Unit) {
        fixedRotation = RotationBuilder().apply(block).build()
    }
}

fun location(block: LocationBuilder.() -> Unit): SNpcLocation {
    return LocationBuilder().apply(block).build()
}

fun rotation(block: RotationBuilder.() -> Unit): SNpcRotation {
    return RotationBuilder().apply(block).build()
}

fun npcProperty(block: NpcPropertyBuilder.() -> Unit): SNpcProperty {
    return NpcPropertyBuilder().apply(block).build()
}

fun skin(block: SkinBuilder.() -> Unit): SNpcSkinData {
    return SkinBuilder().apply(block).build()
}

suspend fun skin(name: String): SNpcSkinData {
    return surfNpcApi.getSkin(name)
}

fun npc(block: NpcDslBuilder.() -> Unit): NpcCreationResult {
    val builder = NpcDslBuilder().apply(block)
    return surfNpcApi.createNpc(
        displayName = builder.displayName,
        internalName = builder.internalName,
        skin = builder.skin,
        location = builder.location,
        global = builder.global,
        rotationType = builder.rotationType,
        fixedRotation = builder.fixedRotation
    )
}

