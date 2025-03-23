package dev.thebjoredcraft.offlinevelocity.example


import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import dev.thebjoredcraft.offlinevelocity.api.OfflineVelocityApi
import dev.thebjoredcraft.offlinevelocity.api.offlineVelocityApi
import org.slf4j.Logger

val plugin: OfflineVelocityPlugin get() = OfflineVelocityPlugin.instance

@Plugin(
    id = "offlinevelocityexample",
    name = "OfflineVelocityExample",
    version = "1.0.0",
    description = "A example plugin for OfflineVelocity",
    url = "github.com/TheBjoRedCraft/OfflineVelocity",
    authors = ["TheBjoRedCraft"]
)
class OfflineVelocityPlugin
@Inject
constructor (
    val logger: Logger,
    val proxy: ProxyServer,
    suspendingPluginContainer: SuspendingPluginContainer
) {
    init {
        suspendingPluginContainer.initialize(this)

        instance = this
    }

    @Subscribe
    suspend fun onProxyInitialization(event: ProxyInitializeEvent) {
        logger.info("You can display, how man offline players are in the database here: ${offlineVelocityApi.getOfflineUsers().size}")
        logger.info("You can get a random offline player name here: ${offlineVelocityApi.getName(offlineVelocityApi.getOfflineUsers().first()) ?: "N/A"}")
        logger.info("You can get a random offline player uuid here: ${offlineVelocityApi.getUuid(offlineVelocityApi.getName(offlineVelocityApi.getOfflineUsers().first()) ?: "N/A")}")
    }

    @Subscribe
    suspend fun onProxyShutdown(event: ProxyShutdownEvent) {

    }

    companion object {
        lateinit var instance: OfflineVelocityPlugin
    }
}
