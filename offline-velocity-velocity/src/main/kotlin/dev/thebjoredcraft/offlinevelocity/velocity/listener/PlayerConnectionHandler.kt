package dev.thebjoredcraft.offlinevelocity.velocity.listener

import com.github.shynixn.mccoroutine.velocity.launch
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import dev.thebjoredcraft.offlinevelocity.core.databaseService
import dev.thebjoredcraft.offlinevelocity.velocity.plugin

class PlayerConnectionHandler() {
    @Subscribe
    fun onDisconnect(event: DisconnectEvent) {
        plugin.suspendingPluginContainer.pluginContainer.launch {
            databaseService.saveIfNotExists(event.player.uniqueId, event.player.username)
        }
    }
}