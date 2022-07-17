package tech.egglink.bot.core.command

import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.StrangerMessageEvent
import tech.egglink.bot.core.command.group.*
import tech.egglink.bot.internal.commands.Commands
import tech.egglink.bot.internal.commands.Sender
import tech.egglink.bot.internal.commands.TargetType
import tech.egglink.bot.untils.Untils

class ProcessCommands {
    fun registry() {
        Untils.regCmd.register(CommandLuckyNumber())
        Untils.regCmd.register(CommandOnline())
        Untils.regCmd.register(CommandWhitelist())
        Untils.regCmd.register(CommandWhitelistAdd())
        Untils.regCmd.register(CommandBroadcast())
        Untils.regCmd.register(CommandDebug())
        Untils.regCmd.register(CommandHelp())
        Untils.regCmd.register(CommandBlacklist())
        Untils.regCmd.register(CommandPermission())
    }

    suspend fun run(input: String, sender: GroupMessageEvent): Boolean {
        val inp = input.split(" ")
        val command = inp[0]
        val args = inp.drop(1).toTypedArray()
        val cmd: Commands = Untils.regCmd.getCommand(command) ?: return true
        if (!(Untils.regCmd.getTargetType(cmd) == TargetType.GROUP ||
                    Untils.regCmd.getTargetType(cmd) == TargetType.BothGroupAndStranger ||
                    Untils.regCmd.getTargetType(cmd) == TargetType.BothFriendAndGroup ||
                    Untils.regCmd.getTargetType(cmd) == TargetType.All)) {
            return true
        }
        // 是否有权限
        if (Untils.config.administrator.contains(sender.sender.id) ||
            Untils.getUserData(sender.sender.id).permissionTrue.contains(Untils.regCmd.getPermission(cmd))) {
            sender.group.sendMessage(Untils.config.group.noPermission)
            return true
        }
        if (!cmd.argSolver(args.asList())) {
            return false
        }
        return cmd.onCommand(Sender(sender), args.asList(), input)
    }

    suspend fun run(input: String, sender: FriendMessageEvent): Boolean {
        val inp = input.split(" ")
        val command = inp[0]
        val args = inp.drop(1).toTypedArray()
        val cmd: Commands = Untils.regCmd.getCommand(command) ?: return true
        if (!(Untils.regCmd.getTargetType(cmd) == TargetType.FRIEND ||
                    Untils.regCmd.getTargetType(cmd) == TargetType.BothFriendAndStranger ||
                    Untils.regCmd.getTargetType(cmd) == TargetType.BothFriendAndGroup ||
                    Untils.regCmd.getTargetType(cmd) == TargetType.All)) {
            return true
        }
        // 是否有权限
        if (Untils.config.administrator.contains(sender.sender.id) ||
            Untils.getUserData(sender.sender.id).permissionTrue.contains(Untils.regCmd.getPermission(cmd))) {
            sender.friend.sendMessage(Untils.config.group.noPermission)
            return true
        }
        if (!cmd.argSolver(args.asList())) {
            return false
        }
        return cmd.onCommand(Sender(sender), args.asList(), input)
    }

    suspend fun run(input: String, sender: StrangerMessageEvent): Boolean {
        val inp = input.split(" ")
        val command = inp[0]
        val args = inp.drop(1).toTypedArray()
        val cmd: Commands = Untils.regCmd.getCommand(command) ?: return true
        if (!(Untils.regCmd.getTargetType(cmd) == TargetType.STRANGER ||
                    Untils.regCmd.getTargetType(cmd) == TargetType.BothFriendAndStranger ||
                    Untils.regCmd.getTargetType(cmd) == TargetType.BothGroupAndStranger ||
                    Untils.regCmd.getTargetType(cmd) == TargetType.All)) {
            return true
        }
        // 是否有权限
        if (Untils.config.administrator.contains(sender.sender.id) ||
            Untils.getUserData(sender.sender.id).permissionTrue.contains(Untils.regCmd.getPermission(cmd))) {
            sender.stranger.sendMessage(Untils.config.group.noPermission)
            return true
        }
        if (!cmd.argSolver(args.asList())) {
            return false
        }
        return cmd.onCommand(Sender(sender), args.asList(), input)
    }
}