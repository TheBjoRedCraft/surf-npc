package dev.slne.surf.npc.core.npc

import dev.slne.surf.npc.api.npc.SNpc
import net.kyori.adventure.text.Component
import org.bukkit.Location
import java.util.*

class CoreNpc(
    override val id: UUID,
    override val name: String,
    override val displayName: Component,
    override val location: Location,
    override val skin: String
) : SNpc {

}