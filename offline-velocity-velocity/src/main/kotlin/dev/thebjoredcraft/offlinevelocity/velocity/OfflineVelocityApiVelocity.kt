package dev.thebjoredcraft.offlinevelocity.velocity

import com.google.auto.service.AutoService
import dev.thebjoredcraft.offlinevelocity.api.OfflineVelocityApi
import dev.thebjoredcraft.offlinevelocity.api.`object`.User
import dev.thebjoredcraft.offlinevelocity.core.databaseService
import net.kyori.adventure.util.Services.Fallback
import java.util.*

@AutoService(OfflineVelocityApi::class)
class OfflineVelocityApiVelocity(): OfflineVelocityApi, Fallback {
    override suspend fun getName(uuid: UUID): String? {
        val optional = OfflineVelocityPlugin.instance.proxy.getPlayer(uuid)

        if(optional.isPresent) {
            return optional.get().username
        }

        return databaseService.getUser(uuid)?.name
    }

    override suspend fun getUuid(name: String): UUID? {
        val optional = OfflineVelocityPlugin.instance.proxy.getPlayer(name)

        if(optional.isPresent) {
            return optional.get().uniqueId
        }

        return databaseService.getUser(name)?.uuid
    }

    override suspend fun getOfflineUsers(): Set<UUID> {
        val databaseUsers = databaseService.getOfflineUsers()
        val onlineUsers = OfflineVelocityPlugin.instance.proxy.allPlayers.map { it.uniqueId }.toSet()

        val set = mutableSetOf<UUID>()

        set.addAll(databaseUsers)
        set.addAll(onlineUsers)

        return set
    }

    override suspend fun getUser(uuid: UUID): User? {
        val optional = OfflineVelocityPlugin.instance.proxy.getPlayer(uuid)

        if(optional.isPresent) {
            return User(optional.get().username, optional.get().uniqueId)
        }

        return databaseService.getUser(uuid)
    }

    override suspend fun getUser(name: String): User? {
        val optional = OfflineVelocityPlugin.instance.proxy.getPlayer(name)

        if(optional.isPresent) {
            return User(optional.get().username, optional.get().uniqueId)
        }

        return databaseService.getUser(name)
    }
}