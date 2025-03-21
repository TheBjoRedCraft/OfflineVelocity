package dev.thebjoredcraft.offlinevelocity.velocity.implementation

import com.google.auto.service.AutoService
import com.zaxxer.hikari.HikariDataSource
import dev.thebjoredcraft.offlinevelocity.api.`object`.User
import dev.thebjoredcraft.offlinevelocity.core.DatabaseService
import net.kyori.adventure.util.Services.Fallback
import org.jetbrains.exposed.sql.Database
import java.nio.file.Path
import java.util.*
import kotlin.io.path.absolutePathString
import kotlin.io.path.createFile
import kotlin.io.path.div
import kotlin.io.path.notExists

@AutoService(DatabaseService::class)
class VelocityDatabaseService(configDirectory: Path, private val storageDirectory: Path): DatabaseService, Fallback {
    private lateinit var connection: Database

    override fun connect() {
        if (::connection.isInitialized && !connection.connector().isClosed) {
            disconnect()
        }

        when (config.storageMethod.lowercase()) {
            "local" -> {
                connectLocal()
            }

            "external" -> {
                connectExternal()
            }

            else -> {
                log.atWarning().log(
                    "Unknown storage method '%s'. Using local storage...",
                    config.storageMethod
                )

                connectLocal()
            }
        }
    }

    override suspend fun getUser(uuid: UUID): User? {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(name: String): User? {
        TODO("Not yet implemented")
    }

    private fun connectLocal() {
        Class.forName("org.sqlite.JDBC")
        val dbFile = storageDirectory / "storage.db"

        if (dbFile.notExists()) {
            dbFile.createFile()
        }

        connectUsingHikari(
            "sqlite",
            "org.sqlite.JDBC",
            "",
            0,
            "",
            "",
            "",
            config.hikari,
            "jdbc:sqlite:file:${dbFile.absolutePathString()}",
        )

        log.atInfo().log("Successfully connected to database with sqlite!")
    }

    private fun connectExternal() {


        connectUsingHikari(
            external.connector,
            external.driver,
            external.hostname,
            external.port,
            external.database,
            external.username,
            external.password,
            hikari
        )
    }

    private fun connectUsingHikari(
        connector: String,
        driver: String,
        hostname: String,
        port: Int,
        database: String,
        username: String,
        password: String,
        hikari: DatabaseHikariConfig,
        url: String = "jdbc:${connector}://${hostname}:${port}/${database}"
    ) {
        Class.forName(driver)

        val hikariConfig = HikariConfig().apply {
            this.jdbcUrl = url
            this.username = username
            this.password = password
            this.minimumIdle = hikari.minimumIdle
            this.maximumPoolSize = hikari.maximumPoolSize
            this.idleTimeout = hikari.idleTimeout
            this.connectionTimeout = hikari.connectionTimeout
            this.maxLifetime = hikari.maxLifetime
            this.driverClassName = driver
            this.isAutoCommit = false
            this.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            validate()
        }

        connection = Database.connect(HikariDataSource(hikariConfig))
    }

    override fun disconnect() {
        if (!::connection.isInitialized || connection.connector().isClosed) {
            return
        }

        connection.connector().close()
    }
}