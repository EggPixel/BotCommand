package tech.egglink.bot.core.command.group

import net.mamoe.mirai.event.events.GroupMessageEvent
import org.bukkit.ChatColor
import tech.egglink.bot.Config
import tech.egglink.bot.internal.commands.Command
import tech.egglink.bot.internal.commands.Commands
import tech.egglink.bot.internal.commands.Sender
import tech.egglink.bot.untils.Untils
import java.util.*

@Command("debug", "command.debugMode.usage", "command.debugMode.description", 0)
class CommandDebugMode: Commands() {
    override suspend fun onCommand(sender: Sender, args: List<String>, all: String): Boolean {
        val event: GroupMessageEvent = sender.getGroup() ?: throw IllegalStateException("Sender is not in group")
        val config: Config = Untils.config
        config.appendDebugFile(event.senderName,"尝试使用群聊命令: $all")
        if (event.sender.id !in config.administrator) {
            ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&',config.message.noPermission))
                ?.let { it1 -> event.group.sendMessage(it1) }
            return true
        }
        // 获取日志信息
        // 遍历 config 中的 record
        // record格式: 列表 每项为 [时间]用户: [操作] 并使用base64编码
        val stringBuilder: StringBuilder = StringBuilder()
        for (record in config.record) {
            // 解码
            val decode = Base64.getDecoder().decode(record)
            // 拼接消息
            stringBuilder.append(String(decode)).append("\n")
        }
        if (stringBuilder.isEmpty()) {
            stringBuilder.append("No Logs")
        }
        event.group.sendMessage(config.group.debugTemplate.replace("\$message", stringBuilder.toString()))
        return true
    }
}