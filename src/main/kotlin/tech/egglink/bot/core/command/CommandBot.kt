package tech.egglink.bot.core.command

import io.ktor.util.reflect.*
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tech.egglink.bot.Config
import tech.egglink.bot.internal.BotLoginSolver
import tech.egglink.bot.untils.Untils

class CommandBot: CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, arg: String, args: Array<out String>): Boolean {
        val config: Config = Untils.config
        if (args.isEmpty()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.botCommand.usage))
            return true
        }
        if (!sender.hasPermission("botcommand.bot.use")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.noPermission))
            return true
        }
        when (args[0]) {
            "logout" -> {  // 退出登录
                if (!sender.hasPermission("botcommand.bot.logout")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.noPermission))
                    return true
                }
                if (Untils.mirai.logout()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.doSuccessfully))
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.failedToDo))
                }
                return true
            }
            "login" -> {  // 登陆
                if (!sender.hasPermission("botcommand.bot.login")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.noPermission))
                    return true
                }
                if (Untils.mirai.login(config.userID, config.userPassword)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.doSuccessfully))
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.failedToDo))
                }
                return true
            }
            "version" -> { // 版本信息
                if (!sender.hasPermission("botcommand.bot.version")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.noPermission))
                    return true
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.versionText.replace("\$version", Untils.version.toString())))
                return true
            }
            "reload" -> {  // 重载
                if (!sender.hasPermission("botcommand.bot.reload")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.noPermission))
                    return true
                }
                Untils().getConfiguration()
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.doSuccessfully))
                return true
            }
            "log" -> {  // 更新日志
                if (!sender.hasPermission("botcommand.bot.log")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.noPermission))
                    return true
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.updateLog))
                return true
            }
            "chat" -> {  // 发送消息
                if (!sender.hasPermission("botcommand.bot.chat")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.noPermission))
                    return true
                }
                if (!sender.instanceOf(Player::class)) {
                    sender.sendMessage(
                        ChatColor.translateAlternateColorCodes('&', config.message.noConsole))
                    return true
                }
                val st: StringBuilder = StringBuilder()
                for (i in args) {
                    if (args.indexOf(i) == 0)
                        continue
                    st.append(st).append(" ")
                }
                suspend {
                    for (group in Untils.config.sendGroup) {
                        Untils.mirai.getGroup(group)?.let {
                            Untils().chat(st.toString(), it, sender.name)
                        }
                    }
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.doSuccessfully))
                }
                return true
            }
            "pic" -> {
                if (args.size == 2) {
                    if (args[1] == "cancel") {
                        BotLoginSolver.solvePicCaptcha(Untils.mirai.getBot().id, true)
                    } else {
                        BotLoginSolver.solvePicCaptcha(Untils.mirai.getBot().id, args[1])
                    }
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.doSuccessfully))
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.failedToDo))
                }
                return true
            }
            "slider" -> {
                if (args.size == 2) {
                    if (args[1] == "cancel") {
                        BotLoginSolver.solveSliderCaptcha(Untils.mirai.getBot().id, true)
                    } else {
                        BotLoginSolver.solveSliderCaptcha(Untils.mirai.getBot().id, args[1])
                    }
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.doSuccessfully))
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.failedToDo))
                }
                return true
            }
            "unsafe" -> {
                when (args.size) {
                    2 -> {
                        return if (args[1] == "cancel") {
                            BotLoginSolver.solveUnsafeDeviceLoginVerify(Untils.mirai.getBot().id, true)
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.doSuccessfully))
                            true
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.failedToDo))
                            true
                        }
                    }
                    1 -> {
                        BotLoginSolver.solveUnsafeDeviceLoginVerify(Untils.mirai.getBot().id, false)
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.doSuccessfully))
                    }
                    else -> {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.failedToDo))
                    }
                }
                return true
            }
            else -> {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.botCommand.usage))
                return true
            }
        }
    }
}