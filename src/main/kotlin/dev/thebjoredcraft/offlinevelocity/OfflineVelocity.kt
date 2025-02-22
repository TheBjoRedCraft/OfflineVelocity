package dev.thebjoredcraft.offlinevelocity

import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.github.shynixn.mccoroutine.velocity.registerSuspend
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import dev.thebjoredcraft.offlinevelocity.database.DatabaseService
import dev.thebjoredcraft.offlinevelocity.listener.PlayerConnectionHandler
import org.slf4j.Logger
import java.nio.file.Path

val plugin: OfflineVelocity get() = OfflineVelocity.instance

@Plugin(
    id = "offlinevelocity",
    name = "OfflineVelocity",
    version = "1.0.0",
    description = "A modern asynchronous Velocity OfflinePlayer support API",
    url = "github.com/TheBjoRedCraft/OfflineVelocity",
    authors = ["TheBjoRedCraft"]
)
class OfflineVelocity
@Inject
constructor (
    val logger: Logger,
    val proxy: ProxyServer,
    @DataDirectory val dataDirectory: Path,
    suspendingPluginContainer: SuspendingPluginContainer
) {
    init {
        suspendingPluginContainer.initialize(this)

        instance = this
    }

    @Subscribe
    suspend fun onProxyInitialization(event: ProxyInitializeEvent) {
        DatabaseService.connect()

        proxy.eventManager.registerSuspend(this, PlayerConnectionHandler())
    }

    @Subscribe
    suspend fun onProxyShutdown(event: ProxyShutdownEvent) {
        DatabaseService.disconnect()
    }

    companion object {
        lateinit var instance: OfflineVelocity
        var dev: Boolean = false
    }
}
