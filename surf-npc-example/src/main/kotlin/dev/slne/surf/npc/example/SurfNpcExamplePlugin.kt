package dev.slne.surf.npc.example

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.npc.api.rotation.SNpcRotationType
import dev.slne.surf.npc.api.skin.SNpcSkinData
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.npc.example.listener.ExampleNpcListener
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
    }

    fun createSkinData(): SNpcSkinData {
        return surfNpcApi.createSkinData(
            owner = "OwnerName",
            value = "SkinValue",
            signature = "SkinSignature"
        )
    }
}