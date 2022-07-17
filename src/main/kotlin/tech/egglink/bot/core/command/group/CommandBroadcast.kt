package tech.egglink.bot.core.command.group

import net.mamoe.mirai.event.events.GroupMessageEvent
import org.bukkit.ChatColor
import tech.egglink.bot.Config
import tech.egglink.bot.internal.commands.Command
import tech.egglink.bot.internal.commands.Commands
import tech.egglink.bot.internal.commands.Sender
import tech.egglink.bot.untils.Untils

@Command("broadcast", "command.broadcast.usage", "command.broadcast.description", permission = "bot.cmd.broadcast")
class CommandBroadcast: Commands() {
    override suspend fun onCommand(sender: Sender, args: List<String>, all: String): Boolean {
        val config: Config = Untils.config
        val event: GroupMessageEvent = sender.getGroup() ?: throw IllegalStateException("Not a group message")
        config.appendDebugFile(event.senderName,"尝试使用群聊命令: $all")
        if (args.size < 2) {
            return false
        }
        try {
            val times = Integer.valueOf(args[0])
            val messages = all.substring(10).substring(times.toString().length + 1)
            Untils().broadcast(messages, times)
        } catch (e: java.lang.NumberFormatException) {
            event.group.sendMessage(config.group.notANumber)
        }
        return true
    }
}