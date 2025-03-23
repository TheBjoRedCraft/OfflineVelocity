package dev.thebjoredcraft.offlinevelocity.velocity.listener

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import dev.thebjoredcraft.offlinevelocity.core.databaseService

class PlayerConnectionHandler() {
    @Subscribe
    suspend fun onDisconnect(event: DisconnectEvent) {
        databaseService.saveIfNotExists(event.player)
    }
}