package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.SNpcPropertyType

class FloatPropertyType : SNpcPropertyType<Float> {
    override val classType: Class<Float> = Float::class.java

    override fun encode(value: Float): String {
        return value.toString()
    }

    override fun decode(value: String): Float {
        return value.toFloat()
    }
}

