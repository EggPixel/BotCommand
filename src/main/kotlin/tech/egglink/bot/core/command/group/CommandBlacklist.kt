package tech.egglink.bot.core.command.group

import tech.egglink.bot.internal.commands.Command
import tech.egglink.bot.internal.commands.Commands
import tech.egglink.bot.internal.commands.Sender
import tech.egglink.bot.untils.Untils

@Command("blacklist", "command.blacklist.usage", "command.blacklist.description", 2)
class CommandBlacklist: Commands() {
    override suspend fun onCommand(sender: Sender, args: List<String>, all: String): Boolean {
        // args[0] = add | remove
        // args[1] = user id
        // config
        val config = Untils.config
        when (args[0]) {
            "add" -> {
                try {
                    config.blackList.add(java.lang.Long.parseLong(args[1]))
                } catch (e: java.lang.NumberFormatException) {
                    return false
                } catch (e: NumberFormatException) {
                    return false
                }
            }
            "remove" -> {
                try {
                    config.blackList.remove(java.lang.Long.parseLong(args[1]))
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