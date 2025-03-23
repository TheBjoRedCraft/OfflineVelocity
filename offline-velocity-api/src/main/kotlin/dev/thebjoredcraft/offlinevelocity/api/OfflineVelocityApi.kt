package dev.thebjoredcraft.offlinevelocity.api

import net.kyori.adventure.util.Services
import java.util.UUID

interface OfflineVelocityApi {
    /**
     * Retrieves the name associated with the given UUID.
     * First, it checks if the user is online.
     * If the user is online, it returns the online username.
     * If the user is not online, it retrieves the name from the database.
     *
     * @param uuid The UUID of the user.
     * @return The name of the user, or null if no user is found with the given UUID.
     */
    suspend fun getName(uuid: UUID): String?

    /**
     * Retrieves the UUID associated with the given name.
     * First, it checks if the user is online.
     * If the user is online, it returns the online UUID.
     * If the user is not online, it retrieves the UUID from the database.
     *
     * @param name The name of the user.
     * @return The UUID of the user, or null if no user is found with the given name.
     */
    suspend fun getUuid(name: String): UUID?

    /**
     * Retrieves a set of UUIDs representing all offline users.
     * This method combines the UUIDs of users
     * stored in the database with the UUIDs of currently online users.
     * Duplicate UUIDs are automatically removed as the result is returned as a Set.
     *
     * @return A set of UUIDs representing all offline users.
     */
    suspend fun getOfflineUsers(): Set<UUID>

    companion object {
        val INSTANCE: OfflineVelocityApi = Services.serviceWithFallback(OfflineVelocityApi::class.java).orElseThrow { Error("Service ${OfflineVelocityApi::class.java.name} not available") }
    }
}

val offlineVelocityApi: OfflineVelocityApi get() = OfflineVelocityApi.INSTANCE