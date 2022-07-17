package tech.egglink.bot.untils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import tech.egglink.bot.untils.Untils.UntilData.config
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

open class Configuration {
    inner class Message {
        val noConsole: String = "&c&lYou cannot do it in console!"
        val doSuccessfully: String = "&a&lRun Task Successfully!"
        val noPermission: String = "&c&lYou do not have permission to do this!"
        val failedToDo: String = "&c&lUnable to Run Task!"
        val versionText: String = "&a&lRun in &b&l\$version"
        val updateLog: String = "&a&lServer Logs:\n1.Update to 1.19"
    }
    inner class Group {
        val onlinePlayer: String = "Online Players: \$player"
        val nothing: String = "No Player"
        val luckyValue: String = "Today's lucky value: \$lucky!"
        val notANumber: String = "Your input is not a number"
        val chatTemplate: String = "[MC] \$player: \$message"
        val joinMessage: String = "[MC] \$player joined the server."
        val leftMessage: String = "[MC] \$player left the server."
        val startServerMessage: String = "[MC] Server has been started!"
        val stopServerMessage: String = "[MC] Server has been stopped!"
        val announcementTemplate: String = "[Announcement] \$message"
        val whitelistDisable: String = "Whitelist has been disabled!"
        val whitelistAlreadyExists: String = "Your account has already bind to '\$already'. Please type the command with 'OVERRIDE' option"
        val whitelistAppendFailed: String = "Your account failed to bind to \$name"
        val whitelistAppendSuccessfully: String = "Your account has bound to \$name"
        val debugTemplate: String = "DEBUG LOGS:\n \$message"
        val noPermission: String = "You do not have permission to do this!"
    }
    inner class BotCommand {
        val usage: String = "&c&lUsage: /bot <help/login/logout/version/log/reload> <...>"
        val chatTemplate: String = "&a&l[QQ] &b&l\$member &a&l(&b&l\$qid&a&l)&a&l: &b&l\$message"
        val announcementTemplate: String = "&a&l[Announcement] &b&l\$message"
        val noWhitelist: String = "&c&lI'm sorry.\n&c&lBut you do not have the whitelist to join the server."
    }
    inner class Commands {
        val luckyDescription: String = "Get the lucky value"
        val luckyUsage: String = "Usage: !lucky"
        val whitelistDescription: String = "Bind your account to the server"
        val whitelistUsage: String = "Usage: !whitelist <name> (OVERRIDE)"
        val whitelistAddDescription: String = "Bind anyone's account to the server"
        val whitelistAddUsage: String = "Usage: !whitelist-add <name> (qq)"
        val whitelistRemoveDescription: String = "Unbind anyone's account to the server"
        val whitelistRemoveUsage: String = "Usage: !whitelist-remove <name>"
        val broadcastDescription: String = "Broadcast a message to the server"
        val broadcastUsage: String = "Usage: !broadcast <times> <message>"
        val onlineDescription: String = "Get the online players' list"
        val onlineUsage: String = "Usage: !online"
        val debugDescription: String = "Get the running logs"
        val debugUsage: String = "Usage: !debug"
        val helpDescription: String = "Get the help list"
        val helpUsage: String = "Usage: !help"
        val helpTemplate: String = "Command: \$command\n\$usage\nDescription: \$description\n\n"
        val blacklistUsage: String = "Usage: !blacklist <add/remove> <name>"
        val blacklistDescription: String = "Add someone to the blacklist"
        val permissionUsage: String = "Usage: !permission <id> <add/remove> <permission>"
        val permissionDescription: String = "Add or remove a permission to a player"
        val alreadyExistsPermission: String = "The permission already exists!"
        val notExistsPermission: String = "The permission does not exist!"
    }

    fun appendDebugFile(user: String,message: String) {
        if (config.record.size >= 50) {
            // Remove the first line
            config.record.removeAt(0)
        }
        // record格式: 列表 每项为 [时间]用户: [操作] 并使用base64编码
        // 获取当前时间
        val date = Calendar.getInstance().time
        // 将时间转换为字符串
        val dateString = SimpleDateFormat("MM-dd HH:mm:ss").format(date)
        val result = "[$dateString] $user: $message"
        config.record.add(String(Base64.getEncoder().encode(result.toByteArray())))
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        FileWriter(File(Untils.plugin.dataFolder, "config.json")).use {
            it.write(gson.toJson(config))
        }
    }

    fun appendMissingKey() {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        FileWriter(File(Untils.plugin.dataFolder, "config.json")).use {
            it.write(gson.toJson(config))
        }
    }
}