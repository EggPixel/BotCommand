package tech.egglink.bot.core.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import tech.egglink.bot.untils.Untils

class EventPlayerPreCommand: Listener {
    @EventHandler
    fun PlayerCommandPreprocessEvent.onCommand() {
        Untils.config.appendDebugFile(player.name,"尝试使用服务器命令: $message")
    }
}