package dev.thebjoredcraft.offlinevelocity.velocity.config

import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.io.File

object ConfigService {
    private lateinit var config: ConfigurationNode
    private val configFile = File("plugins/YOUR_PLUGIN_NAME/config.conf")

    fun loadConfig() {
        val loader = HoconConfigurationLoader.builder().file(configFile).build()
        config = loader.load()

        if (config.node("server", "motd").virtual()) {
            config.node("server", "motd").set("Willkommen auf dem Proxy!")
        }
        if (config.node("server", "max-players").virtual()) {
            config.node("server", "max-players").set(100)
        }

        saveConfig()
    }

    fun saveConfig() {
        val loader = HoconConfigurationLoader.builder().file(configFile).build()
        loader.save(config)
    }

    fun getMotd(): String = config.node("server", "motd").getString("Willkommen auf dem Proxy!")
    fun getMaxPlayers(): Int = config.node("server", "max-players").getInt(100)
}