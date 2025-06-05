package dev.slne.surf.npc.bukkit.listener

import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition
import dev.slne.surf.npc.api.event.NpcCollisionEvent
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.bukkit.util.toLocation
import dev.slne.surf.npc.core.controller.npcController
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class NpcListener : PacketListener {
    override fun onPacketReceive(event: PacketReceiveEvent) {
        val player = event.getPlayer<Player>()

        if(player == null) {
            return
        }

        when(event.packetType) {
            PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION -> {
                for (npc in npcController.getNpcs()) {
                    val npcLoc = npc.data.location
                    val playerLoc = player.location

                    if (playerLoc.distanceSquared(npcLoc.toLocation() ?: return) > 20 * 20) {
                        continue
                    }

                    npc.refreshRotation(player.uniqueId)
                }
            }
            PacketType.Play.Client.PLAYER_POSITION -> {
                val packet = WrapperPlayClientPlayerPosition(event)

                for (npc in npcController.getNpcs()) {
                    val npcLoc = npc.data.location
                    val playerLoc = player.location

                    if (playerLoc.distanceSquared(npcLoc.toLocation() ?: return) > 1 * 1) {
                        continue
                    }

                    Bukkit.getPluginManager().callEvent(NpcCollisionEvent (
                        npc,
                        player
                    ))
                }
            }
            PacketType.Play.Client.INTERACT_ENTITY -> {
                val packet = WrapperPlayClientInteractEntity(event)
                val npc = npcController.getNpc(packet.entityId) ?: return

                Bukkit.getPluginManager().callEvent(NpcInteractEvent (
                    npc,
                    player
                ))
            }
        }
    }
}