package tech.egglink.bot.core.event

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import tech.egglink.bot.Config
import tech.egglink.bot.internal.WhitelistSupport
import tech.egglink.bot.untils.Untils

class EventJoinLeave: Listener {
    @OptIn(DelicateCoroutinesApi::class)
    @EventHandler
    fun PlayerLoginEvent.onLogin() {
        val config: Config = Untils.config
        // 判断是否有白名单
        if (config.enableWhiteList && !WhitelistSupport().hasWhitelist(player.name)) {
            player.kickPlayer(ChatColor.translateAlternateColorCodes('&', config.botCommand.noWhitelist))
            return
        }
        GlobalScope.launch {
            if (config.joinLeaveMessage) {
                val name: String = player.name
                for (group in config.sendGroup)
                    Untils.mirai.getGroup(group)?.sendMessage(config.group.joinMessage.replace("\$player", name))
            }
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    @EventHandler
    fun PlayerQuitEvent.onLeave() {
        val config: Config = Untils.config
        // 判断是否有白名单
        if (config.enableWhiteList && !WhitelistSupport().hasWhitelist(player.name)) {
            return
        }
        GlobalScope.launch {
            if (config.joinLeaveMessage) {
                val name: String = player.name
                for (group in config.sendGroup)
                    Untils.mirai.getGroup(group)?.sendMessage(config.group.leftMessage.replace("\$player", name))
            }
        }
    }
}