package tech.egglink.bot.core.command.group

import net.mamoe.mirai.contact.Group
import org.bukkit.Bukkit
import tech.egglink.bot.Config
import tech.egglink.bot.internal.commands.Command
import tech.egglink.bot.internal.commands.Commands
import tech.egglink.bot.internal.commands.Sender
import tech.egglink.bot.untils.Untils

@Command(name = "online", usage = "command.online.usage", description = "command.online.description", 0)
class CommandOnline: Commands() {
    override suspend fun onCommand(sender: Sender, args: List<String>, all: String): Boolean {
        val group: Group? = sender.getGroup()?.group
        val config: Config = Untils.config
        val players: StringBuilder = StringBuilder()
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            players.append(config.group.nothing)
            group?.sendMessage(config.group.onlinePlayer.replace("\$player", players.toString()))
        } else {
            Bukkit.getOnlinePlayers().forEach {
                players.append(it.name).append(",")
            }
            players.deleteAt(players.length-1)
            group?.sendMessage(config.group.onlinePlayer.replace("\$player", players.toString()))
        }
        return true
    }

}