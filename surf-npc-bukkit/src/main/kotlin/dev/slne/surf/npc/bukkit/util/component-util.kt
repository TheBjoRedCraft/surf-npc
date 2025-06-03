package dev.slne.surf.npc.bukkit.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.gradle.internal.resource.StringTextResource

fun Component.toPlain(): String {
    return PlainTextComponentSerializer.plainText().serialize(this)
}