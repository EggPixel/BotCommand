package tech.egglink.bot.core.command.group

import org.bukkit.ChatColor
import tech.egglink.bot.internal.commands.Command
import tech.egglink.bot.internal.commands.Commands
import tech.egglink.bot.internal.commands.Sender
import tech.egglink.bot.untils.Untils

//Usage: !permission <id> <add/remove> <permission>
@Command(name = "permission", usage = "command.permission.usage", description = "command.permission.description", permission = "bot.cmd.permission", argsCount = 3)
class CommandPermission: Commands() {
    override suspend fun onCommand(sender: Sender, args: List<String>, all: String): Boolean {
        val id = try { args[0].toLong() } catch (e: NumberFormatException) {
            return false
        }
        val type = args[1]
        val permission = args[2]
        val event = sender.getGroup() ?: return true
        when (type) {
            "add" -> {
                val config = Untils.getUserData(id)
                if (permission !in Untils.permissionList) {
                    return false
                }
                if (permission in config.permissionTrue) {
                    event.group.sendMessage(Untils.config.commands.alreadyExistsPermission)
                } else {
                    config.permissionTrue.add(permission)
                    config.save()
                    ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', Untils.config.message.doSuccessfully))
                        ?.let { event.group.sendMessage(it) }
                }
            }
            "remove" -> {
                val config = Untils.getUserData(id)
                if (permission !in Untils.permissionList) {
                    return false
                }
                if (permission !in config.permissionTrue) {
                    event.group.sendMessage(Untils.config.commands.notExistsPermission)
                } else {
                    config.permissionTrue.remove(permission)
                    config.save()
                    ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', Untils.config.message.doSuccessfully))
                        ?.let { event.group.sendMessage(it) }
                }
            }
            else -> return false
        }
        return true
    }

}