package dev.slne.surf.npc.api.npc

import it.unimi.dsi.fastutil.objects.ObjectSet
import org.bukkit.Location

data class Npc (
    val display: String,
    val location: Location,
    val skin: String,
    val property: ObjectSet<NpcProperty>
    )
