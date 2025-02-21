package dev.thebjoredcraft.offlinevelocity.extension

import com.velocitypowered.api.proxy.Player
import dev.thebjoredcraft.offlinevelocity.player.PlayerData

class PlayerExtension {
    fun Player.getData(): PlayerData {
        return PlayerData(this.uniqueId, this.username, true)
    }

    fun Player.asData(): PlayerData {
        return PlayerData(this.uniqueId, this.username, true)
    }
}