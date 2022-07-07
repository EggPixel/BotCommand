package tech.egglink.bot.core.command.group

import net.mamoe.mirai.contact.Group
import tech.egglink.bot.Config
import tech.egglink.bot.internal.commands.Command
import tech.egglink.bot.internal.commands.Commands
import tech.egglink.bot.internal.commands.Sender
import tech.egglink.bot.untils.Untils
import java.util.*
import kotlin.random.Random

@Command("lucky", "command.lucky.usage", "command.lucky.description", 0)
class CommandLuckyNumber: Commands() {
    override suspend fun onCommand(sender: Sender, args: List<String>, all: String): Boolean {
        val config: Config = Untils.config
        if (!sender.isGroup()) {
            return true
        }
        val group: Group? = sender.getGroup()?.group
        val id: Long = sender.getGroup()?.sender?.id ?: 0
        val luckyValue: Int = (0..100).random(
            Random(id +
                Calendar.getInstance().get(Calendar.DAY_OF_YEAR) * Calendar.getInstance().get(Calendar.YEAR))
        )
        group?.sendMessage(config.group.luckyValue.replace("\$lucky", luckyValue.toString()))
        return true
    }
}