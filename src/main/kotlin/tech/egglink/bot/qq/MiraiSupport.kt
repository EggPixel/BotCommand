package tech.egglink.bot.qq

import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Stranger
import net.mamoe.mirai.utils.BotConfiguration
import tech.egglink.bot.internal.BotLoginSolver
import tech.egglink.bot.untils.Untils

class MiraiSupport {
    private lateinit var bot: Bot
    private var isLogin = false
    fun login(userID: Long, userPassword: String): Boolean {
        bot = BotFactory.newBot(userID, userPassword, configuration = BotConfiguration {
            loginSolver = BotLoginSolver()
            protocol = BotConfiguration.MiraiProtocol.MACOS
        })
        return if (bot.isOnline) {
            isLogin = true
            false
        } else {
            isLogin = true
            bot.launch {
                bot.login()
                Untils.config.appendDebugFile("System","机器人已启动")
                // 是否提示启动
                if (Untils.config.serverStartStopMessage) {
                    for (group in Untils.config.sendGroup)
                        Untils.mirai.getGroup(group)?.sendMessage(Untils.config.group.startServerMessage)
                }
//                // Bot的守护进程
//                Untils.mirai.getBot().launch {
//                    while (true) {
//                        if (isLogin) {
//                            if (!bot.isOnline) {
//                                bot.login()
//                                Untils.config.appendDebugFile("System","机器人已启动")
//                            }
//                        }
//                    }
//                }
            }
            // 注册事件
            Untils.event.start()
            true
        }
    }

    /**
     * 获取一个 Bot 实例
     * */
    fun getBot(): Bot {
        return bot
    }

    fun logout(): Boolean {
        isLogin = false
        return if (!bot.isOnline) {
            false
        } else {
            Untils.config.appendDebugFile("System","机器人已注销")
            Untils.event.stop()
            bot.close()
            true
        }
    }
    /**
     * 得到了一个 Group? 类型
     * 需要?.判断
     * */

    fun getGroup(groupID: Long): Group? {
        return bot.getGroup(groupID)
    }
    /**
     * 得到了一个 Friend? 类型
     * 需要?.判断
     * */

    fun getFriend(friendID: Long): Friend? {
        return bot.getFriend(friendID)
    }
    /**
     * 得到了一个 Stranger? 类型
     * 陌生人
     * 需要?.判断
     * */

    fun getStranger(strangerID: Long): Stranger? {
        return bot.getStranger(strangerID)
    }

}