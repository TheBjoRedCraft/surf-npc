package dev.slne.surf.npc.bukkit.listener

import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import dev.slne.surf.npc.bukkit.util.toLocation
import dev.slne.surf.npc.core.controller.npcController
import org.bukkit.entity.Player
import java.util.UUID

class NpcListener : PacketListener {
    override fun onPacketReceive(event: PacketReceiveEvent) {
        val player = event.getPlayer<Player>()

        if(player == null) {
            return
        }

        if (event.packetType == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
            val playerUuid = player.uniqueId

            for (npc in npcController.getNpcs()) {
                val npcLoc = npc.data.location
                val playerLoc = player.location

                if (playerLoc.distanceSquared(npcLoc.toLocation() ?: return) > 20 * 20) {
                    continue
                }

                npc.refreshRotation(playerUuid)
            }
        }
    }
}