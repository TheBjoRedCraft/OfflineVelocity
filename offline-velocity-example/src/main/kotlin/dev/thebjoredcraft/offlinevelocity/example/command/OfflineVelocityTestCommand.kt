package dev.thebjoredcraft.offlinevelocity.example.command

import com.github.shynixn.mccoroutine.velocity.launch
import com.velocitypowered.api.command.SimpleCommand
import dev.thebjoredcraft.offlinevelocity.api.offlineVelocityApi
import dev.thebjoredcraft.offlinevelocity.example.plugin

class OfflineVelocityTestCommand(): SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()

        plugin.pluginContainer.pluginContainer.launch {
            val header = "§7-------- §fOffline Velocity Example §7--------"
            val footer = "§7--------------------------------------"

            val offlineUsers = offlineVelocityApi.getOfflineUsers()
            val amount = when(offlineUsers.isNotEmpty()) {
                true -> offlineUsers.size
                false -> "N/A"
            }
            val randomUuid = when(offlineUsers.isNotEmpty()) {
                true ->  offlineUsers.firstOrNull() ?: "N/A"
                false -> "N/A"
            }
            val randomName = when(offlineUsers.isNotEmpty()) {
                true ->  offlineVelocityApi.getName(offlineUsers.random()) ?: "N/A"
                false -> "N/A"
            }

            source.sendPlainMessage(header)
            source.sendPlainMessage("§7Version: §av1.0.0")
            source.sendPlainMessage("")
            source.sendPlainMessage("§7There are §a$amount §7players in the database.")
            source.sendPlainMessage("§7Random UUID: §a$randomUuid")
            source.sendPlainMessage("§7Random Name: §a$randomName")
            source.sendPlainMessage("")
            source.sendPlainMessage("§cby TheBjoRedCraft")
            source.sendPlainMessage(footer)
        }

    }
}