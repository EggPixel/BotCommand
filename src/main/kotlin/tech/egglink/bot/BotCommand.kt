package tech.egglink.bot

import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import tech.egglink.bot.core.command.CommandBot
import tech.egglink.bot.core.command.ProcessCommands
import tech.egglink.bot.core.event.EventJoinLeave
import tech.egglink.bot.core.event.EventPlayerChat
import tech.egglink.bot.untils.Configuration
import tech.egglink.bot.untils.Logger
import tech.egglink.bot.untils.Untils
import java.io.File

class BotCommand: JavaPlugin() {
    override fun onEnable() {
        Logger().info("Plugin has been loaded!")
        // Json初始化
        Untils().getConfiguration()
        // 添加失去的Key
        Configuration().appendMissingKey()
        // 注册命令
        this.getCommand("bot")?.setExecutor(CommandBot())
        // 写入配置文件
        Untils.config.appendDebugFile("Console","插件已启用!")
        // 注册bot事件在MiraiSupport中
        // 注册mc事件
        Bukkit.getPluginManager().registerEvents(EventPlayerChat(), this)
        Bukkit.getPluginManager().registerEvents(EventJoinLeave(), this)
        // 注册命令
        ProcessCommands().registry()
        // 判断是否 autoLogin
        if (Untils.config.autoLogin) {
            Untils.mirai.login(Untils.config.userID, Untils.config.userPassword)
        }
    }
    override fun onLoad() {
        Untils.plugin = this
    }

    override fun onDisable() {
        Logger().info("Plugin has been unloaded!")
        Untils.config.appendDebugFile("Console","插件已关闭!")
        // 是否提示关闭
        if (Untils.config.serverStartStopMessage && Untils.mirai.getBot().isOnline) {
            Untils.mirai.getBot().launch {
                for (group in Untils.config.sendGroup)
                    Untils.mirai.getGroup(group)?.sendMessage(Untils.config.group.stopServerMessage)
            }
        }
    }
}