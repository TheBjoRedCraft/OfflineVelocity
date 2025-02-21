package dev.thebjoredcraft.offlinevelocity.player

import java.util.UUID

class PlayerData (
    val uuid: UUID,
    val name: String,
    private var exists: Boolean
) {
    fun exists(): Boolean {
        return exists
    }
}