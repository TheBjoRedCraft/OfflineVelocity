package dev.thebjoredcraft.offlinevelocity.core

import dev.thebjoredcraft.offlinevelocity.api.`object`.User
import net.kyori.adventure.util.Services
import java.util.UUID

interface DatabaseService {
    fun connect()

    suspend fun getUser(uuid: UUID): User?
    suspend fun getUser(name: String): User?
    suspend fun getOfflineUsers(): Set<UUID>

    suspend fun saveIfNotExists(uuid: UUID, name: String)

    fun disconnect()

    companion object {
        val INSTANCE = Services.serviceWithFallback(DatabaseService::class.java).orElseThrow { Error("Service ${DatabaseService::class.java.name} not available") }
    }
}

val databaseService get() = DatabaseService.INSTANCE