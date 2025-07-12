package dev.slne.surf.npc.bukkit.property.impl

import dev.slne.surf.npc.api.npc.SNpcPropertyType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

class ComponentPropertyType : SNpcPropertyType<Component> {
    override val classType: Class<Component> = Component::class.java

    override fun encode(value: Component): String {
        return MiniMessage.miniMessage().serialize(value)
    }

    override fun decode(value: String): Component {
        return MiniMessage.miniMessage().deserialize(value)
    }
}

