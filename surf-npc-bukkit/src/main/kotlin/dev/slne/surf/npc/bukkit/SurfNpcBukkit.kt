package dev.slne.surf.npc.bukkit

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.google.gson.Gson
import org.bukkit.plugin.java.JavaPlugin

class SurfNpcBukkit : SuspendingJavaPlugin() {
    override fun onEnable() {

    }
}

val plugin get() = JavaPlugin.getPlugin(SurfNpcBukkit::class.java)