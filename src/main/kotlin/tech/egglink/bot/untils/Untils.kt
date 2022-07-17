package tech.egglink.bot.untils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.Plugin
import tech.egglink.bot.Config
import tech.egglink.bot.core.command.CommandRegister
import tech.egglink.bot.core.event.EventReceiveMessage
import tech.egglink.bot.qq.MiraiSupport
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class Untils {
    companion object UntilData {
        lateinit var plugin: Plugin
        var config: Config = Config()
        var mirai: MiraiSupport = MiraiSupport()
        val event: EventReceiveMessage = EventReceiveMessage()
        const val version: Double = 1.0
        val regCmd: CommandRegister = CommandRegister()
        val permissionList: List<String> = listOf("bot.true", "bot.cmd.permission", "bot.cmd.debug",
            "bot.cmd.broadcast", "bot.cmd.blacklist", "bot.cmd.whitelist-add")

        fun getUserData(id: Long): ConfigUserData {
            val parent = File(plugin.dataFolder ,"data")
            if (!parent.exists()) {
                parent.mkdir()
            }
            var result = ConfigUserData()
            val gson: Gson = GsonBuilder().setPrettyPrinting().create()
            val file = File(parent, "$id.json")
            result.qid = id
            if (!file.exists()) {
                try {
                    FileWriter(file).use { it.write(gson.toJson(result)) }
                } catch (e: Exception) {
                    Logger().error("Unable to save file...")
                }
            }
            try {
                FileReader(file).use {
                    result = gson.fromJson(it, ConfigUserData::class.java)
                }
            } catch (exception: Exception) {
                Logger().error("Unable to read file!")
            }
            return result
        }
    }
    fun getConfiguration() {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        val config = File(plugin.dataFolder, "config.json")
        if (!plugin.dataFolder.exists())
            plugin.dataFolder.mkdir()
        if (!config.exists()) {
            Logger().warn("No Config! Creating...")
            try {
                FileWriter(config).use { it.write(gson.toJson(Config())) }
            } catch (e: Exception) {
                Logger().error("Unable to save file...")
            }
        }
        try {
            FileReader(config).use {
                Untils.config = gson.fromJson(it, Config::class.java)
            }
        } catch (exception: Exception) {
            Logger().error("Unable to read file!")
        }
    }

    suspend fun broadcast(message: String, times: Int) {
        val game: String = ChatColor.translateAlternateColorCodes('&', message)
        val group: String? = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message))
        for (i in 1..times) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.botCommand.announcementTemplate.replace("\$message", game)))
            group?.let {
                for (is2 in config.sendGroup) {
                    mirai.getGroup(is2)?.sendMessage(config.group.announcementTemplate.replace("\$message", it))
                }
            }
        }
    }

    suspend fun chat(message: String, group: Group, playerName: String) {
        val text: String = config.group.chatTemplate
            .replace("\$player", playerName)
            .replace("\$message",
                ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message)).toString())
        group.sendMessage(text)
    }

    fun chat(message: String, member: Member) {
        Bukkit.broadcastMessage(
            ChatColor.translateAlternateColorCodes('&',
                config.botCommand.chatTemplate
                    .replace("\$member", member.nameCard.ifEmpty { member.nick })
                    .replace("\$qid", member.id.toString())
                    .replace("\$message", ChatColor.translateAlternateColorCodes('&', message))
            )
        )
    }
}