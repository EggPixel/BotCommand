package tech.egglink.bot.core.command.group

import net.mamoe.mirai.event.events.GroupMessageEvent
import org.bukkit.ChatColor
import tech.egglink.bot.Config
import tech.egglink.bot.internal.WhitelistSupport
import tech.egglink.bot.internal.commands.Command
import tech.egglink.bot.internal.commands.Commands
import tech.egglink.bot.internal.commands.Sender
import tech.egglink.bot.untils.Untils

@Command(name = "whitelist-add", usage = "command.whitelist-add.usage", description = "command.whitelist-add.description")
class CommandWhitelistAdd: Commands() {
    override suspend fun onCommand(sender: Sender, args: List<String>, all: String): Boolean {
        val event: GroupMessageEvent = sender.getGroup() ?: throw IllegalStateException("Sender is not in group")
        val config: Config = Untils.config
        config.appendDebugFile(event.senderName,"尝试使用群聊命令: $all")
        if (event.sender.id !in config.administrator) {
            ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&',config.message.noPermission))
                ?.let { it1 -> event.group.sendMessage(it1) }
            return true
        }
        if (args.size != 1 && args.size != 2) {
            return false
        }
        // args[0] 为 bindName
        // args[1] 为 bindID
        if (args.size == 2) {
            when (WhitelistSupport().forceAppendWhitelist(args[0], java.lang.Long.parseLong(args[1]))) {
                WhitelistSupport.Status.Successful -> {
                    event.group.sendMessage(config.group.whitelistAppendSuccessfully.replace("\$name", args[0]))
                }
                WhitelistSupport.Status.DisableWhitelist -> {
                    event.group.sendMessage(config.group.whitelistDisable)
                }
                else -> {
                    //发送添加失败信息
                    event.group.sendMessage(config.group.whitelistAppendFailed.replace("\$name", args[0]))
                }
            }
        } else {
            when (WhitelistSupport().forceAppendWhitelist(args[0])) {
                WhitelistSupport.Status.Successful -> {
                    event.group.sendMessage(config.group.whitelistAppendSuccessfully.replace("\$name", args[0]))
                }
                WhitelistSupport.Status.DisableWhitelist -> {
                    event.group.sendMessage(config.group.whitelistDisable)
                }
                else -> {
                    //发送添加失败信息
                    event.group.sendMessage(config.group.whitelistAppendFailed.replace("\$name", args[0]))
                }
            }
        }
        return true
    }

}