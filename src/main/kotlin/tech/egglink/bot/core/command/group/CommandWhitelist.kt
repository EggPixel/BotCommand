package tech.egglink.bot.core.command.group

import net.mamoe.mirai.event.events.GroupMessageEvent
import tech.egglink.bot.internal.WhitelistSupport
import tech.egglink.bot.internal.commands.Command
import tech.egglink.bot.internal.commands.Commands
import tech.egglink.bot.internal.commands.Sender
import tech.egglink.bot.untils.Untils.UntilData.config

@Command("whitelist", "command.whitelist.usage", "command.whitelist.description", -1)
class CommandWhitelist: Commands() {
    override suspend fun onCommand(sender: Sender, args: List<String>, all: String): Boolean {
        if (args.size != 1 && args.size != 2) {
            return false
        }
        // args[0] 为 bindName
        // args[1] 为 override?
        val group: GroupMessageEvent = sender.getGroup() ?: return true
        config.appendDebugFile(group.senderName,"尝试使用群聊命令: $all")
        when (WhitelistSupport().appendWhitelist(args[0],group.sender.id)) {
            WhitelistSupport.Status.NoOverride -> {
                if (args.size == 2 && args[1] == "OVERRIDE") {
                    if (WhitelistSupport().appendWhitelist(
                            args[0],
                            group.sender.id,
                            override = true
                        ) == WhitelistSupport.Status.Successful
                    ) {
                        //发送成功添加信息
                        group.group.sendMessage(config.group.whitelistAppendSuccessfully.replace("\$name", args[0]))
                    } else {
                        //发送添加失败信息
                        group.group.sendMessage(config.group.whitelistAppendFailed.replace("\$name", args[0]))
                    }
                } else {
                    //发送没有重载信息
                    group.group.sendMessage(config.group.whitelistAlreadyExists.replace("\$already", WhitelistSupport().hasWhitelist(group.sender.id)))
                }
            }
            WhitelistSupport.Status.DisableWhitelist -> {
                group.group.sendMessage(config.group.whitelistDisable)
            }
            else -> {
                // 发送成功信息
                group.group.sendMessage(config.group.whitelistAppendSuccessfully.replace("\$name", args[0]))
            }
        }
        return true
    }

    override fun argSolver(args: List<String>): Boolean {
        return args.size == 2 || args.size == 1
    }
}