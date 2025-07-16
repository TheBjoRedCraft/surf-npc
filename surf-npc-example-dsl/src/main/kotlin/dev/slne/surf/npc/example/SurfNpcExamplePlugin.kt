package dev.slne.surf.npc.example

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.npc.api.dsl.npc
import dev.slne.surf.npc.api.dsl.npcProperty
import dev.slne.surf.npc.api.npc.SNpcPropertyType
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.api.skin.SNpcSkinData
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.npc.example.listener.ExampleNpcListener
import dev.slne.surf.surfapi.core.api.util.logger
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit

class SurfNpcExamplePlugin(): SuspendingJavaPlugin() {
    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(ExampleNpcListener(), this)

        npc {
            displayName = MiniMessage.miniMessage().deserialize("<rainbow>Example Npc by surf-npc-example")
            internalName = "example_npc"
            skin = dev.slne.surf.npc.api.dsl.skin {
                ownerName = "CastCrafter"
                value = "SkinValue"
                signature = "SkinSignature"
            }
            location = dev.slne.surf.npc.api.dsl.location {
                world = "world"
                x = 0.0
                y = 0.0
                z = 0.0
            }
            global = true
            rotationType = SNpcRotationType.PER_PLAYER
        }

        val npc = surfNpcApi.getNpc("example_npc") ?: return run {
            logger().atWarning().log("Failed to create example NPC: NPC not found after creation.")
        }

        surfNpcApi.addProperty(npc, npcProperty {
            key = "example_npc"
            value = true
            type = surfNpcApi.getPropertyType(SNpcPropertyType.Types.BOOLEAN) ?: return@npcProperty run {
                logger().atWarning().log("Failed to create example NPC: Boolean property type not found.")
            }
        })
    }
}