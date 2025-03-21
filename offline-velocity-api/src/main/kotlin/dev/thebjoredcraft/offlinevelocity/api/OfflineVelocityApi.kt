package dev.thebjoredcraft.offlinevelocity.api

import java.util.UUID

interface OfflineVelocityApi {
    fun getName(uuid: UUID): String?
    fun getUUID(name: String): UUID?
}