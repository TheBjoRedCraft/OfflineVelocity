package dev.thebjoredcraft.offlinevelocity.velocity


import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.github.shynixn.mccoroutine.velocity.launch
import com.github.shynixn.mccoroutine.velocity.registerSuspend
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import dev.thebjoredcraft.offlinevelocity.api.offlineVelocityApi
import dev.thebjoredcraft.offlinevelocity.core.databaseService
import dev.thebjoredcraft.offlinevelocity.velocity.config.ConfigService
import dev.thebjoredcraft.offlinevelocity.velocity.listener.PlayerConnectionHandler
import org.slf4j.Logger
import java.nio.file.Path

val plugin: OfflineVelocityPlugin get() = OfflineVelocityPlugin.instance

@Plugin(
    id = "offlinevelocity",
    name = "OfflineVelocity",
    version = "2.0.0",
    description = "A modern asynchronous Velocity OfflinePlayer support API",
    url = "github.com/TheBjoRedCraft/OfflineVelocity",
    authors = ["TheBjoRedCraft"],
)
class OfflineVelocityPlugin
@Inject
constructor (
    val logger: Logger,
    val proxy: ProxyServer,
    @DataDirectory val dataDirectory: Path,

    val suspendingPluginContainer: SuspendingPluginContainer
) {
    init {
        suspendingPluginContainer.initialize(this)
        instance = this
    }

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        ConfigService.loadConfig()
        databaseService.connect()

        proxy.eventManager.register(this, PlayerConnectionHandler())
    }

    @Subscribe
    suspend fun onProxyShutdown(event: ProxyShutdownEvent) {
        databaseService.disconnect()
    }

    companion object {
        lateinit var instance: OfflineVelocityPlugin
    }
}
