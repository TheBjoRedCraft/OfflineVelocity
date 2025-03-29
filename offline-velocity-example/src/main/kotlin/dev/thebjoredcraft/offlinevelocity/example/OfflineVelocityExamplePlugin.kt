package dev.thebjoredcraft.offlinevelocity.example

import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import dev.thebjoredcraft.offlinevelocity.api.offlineVelocityApi
import org.slf4j.Logger

@Plugin(
    id = "offlinevelocityexample",
    name = "OfflineVelocityExample",
    version = "1.0.0",
    description = "A example plugin for OfflineVelocity",
    url = "github.com/TheBjoRedCraft/OfflineVelocity",
    authors = ["TheBjoRedCraft"]
)
class OfflineVelocityExamplePlugin
@Inject
constructor (
    private val logger: Logger,
    pluginContainer: SuspendingPluginContainer
) {
    init {
        pluginContainer.initialize(this)
    }

    @Subscribe
    suspend fun onProxyInitialization(event: ProxyInitializeEvent) {
        println("example api: $offlineVelocityApi")

        logger.warn("You can display, how man offline players are in the database here: ${offlineVelocityApi.getOfflineUsers().size}")
        logger.warn("You can get a random offline player name here: ${offlineVelocityApi.getName(offlineVelocityApi.getOfflineUsers().first()) ?: "N/A"}")
        logger.warn("You can get a random offline player uuid here: ${offlineVelocityApi.getUuid(offlineVelocityApi.getName(offlineVelocityApi.getOfflineUsers().first()) ?: "N/A")}")
    }

    @Subscribe
    suspend fun onProxyShutdown(event: ProxyShutdownEvent) {

    }
}
