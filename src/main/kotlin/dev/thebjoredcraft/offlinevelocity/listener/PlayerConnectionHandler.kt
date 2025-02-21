package dev.thebjoredcraft.offlinevelocity.listener

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import dev.thebjoredcraft.offlinevelocity.database.DatabaseService

class PlayerConnectionHandler() {
    @Subscribe
    suspend fun onDisconnect(event: DisconnectEvent) {
        DatabaseService.saveIfNotExists(event.player)
    }
}