package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.SNpcPropertyType

class StringPropertyType : SNpcPropertyType<String> {
    override val classType: Class<String> = String::class.java

    override fun encode(value: String): String {
        return value
    }

    override fun decode(value: String): String {
        return value
    }
}

