package dev.thebjoredcraft.offlinevelocity.example

import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import dev.thebjoredcraft.offlinevelocity.api.offlineVelocityApi
import dev.thebjoredcraft.offlinevelocity.example.command.OfflineVelocityTestCommand
import org.slf4j.Logger

val plugin: OfflineVelocityExamplePlugin get() = OfflineVelocityExamplePlugin.instance

@Plugin(
    id = "offlinevelocityexample",
    name = "OfflineVelocityExample",
    version = "1.0.0",
    description = "A example plugin for OfflineVelocity",
    url = "github.com/TheBjoRedCraft/OfflineVelocity",
    authors = ["TheBjoRedCraft"],
    dependencies = [
        Dependency(id = "offlinevelocity", )
    ]
)



class OfflineVelocityExamplePlugin
@Inject
constructor (
    private val logger: Logger,
    val proxyServer: ProxyServer,
    val pluginContainer: SuspendingPluginContainer
) {
    init {
        pluginContainer.initialize(this)
        instance = this
    }

    @Subscribe
    suspend fun onProxyInitialization(event: ProxyInitializeEvent) {
        val commandManager = proxyServer.commandManager

        commandManager.register(commandManager.metaBuilder("offlinevelocitytest").build(), OfflineVelocityTestCommand())
    }

    @Subscribe
    suspend fun onProxyShutdown(event: ProxyShutdownEvent) {

    }

    companion object {
        lateinit var instance: OfflineVelocityExamplePlugin
    }
}
