package dev.slne.surf.npc.api.dsl

import dev.slne.surf.npc.api.npc.location.NpcLocation
import dev.slne.surf.npc.api.npc.property.NpcProperty
import dev.slne.surf.npc.api.npc.property.NpcPropertyType
import dev.slne.surf.npc.api.npc.rotation.NpcRotation
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.api.surfNpcApi

class SkinBuilder {
    lateinit var ownerName: String
    lateinit var value: String
    lateinit var signature: String

    fun build(): NpcSkin = object : NpcSkin {
        override val ownerName = this@SkinBuilder.ownerName
        override val value = this@SkinBuilder.value
        override val signature = this@SkinBuilder.signature
    }
}

class LocationBuilder {
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0
    lateinit var world: String

    fun build(): NpcLocation = object : NpcLocation {
        override val x = this@LocationBuilder.x
        override val y = this@LocationBuilder.y
        override val z = this@LocationBuilder.z
        override val world = this@LocationBuilder.world
    }
}

class RotationBuilder {
    var yaw: Float = 0f
    var pitch: Float = 0f

    fun build(): NpcRotation = object : NpcRotation {
        override val yaw = this@RotationBuilder.yaw
        override val pitch = this@RotationBuilder.pitch
    }
}

class NpcPropertyBuilder {
    var key: String = ""
    var value: Any = ""
    var type: NpcPropertyType = surfNpcApi.getPropertyType("string") ?: error("Default property type 'string' not found")

    fun build(): NpcProperty = object : NpcProperty {
        override val key = this@NpcPropertyBuilder.key
        override val value = this@NpcPropertyBuilder.value
        override val type = this@NpcPropertyBuilder.type
    }
}
