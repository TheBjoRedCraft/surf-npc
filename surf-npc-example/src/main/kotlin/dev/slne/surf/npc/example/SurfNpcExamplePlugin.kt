package dev.slne.surf.npc.example

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
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

        surfNpcApi.createNpc (
            MiniMessage.miniMessage().deserialize("<rainbow>Example Npc by surf-npc-example"),
            "example_npc",
            createSkinData(),
            surfNpcApi.createLocation(0.0, 0.0, 0.0, "world"),
            true,
            SNpcRotationType.PER_PLAYER
        )

        val npc = surfNpcApi.getNpc("example_npc") ?: return run {
            logger().atWarning().log("Failed to create example NPC: NPC not found after creation.")
        }

        surfNpcApi.addProperty(npc, surfNpcApi.createProperty(
            "example_npc",
            "true",
            surfNpcApi.getPropertyType(Boolean::class.java) ?: return run {
                logger().atWarning().log("Failed to create example NPC: Boolean property type not found.")
            }
        ))
    }

    fun createSkinData(): SNpcSkinData {
        return surfNpcApi.createSkinData(
            owner = "OwnerName",
            value = "SkinValue",
            signature = "SkinSignature"
        )
    }
}