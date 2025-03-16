package dev.slne.surf.npc.bukkit

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin

class SurfNpcBukkit():  SuspendingJavaPlugin() {
    companion object {
        lateinit var instance: SurfNpcBukkit
    }
}