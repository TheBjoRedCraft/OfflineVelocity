package dev.thebjoredcraft.offlinevelocity.database

import dev.thebjoredcraft.offlinevelocity.player.PlayerData
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.UUID

object DatabaseService {
    private lateinit var connection: Connection

    fun connect() {
        val url = "jdbc:sqlite:player-storage.db"

        connection = DriverManager.getConnection(url)

        val statement = connection.createStatement()
        val sql = """
            CREATE TABLE IF NOT EXISTS players (
                uuid TEXT PRIMARY KEY,
                name TEXT NOT NULL
            );
        """
        statement.execute(sql)
    }

    suspend fun loadPlayer(uuid: UUID): PlayerData {
        val sql = "SELECT * FROM players WHERE uuid = ?"

        val preparedStatement = connection.prepareStatement(sql)

        preparedStatement.setString(1, uuid.toString())

        val resultSet: ResultSet = preparedStatement.executeQuery()
        return if (resultSet.next()) {
            PlayerData(UUID.fromString(resultSet.getString("uuid")), resultSet.getString("name"), true)
        } else {
            PlayerData(uuid, "Unknown", false)
        }
    }

    suspend fun savePlayer(playerData: PlayerData) {
        val sql = "INSERT OR REPLACE INTO players(uuid, name) VALUES(?, ?)"
        val preparedStatement = connection.prepareStatement(sql)

        preparedStatement.setString(1, playerData.uuid.toString())
        preparedStatement.setString(2, playerData.name)
        preparedStatement.executeUpdate()
    }

    fun disconnect() {
        connection.close()
    }

    suspend fun getUUID(name: String): UUID? {
        val sql = "SELECT uuid FROM players WHERE name = ?"
        val preparedStatement = connection.prepareStatement(sql)

        preparedStatement.setString(1, name)

        val resultSet: ResultSet = preparedStatement.executeQuery()
        return if (resultSet.next()) {
            UUID.fromString(resultSet.getString("uuid"))
        } else {
            null
        }
    }
}