package dev.thebjoredcraft.offlinevelocity

import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import dev.thebjoredcraft.offlinevelocity.database.DatabaseService
import org.slf4j.Logger
import java.nio.file.Path

@Plugin(
    id = "offline-velocity",
    name = "OfflineVelocity",
    version = "1.0.0",
    description = "A modern asynchronous Velocity OfflinePlayer support API",
    url = "github.com/TheBjoRedCraft/OfflineVelocity",
    authors = ["TheBjoRedCraft"]
)
class OfflineVelocity @Inject constructor(
    val logger: Logger,
    val proxy: ProxyServer,
    @DataDirectory dataDirectory: Path,
    suspendingPluginContainer: SuspendingPluginContainer
) {
    init {
        suspendingPluginContainer.initialize(this)

        instance = this
    }

    @Subscribe
    suspend fun onProxyInitialization(event: ProxyInitializeEvent) {
        DatabaseService.connect()
    }

    @Subscribe
    suspend fun onProxyShutdown(event: ProxyShutdownEvent) {
        DatabaseService.disconnect()
    }

    companion object {
        lateinit var instance: OfflineVelocity
    }
}
