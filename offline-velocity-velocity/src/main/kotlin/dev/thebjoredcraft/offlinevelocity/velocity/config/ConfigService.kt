package dev.thebjoredcraft.offlinevelocity.velocity.config

import dev.thebjoredcraft.offlinevelocity.velocity.plugin
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.io.File

object ConfigService {
    private lateinit var config: ConfigurationNode
    private val configFile = File(plugin.dataDirectory.toFile(), "config.conf")

    fun loadConfig() {
        if (!configFile.exists()) {
            configFile.createNewFile()
        }

        val loader = HoconConfigurationLoader.builder().file(configFile).build()
        config = loader.load()

        if (config.node("storage-method").virtual()) {
            config.node("storage-method").set("local")
        }

        if (config.node("database-external", "connector").virtual()) {
            config.node("database-external", "connector").set("mysql")
        }
        if (config.node("database-external", "driver").virtual()) {
            config.node("database-external", "driver").set("mysql driver //TODO")
        }
        if (config.node("database-external", "hostname").virtual()) {
            config.node("database-external", "hostname").set("localhost")
        }
        if (config.node("database-external", "port").virtual()) {
            config.node("database-external", "port").set("3006")
        }
        if (config.node("database-external", "database").virtual()) {
            config.node("database-external", "database").set("offline_velocity")
        }
        if (config.node("database-external", "username").virtual()) {
            config.node("database-external", "username").set("root")
        }
        if (config.node("database-external", "password").virtual()) {
            config.node("database-external", "password").set("password")
        }

        saveConfig()
    }

    private fun saveConfig() {
        val loader = HoconConfigurationLoader.builder().file(configFile).build()
        loader.save(config)
    }

    fun getStorageMethod(): String = config.node("storage-method").getString("local")
    fun getExternalConnector(): String = config.node("database-external", "connector").getString("mysql")
    fun getExternalDriver(): String = config.node("database-external", "driver").getString("com.mysql.cj.jdbc.Driver")
    fun getExternalHostname(): String = config.node("database-external", "hostname").getString("localhost")
    fun getExternalPort(): Int = config.node("database-external", "port").getInt(3006)
    fun getExternalDatabase(): String = config.node("database-external", "database").getString("offline_velocity")
    fun getExternalUsername(): String = config.node("database-external", "username").getString("root")
    fun getExternalPassword(): String = config.node("database-external", "password").getString("password")
}