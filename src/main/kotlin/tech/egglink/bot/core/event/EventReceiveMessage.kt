package tech.egglink.bot.core.event

import kotlinx.coroutines.CompletableJob
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.toPlainText
//import org.bukkit.Bukkit
//import org.bukkit.ChatColor
import tech.egglink.bot.Config
import tech.egglink.bot.core.command.ProcessCommands
//import tech.egglink.bot.internal.WhitelistSupport
import tech.egglink.bot.untils.Untils
//import java.util.Calendar
//import kotlin.random.Random

class EventReceiveMessage {
    private lateinit var listener: CompletableJob

    fun start() {
        listener = Untils.mirai.getBot().eventChannel.subscribeAlways<GroupMessageEvent> { event ->
            // message.content.toPlainText().content // 获取信息
            val config: Config = Untils.config
            if (group.id !in config.sendGroup)
                return@subscribeAlways
            val message = message.content.toPlainText().content
            if (!message.startsWith("!")) {
                if (message.startsWith(config.chatPrefix)) {  // 群服共通
                    Untils().chat(message.substring(config.chatPrefix.length), sender)
                }
                return@subscribeAlways
            }
            if (sender.id in config.blackList) {
                return@subscribeAlways
            }
            if (!ProcessCommands().run(message.substring(1), event)) {
                group.sendMessage(Untils.regCmd.getCommand(message.substring(1).split(" ")[0])
                    ?.let { Untils.regCmd.getUsage(it) } ?: "none")
                return@subscribeAlways
            } else {
                // 命令执行成功
                return@subscribeAlways
            }
//            when (message) {
//                "!online" -> {  // 在线信息
//                    val players: StringBuilder = StringBuilder()
//                    if (Bukkit.getOnlinePlayers().isEmpty()) {
//                        players.append(config.group.nothing)
//                        group.sendMessage(config.group.onlinePlayer.replace("\$player", players.toString()))
//                    } else {
//                        Bukkit.getOnlinePlayers().forEach {
//                            players.append(it.name).append(",")
//                        }
//                        players.deleteAt(players.length-1)
//                        group.sendMessage(config.group.onlinePlayer.replace("\$player", players.toString()))
//                    }
//                }
//                "!lucky" -> {  // 幸运值
//                    val luckyValue: Int = (0..100).random(Random(sender.id +
//                            Calendar.getInstance().get(Calendar.DAY_OF_YEAR) * Calendar.getInstance().get(Calendar.YEAR)))
//                    group.sendMessage(config.group.luckyValue.replace("\$lucky", luckyValue.toString()))
//                }
//            }
//            if (message.startsWith("!broadcast ")) {  // 广播一条消息
//                if (sender.id !in config.administrator) {
//                    ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&',config.message.noPermission))
//                        ?.let { it1 -> group.sendMessage(it1) }
//                    return@subscribeAlways
//                }
//                val args = message.substring(11).split(" ")
//                if (args.size < 2) {
//                    return@subscribeAlways
//                }
//                try {
//                    val times = Integer.valueOf(args[0])
//                    val messages = message.substring(11).substring(times.toString().length + 1)
//                    Untils().broadcast(messages, times)
//                } catch (e: java.lang.NumberFormatException) {
//                    group.sendMessage(config.group.notANumber)
//                }
//            }
//            if (message.startsWith("!whitelist ")) {  // 白名单
//                val args: List<String> = message.substring(11).split(" ")
//                if (args.size != 1 && args.size != 2) {
//                    return@subscribeAlways
//                }
//                // args[0] 为 bindName
//                // args[1] 为 override?
//                when (WhitelistSupport().appendWhitelist(args[0],sender.id)) {
//                    WhitelistSupport.Status.NoOverride -> {
//                        if (args.size == 2 && args[1] == "OVERRIDE") {
//                            if (WhitelistSupport().appendWhitelist(
//                                    args[0],
//                                    sender.id,
//                                    override = true
//                                ) == WhitelistSupport.Status.Successful
//                            ) {
//                                //发送成功添加信息
//                                group.sendMessage(config.group.whitelistAppendSuccessfully.replace("\$name", args[0]))
//                            } else {
//                                //发送添加失败信息
//                                group.sendMessage(config.group.whitelistAppendFailed.replace("\$name", args[0]))
//                            }
//                        } else {
//                            //发送没有重载信息
//                            group.sendMessage(config.group.whitelistAlreadyExists.replace("\$already", WhitelistSupport().hasWhitelist(sender.id)))
//                        }
//                    }
//                    WhitelistSupport.Status.DisableWhitelist -> {
//                        group.sendMessage(config.group.whitelistDisable)
//                    }
//                    else -> {
//                        // 发送成功信息
//                        group.sendMessage(config.group.whitelistAppendSuccessfully.replace("\$name", args[0]))
//                    }
//                }
//            }
//            if (message.startsWith("!whitelist-add ")) {  // 强制白名单
//                if (sender.id !in config.administrator) {
//                    ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&',config.message.noPermission))
//                        ?.let { it1 -> group.sendMessage(it1) }
//                    return@subscribeAlways
//                }
//                val args: List<String> = message.substring(15).split(" ")
//                if (args.size != 1 && args.size != 2) {
//                    return@subscribeAlways
//                }
//                // args[0] 为 bindName
//                // args[1] 为 bindID
//                if (args.size == 2) {
//                    when (WhitelistSupport().forceAppendWhitelist(args[0], java.lang.Long.parseLong(args[1]))) {
//                        WhitelistSupport.Status.Successful -> {
//                            group.sendMessage(config.group.whitelistAppendSuccessfully.replace("\$name", args[0]))
//                        }
//                        WhitelistSupport.Status.DisableWhitelist -> {
//                            group.sendMessage(config.group.whitelistDisable)
//                        }
//                        else -> {
//                            //发送添加失败信息
//                            group.sendMessage(config.group.whitelistAppendFailed.replace("\$name", args[0]))
//                        }
//                    }
//                } else {
//                    when (WhitelistSupport().forceAppendWhitelist(args[0])) {
//                        WhitelistSupport.Status.Successful -> {
//                            group.sendMessage(config.group.whitelistAppendSuccessfully.replace("\$name", args[0]))
//                        }
//                        WhitelistSupport.Status.DisableWhitelist -> {
//                            group.sendMessage(config.group.whitelistDisable)
//                        }
//                        else -> {
//                            //发送添加失败信息
//                            group.sendMessage(config.group.whitelistAppendFailed.replace("\$name", args[0]))
//                        }
//                    }
//                }
//            }
        }
        listener.start()
    }

    fun stop() {
        listener.complete()
    }
}