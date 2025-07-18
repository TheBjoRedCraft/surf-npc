package dev.slne.surf.npc.api.dsl

import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.npc.rotation.NpcRotation
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.api.surfNpcApi
import net.kyori.adventure.text.Component

class NpcDslBuilder {
    lateinit var displayName: Component
    lateinit var internalName: String
    lateinit var skin: NpcSkin
    lateinit var location: NpcLocation
    var global: Boolean = true
    var rotationType: NpcRotationType = NpcRotationType.PER_PLAYER
    var fixedRotation: NpcRotation? = null

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

fun location(block: LocationBuilder.() -> Unit): NpcLocation {
    return LocationBuilder().apply(block).build()
}

fun rotation(block: RotationBuilder.() -> Unit): NpcRotation {
    return RotationBuilder().apply(block).build()
}

fun npcProperty(block: NpcPropertyBuilder.() -> Unit): NpcProperty {
    return NpcPropertyBuilder().apply(block).build()
}

fun skin(block: SkinBuilder.() -> Unit): NpcSkin {
    return SkinBuilder().apply(block).build()
}

suspend fun skin(name: String): NpcSkin {
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

