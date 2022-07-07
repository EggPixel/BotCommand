package tech.egglink.bot.core.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import tech.egglink.bot.core.command.group.*
import tech.egglink.bot.internal.commands.Commands
import tech.egglink.bot.internal.commands.Sender
import tech.egglink.bot.untils.Untils

class ProcessCommands {
    fun registry() {
        Untils.regCmd.register(CommandLuckyNumber())
        Untils.regCmd.register(CommandOnline())
        Untils.regCmd.register(CommandWhitelist())
        Untils.regCmd.register(CommandWhitelistAdd())
        Untils.regCmd.register(CommandBroadcast())
        Untils.regCmd.register(CommandDebugMode())
        Untils.regCmd.register(CommandHelp())
    }

    suspend fun run(input: String, sender: GroupMessageEvent): Boolean {
        val inp = input.split(" ")
        val command = inp[0]
        val args = inp.drop(1).toTypedArray()
        val cmd: Commands = Untils.regCmd.getCommand(command) ?: return true
        if (!cmd.argSolver(args.asList())) {
            return false
        }
        return cmd.onCommand(Sender(sender), args.asList(), input)
    }
}