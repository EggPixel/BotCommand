package tech.egglink.bot

import tech.egglink.bot.untils.Configuration

class Config: Configuration() {
    val userID: Long = 10000L
    val userPassword: String = "Password"
    val chatPrefix: String = "#"
    val sendGroup: ArrayList<Long> = arrayListOf(1000L)
    val administrator: ArrayList<Long> = arrayListOf(1000L)
    val autoLogin: Boolean = true
    val joinLeaveMessage: Boolean = true
    val serverStartStopMessage: Boolean = true
    val enableWhiteList: Boolean = false
    var whiteList: ArrayList<String> = arrayListOf("1000|Somebody")
    var record: ArrayList<String> = arrayListOf()
    val message = Message()
    val group = Group()
    val botCommand = BotCommand()
    val commands = Commands()
}