package dev.thebjoredcraft.offlinevelocity.api

import com.github.benmanes.caffeine.cache.Caffeine
import dev.hsbrysk.caffeine.CoroutineLoadingCache
import dev.hsbrysk.caffeine.buildCoroutine
import dev.thebjoredcraft.offlinevelocity.OfflineVelocity
import dev.thebjoredcraft.offlinevelocity.database.DatabaseService
import dev.thebjoredcraft.offlinevelocity.player.PlayerData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import java.util.UUID

object OfflineVelocityAPI {
    private val cache: CoroutineLoadingCache<UUID, PlayerData> = Caffeine.newBuilder().buildCoroutine(DatabaseService::loadPlayer)

    suspend fun getPlayer(name: String): PlayerData {
        val velocityPlayer = OfflineVelocity.instance.proxy.getPlayer(name)

        if(velocityPlayer.isPresent) {
            return PlayerData(velocityPlayer.get().uniqueId, name, true)
        }

        return cache.get(DatabaseService.getUUID(name) ?: return PlayerData(UUID.randomUUID(), name, false))
    }

    suspend fun getPlayer(name: Component): PlayerData {
        return this.getPlayer(PlainTextComponentSerializer.plainText().serialize(name))
    }

    suspend fun getPlayer(uuid: UUID): PlayerData {
        val velocityPlayer = OfflineVelocity.instance.proxy.getPlayer(uuid)

        if(velocityPlayer.isPresent) {
            return PlayerData(uuid, velocityPlayer.get().username, true)
        }

        return cache.get(uuid)
    }
}