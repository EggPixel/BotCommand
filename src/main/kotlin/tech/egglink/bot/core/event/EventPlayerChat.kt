package tech.egglink.bot.core.event

import kotlinx.coroutines.launch
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import tech.egglink.bot.Config
import tech.egglink.bot.untils.Untils

class EventPlayerChat: Listener {
    @EventHandler
    fun AsyncPlayerChatEvent.onMessage() {
        val config: Config = Untils.config
        if (message.startsWith(config.chatPrefix)) {
            if (!player.hasPermission("botcommand.bot.chat")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.message.noPermission))
                return
            }
            Untils.mirai.getBot().launch {
                for (group in config.sendGroup) {
                    Untils.mirai.getGroup(group)?.let {
                        Untils().chat(message.substring(config.chatPrefix.length), it, player.name)
                    }
                }
                message = message.substring(config.chatPrefix.length)
            }
        }
    }
}