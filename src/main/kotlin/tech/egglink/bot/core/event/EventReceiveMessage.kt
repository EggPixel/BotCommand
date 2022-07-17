package tech.egglink.bot.core.event

import kotlinx.coroutines.CompletableJob
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.StrangerMessageEvent
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
    private lateinit var listener2: CompletableJob
    private lateinit var listener3: CompletableJob

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
        }
        listener2 = Untils.mirai.getBot().eventChannel.subscribeAlways<FriendMessageEvent> { event ->
            // message.content.toPlainText().content // 获取信息
            val config: Config = Untils.config
            val message = message.content.toPlainText().content
            if (!message.startsWith("!")) {
                return@subscribeAlways
            }
            if (sender.id in config.blackList) {
                return@subscribeAlways
            }
            if (!ProcessCommands().run(message.substring(1), event)) {
                friend.sendMessage(Untils.regCmd.getCommand(message.substring(1).split(" ")[0])
                    ?.let { Untils.regCmd.getUsage(it) } ?: "none")
                return@subscribeAlways
            } else {
                // 命令执行成功
                return@subscribeAlways
            }
        }
        listener3 = Untils.mirai.getBot().eventChannel.subscribeAlways<StrangerMessageEvent> { event ->
            // message.content.toPlainText().content // 获取信息
            val config: Config = Untils.config
            val message = message.content.toPlainText().content
            if (!message.startsWith("!")) {
                return@subscribeAlways
            }
            if (sender.id in config.blackList) {
                return@subscribeAlways
            }
            if (!ProcessCommands().run(message.substring(1), event)) {
                stranger.sendMessage(Untils.regCmd.getCommand(message.substring(1).split(" ")[0])
                    ?.let { Untils.regCmd.getUsage(it) } ?: "none")
                return@subscribeAlways
            } else {
                // 命令执行成功
                return@subscribeAlways
            }
        }
        listener.start()
        listener2.start()
        listener3.start()
    }

    fun stop() {
        listener.complete()
        listener2.complete()
        listener3.complete()
    }
}