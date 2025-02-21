package dev.thebjoredcraft.offlinevelocity.database

import com.velocitypowered.api.proxy.Player

import dev.thebjoredcraft.offlinevelocity.OfflineVelocity
import dev.thebjoredcraft.offlinevelocity.player.PlayerData

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

import java.util.UUID

object DatabaseService {
    private lateinit var connection: Connection
    private val path: String = "${OfflineVelocity.instance.dataDirectory}/player-storage.db"

    fun connect() {
        val url = "jdbc:sqlite:$path"

        connection = DriverManager.getConnection(url)

        val statement = connection.createStatement()
        val sql = """
            CREATE TABLE IF NOT EXISTS players (
                uuid TEXT PRIMARY KEY,
                name TEXT NOT NULL
            );
        """
        statement.execute(sql)

        OfflineVelocity.instance.logger.info("OV > Connected to SQLite database.")
    }

    suspend fun loadPlayer(uuid: UUID): PlayerData {
        withContext(Dispatchers.IO) {
            val sql = "SELECT * FROM players WHERE uuid = ?"

            val preparedStatement = connection.prepareStatement(sql)

            preparedStatement.setString(1, uuid.toString())

            val resultSet: ResultSet = preparedStatement.executeQuery()

            if(OfflineVelocity.instance.dev) {
                OfflineVelocity.instance.logger.info("OV > Loaded player with UUID $uuid from SQLite database.")
            }

            return@withContext if (resultSet.next()) {
                PlayerData(UUID.fromString(resultSet.getString("uuid")), resultSet.getString("name"), true)
            } else {
                createNotExistingUser()
            }
        }

        return createNotExistingUser()
    }

    suspend fun saveIfNotExists(player: Player) {
        withContext(Dispatchers.IO) {
            if(isUnknownUser(player)) {
                return@withContext
            }

            val sql = "INSERT OR IGNORE INTO players(uuid, name) VALUES(?, ?)"
            val preparedStatement = connection.prepareStatement(sql)

            preparedStatement.setString(1, player.uniqueId.toString())
            preparedStatement.setString(2, player.username)
            preparedStatement.executeUpdate()


            if(OfflineVelocity.instance.dev) {
                OfflineVelocity.instance.logger.info("OV > Saved player ${player.username} to SQLite database.")
            }
        }
    }

    fun disconnect() {
        connection.close()

        OfflineVelocity.instance.logger.info("OV > Disconnected from SQLite database.")
    }

    suspend fun getUUID(name: String): UUID? {
        withContext(Dispatchers.IO) {
            val sql = "SELECT uuid FROM players WHERE name = ?"
            val preparedStatement = connection.prepareStatement(sql)

            preparedStatement.setString(1, name)

            val resultSet: ResultSet = preparedStatement.executeQuery()

            if(OfflineVelocity.instance.dev) {
                OfflineVelocity.instance.logger.info("OV > Loaded player with name $name from SQLite database.")
            }

            return@withContext if (resultSet.next()) {
                UUID.fromString(resultSet.getString("uuid"))
            } else {
                null
            }
        }

        return null;
    }

    private fun createNotExistingUser() : PlayerData {
        return PlayerData(UUID.randomUUID(), "Unknown", false)
    }

    private fun isUnknownUser(player: Player) : Boolean {
        return player.username == "Unknown"
    }
}