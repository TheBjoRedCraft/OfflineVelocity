package dev.thebjoredcraft.offlinevelocity.velocity.implementation

import com.google.auto.service.AutoService
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.thebjoredcraft.offlinevelocity.api.`object`.User
import dev.thebjoredcraft.offlinevelocity.core.DatabaseService
import dev.thebjoredcraft.offlinevelocity.velocity.config.ConfigService
import dev.thebjoredcraft.offlinevelocity.velocity.info
import dev.thebjoredcraft.offlinevelocity.velocity.plugin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.kyori.adventure.util.Services.Fallback
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

import java.util.*
import kotlin.io.path.absolutePathString
import kotlin.io.path.createFile
import kotlin.io.path.div
import kotlin.io.path.notExists
import kotlin.text.insert

@AutoService(DatabaseService::class)
class VelocityDatabaseService: DatabaseService, Fallback {
    private lateinit var connection: Database

    object Users : Table() {
        val uuid = text("uuid").transform({ UUID.fromString(it) }, { it.toString() })
        val name = text("name")

        override val primaryKey = PrimaryKey(uuid)
    }

    override fun connect() {
        if (::connection.isInitialized && !connection.connector().isClosed) {
            disconnect()
        }

        val storageMethod = ConfigService.getStorageMethod()

        when (storageMethod.lowercase()) {
            "local" -> {
                connectLocal()
            }

            "external" -> {
                connectExternal()
            }

            else -> {
                dev.thebjoredcraft.offlinevelocity.velocity.error("Unknown storage method '$storageMethod'. Using local storage...")

                connectLocal()
            }
        }

         transaction {
             SchemaUtils.create(Users)
         }
    }

    override suspend fun getUser(uuid: UUID): User? {
        return withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                Users.selectAll().where { Users.uuid eq uuid }.firstNotNullOfOrNull { row ->
                    User(
                        uuid = row[Users.uuid],
                        name = row[Users.name]
                    )
                }
            }
        }
    }

    override suspend fun getUser(name: String): User? {
        return withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                Users.selectAll().where { Users.name eq name }.firstNotNullOfOrNull { row ->
                    User(
                        uuid = row[Users.uuid],
                        name = row[Users.name]
                    )
                }
            }
        }
    }

    override suspend fun getOfflineUsers(): Set<UUID> {
        return withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                Users.selectAll()
                    .map { row -> row[Users.uuid] }
                    .toSet()
            }
        }
    }

    override suspend fun saveIfNotExists(uuid: UUID, name: String) {
        withContext(Dispatchers.IO) {
            newSuspendedTransaction {
                val existingUser = Users.select ( Users.uuid eq uuid ).firstOrNull()

                if (existingUser == null) {
                    Users.insert {
                        it[Users.uuid] = uuid
                        it[Users.name] = name
                    }
                } else if (existingUser[Users.name] != name) {
                    Users.update({ Users.uuid eq uuid }) {
                        it[Users.name] = name
                    }
                } else {
                    return@newSuspendedTransaction
                }
            }
        }
    }

    private fun connectLocal() {
        Class.forName("org.sqlite.JDBC")
        val dbFile = plugin.dataDirectory / "storage.db"

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
            "jdbc:sqlite:file:${dbFile.absolutePathString()}",
        )

        info("Successfully connected to database with sqlite!")
    }

    private fun connectExternal() {
        connectUsingHikari(
            ConfigService.getExternalConnector(),
            ConfigService.getExternalDriver(),
            ConfigService.getExternalHostname(),
            ConfigService.getExternalPort(),
            ConfigService.getExternalDatabase(),
            ConfigService.getExternalUsername(),
            ConfigService.getExternalPassword()
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
        url: String = "jdbc:${connector}://${hostname}:${port}/${database}"
    ) {
        Class.forName(driver)

        val hikariConfig = HikariConfig().apply {
            this.jdbcUrl = url
            this.username = username
            this.password = password
            this.driverClassName = driver
            this.isAutoCommit = false
            this.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            validate()
        }

        connection = Database.connect(HikariDataSource(hikariConfig))

        info("Successfully connected to database with $connector!")
    }

    override fun disconnect() {
        if (!::connection.isInitialized || connection.connector().isClosed) {
            return
        }

        connection.connector().close()
    }
}