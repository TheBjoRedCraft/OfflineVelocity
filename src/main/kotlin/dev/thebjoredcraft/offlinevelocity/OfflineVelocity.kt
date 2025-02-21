package dev.thebjoredcraft.offlinevelocity;

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
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
class OfflineVelocity @Inject constructor(val logger: Logger, @DataDirectory val dataDirectory: Path) {
    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {

    }
}
