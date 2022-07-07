package tech.egglink.bot.untils

class Logger {
    private val logger = Untils.plugin.logger
    /**
     * 输出一条错误信息
     * @param message 发送的消息
     * */

    fun error(message: String) {
        logger.severe(message)
    }
    /**
     * 输出一条普通信息
     * @param message 发送的消息
     * */

    fun info(message: String) {
        logger.info(message)
    }
    /**
     * 输出一条警告信息
     * @param message 发送的消息
     * */

    fun warn(message: String) {
        logger.warning(message)
    }
}