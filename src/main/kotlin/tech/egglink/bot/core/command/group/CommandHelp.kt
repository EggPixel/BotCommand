package tech.egglink.bot.core.command.group

import tech.egglink.bot.internal.commands.Command
import tech.egglink.bot.internal.commands.Commands
import tech.egglink.bot.internal.commands.Sender
import tech.egglink.bot.untils.Untils

@Command("help", "command.help.usage", "command.help.description", 0)
class CommandHelp: Commands() {
    override suspend fun onCommand(sender: Sender, args: List<String>, all: String): Boolean {
        // 生成 StringBuilder 来拼接消息
        val stringBuilder = StringBuilder()
        // 遍历 Untils.regCmd.getCommands() 获取所有的命令
        Untils.regCmd.getCommands().forEach {
            // 获取命令的名称 用法和描述
            val name = it.toString()
            val usage = Untils.regCmd.getUsage(it)
            val description = Untils.regCmd.getDescription(it)
            stringBuilder.append(Untils.config.commands.helpTemplate.replace("\$command", name).replace("\$usage", usage).replace("\$description", description))
        }
        sender.getGroup()?.group?.sendMessage(stringBuilder.toString()) ?: throw IllegalStateException("Group is null")
        return true
    }
}