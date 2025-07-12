package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.SNpcPropertyType

class IntegerPropertyType : SNpcPropertyType<Int> {
    override val classType: Class<Int> = Int::class.java

    override fun encode(value: Int): String {
        return value.toString()
    }

    override fun decode(value: String): Int {
        return value.toInt()
    }
}

