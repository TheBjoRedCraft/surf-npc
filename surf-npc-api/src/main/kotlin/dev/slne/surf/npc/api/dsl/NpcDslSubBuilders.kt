package dev.slne.surf.npc.api.dsl

import dev.slne.surf.npc.api.npc.SNpcLocation
import dev.slne.surf.npc.api.npc.SNpcProperty
import dev.slne.surf.npc.api.npc.SNpcPropertyType
import dev.slne.surf.npc.api.rotation.SNpcRotation
import dev.slne.surf.npc.api.skin.SNpcSkinData
import dev.slne.surf.npc.api.surfNpcApi

class SkinBuilder {
    lateinit var ownerName: String
    lateinit var value: String
    lateinit var signature: String

    fun build(): SNpcSkinData = object : SNpcSkinData {
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

    fun build(): SNpcLocation = object : SNpcLocation {
        override val x = this@LocationBuilder.x
        override val y = this@LocationBuilder.y
        override val z = this@LocationBuilder.z
        override val world = this@LocationBuilder.world
    }
}

class RotationBuilder {
    var yaw: Float = 0f
    var pitch: Float = 0f

    fun build(): SNpcRotation = object : SNpcRotation {
        override val yaw = this@RotationBuilder.yaw
        override val pitch = this@RotationBuilder.pitch
    }
}

class NpcPropertyBuilder {
    var key: String = ""
    var value: Any = ""
    var type: SNpcPropertyType = surfNpcApi.getPropertyType("string") ?: error("Default property type 'string' not found")

    fun build(): SNpcProperty = object : SNpcProperty {
        override val key = this@NpcPropertyBuilder.key
        override val value = this@NpcPropertyBuilder.value
        override val type = this@NpcPropertyBuilder.type
    }
}
