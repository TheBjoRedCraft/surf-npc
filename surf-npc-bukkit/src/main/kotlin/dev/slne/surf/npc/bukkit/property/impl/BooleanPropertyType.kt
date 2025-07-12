package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.SNpcPropertyType

class BooleanPropertyType : SNpcPropertyType<Boolean> {
    override val classType: Class<Boolean> = Boolean::class.java

    override fun encode(value: Boolean): String {
        return value.toString()
    }

    override fun decode(value: String): Boolean {
        return value.toBooleanStrict()
    }
}

