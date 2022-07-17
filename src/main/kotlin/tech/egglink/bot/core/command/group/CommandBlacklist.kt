package tech.egglink.bot.core.command.group

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.ChatColor
import tech.egglink.bot.internal.commands.Command
import tech.egglink.bot.internal.commands.Commands
import tech.egglink.bot.internal.commands.Sender
import tech.egglink.bot.untils.Untils
import java.io.File
import java.io.FileWriter

@Command("blacklist", "command.blacklist.usage", "command.blacklist.description", 2, permission = "bot.cmd.blacklist")
class CommandBlacklist: Commands() {
    override suspend fun onCommand(sender: Sender, args: List<String>, all: String): Boolean {
        // args[0] = add | remove
        // args[1] = user id
        // config
        val config = Untils.config
        val event = sender.getGroup() ?: throw IllegalStateException("Group not found")
        config.appendDebugFile(event.senderName,"尝试使用群聊命令: $all")
        when (args[0]) {
            "add" -> {
                try {
                    config.blackList.add(java.lang.Long.parseLong(args[1]))
                    val gson: Gson = GsonBuilder().setPrettyPrinting().create()
                    withContext(Dispatchers.IO) {
                        FileWriter(File(Untils.plugin.dataFolder, "config.json")).use {
                            it.write(gson.toJson(Untils.config))
                        }
                    }
                    ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', config.message.doSuccessfully))
                        ?.let { event.group.sendMessage(it) }
                } catch (e: java.lang.NumberFormatException) {
                    return false
                } catch (e: NumberFormatException) {
                    return false
                }
            }
            "remove" -> {
                try {
                    config.blackList.remove(java.lang.Long.parseLong(args[1]))
                    val gson: Gson = GsonBuilder().setPrettyPrinting().create()
                    withContext(Dispatchers.IO) {
                        FileWriter(File(Untils.plugin.dataFolder, "config.json")).use {
                            it.write(gson.toJson(Untils.config))
                        }
                    }
                    ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', config.message.doSuccessfully))
                        ?.let { event.group.sendMessage(it) }
                } catch (e: java.lang.NumberFormatException) {
                    return false
                } catch (e: NumberFormatException) {
                    return false
                } catch (e: NoSuchElementException) {
                    return false
                }
            }
            else -> {
                return false
            }
        }
        return true
    }
}