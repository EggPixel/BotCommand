package tech.egglink.bot.internal

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.Bot
import net.mamoe.mirai.network.CustomLoginFailedException
import net.mamoe.mirai.utils.LoginSolver
import tech.egglink.bot.untils.Untils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class BotLoginSolver : LoginSolver() {
    private var threads: Thread? = null
    private val loginCancelException: CustomLoginFailedException =
        object : CustomLoginFailedException(true, "用户终止登录") {}
    private val loginErrorException: CustomLoginFailedException =
        object : CustomLoginFailedException(true, "登录时出现严重错误") {}

    /**
     * @param bot 机器人实例
     * @param data 图片内容
     * @return 验证码结果
     */
    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? {
        deviceVerifyCanceled[bot] = false

        // 建立机器人账号文件夹
        val imageDir = File(Untils.plugin.dataFolder, "verifyimage")
        if (!imageDir.exists() && !imageDir.mkdirs()) {
            throw RuntimeException("Failed to create folder " + imageDir.path)
        }

        // 验证码保存到本地
        val imageFile = File(imageDir, bot.id.toString() + "-verify.png")
        try {
            withContext(Dispatchers.IO) {
                FileOutputStream(imageFile).use { fos ->
                    fos.write(data)
                    fos.flush()
                }
            }
        } catch (e: IOException) {
            bot.logger.warning("保存验证码图片文件时出现异常，原因: $e")
        }
        threads = Thread {
            deviceVerifyContinue[bot] = false
            bot.logger.warning("当前登录的QQ（" + bot.id + "）需要文字验证码验证")
            bot.logger.warning("请找到下面的文件并识别文字验证码")
            bot.logger.warning(imageFile.path)
            bot.logger.warning("识别完成后，请输入指令 /bot pic <Code>")
            bot.logger.warning("如需取消登录，请输入指令 /bot pic cancel")
            while (true) {
                try {
                    if (!deviceVerifyContinue.containsKey(bot) || deviceVerifyContinue[bot]!!) {
                        break
                    }
                } catch (e: NullPointerException) {
                    break
                }
            }
        }
        threads!!.start()
        try {
            withContext(Dispatchers.IO) {
                threads!!.join()
            }
        } catch (e: InterruptedException) {
            bot.logger.warning("启动验证线程时出现异常，原因: $e")
            throw loginErrorException
        }
        return if (!deviceVerifyCanceled.containsKey(bot) || deviceVerifyCanceled[bot]!!) {
            deviceVerifyCanceled.remove(bot)
            deviceVerifyContinue.remove(bot)
            deviceVerifyCode.remove(bot)
            throw loginCancelException
        } else {
            val result = deviceVerifyCode[bot]
            deviceVerifyCanceled.remove(bot)
            deviceVerifyContinue.remove(bot)
            deviceVerifyCode.remove(bot)
            result
        }
    }

    /**
     * @param bot 机器人实例
     * @param url 滑动验证码验证链接
     * @return 验证码解决成功后获得的 ticket
     */
    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? {
        deviceVerifyCanceled[bot] = false
        threads = Thread {
            deviceVerifyContinue[bot] = false
            bot.logger.warning("当前登录的QQ（" + bot.id + "）需要滑动验证码验证")
            bot.logger.warning("请使用手机QQ打开以下链接进行验证")
            bot.logger.warning(url)
            bot.logger.warning("验证完成后，请输入指令 /bot slider <ticket>")
            bot.logger.warning("如需取消登录，请输入指令 /bot slider cancel")
            while (true) {
                try {
                    if (!deviceVerifyContinue.containsKey(bot) || deviceVerifyContinue[bot]!!
                    ) {
                        break
                    }
                } catch (e: NullPointerException) {
                    break
                }
            }
        }
        threads!!.start()
        try {
            withContext(Dispatchers.IO) {
                threads!!.join()
            }
        } catch (e: InterruptedException) {
            bot.logger.warning("启动验证线程时出现异常，原因: $e")
            throw loginErrorException
        }
        return if (!deviceVerifyCanceled.containsKey(bot) || deviceVerifyCanceled[bot]!!) {
            deviceVerifyCanceled.remove(bot)
            deviceVerifyContinue.remove(bot)
            deviceVerifyCode.remove(bot)
            throw loginCancelException
        } else {
            val result = deviceVerifyCode[bot]
            deviceVerifyCanceled.remove(bot)
            deviceVerifyContinue.remove(bot)
            deviceVerifyCode.remove(bot)
            result
        }
    }

    /**
     * @param bot 机器人实例
     * @param url 设备锁验证链接
     * @return 任意值
     */
    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? {
        deviceVerifyCanceled[bot] = false
        threads = Thread {
            deviceVerifyContinue[bot] = false
            bot.logger.warning("当前登录的QQ（" + bot.id + "）需要设备锁验证")
            bot.logger.warning("请使用手机QQ打开以下链接进行验证")
            bot.logger.warning(url)
            bot.logger.warning("验证完成后，请输入指令 /bot unsafe verify")
            bot.logger.warning("如需取消登录，请输入指令 /bot unsafe cancel")
            while (true) {
                try {
                    if (!deviceVerifyContinue.containsKey(bot) || deviceVerifyContinue[bot]!!
                    ) {
                        break
                    }
                } catch (e: NullPointerException) {
                    break
                }
            }
        }
        threads!!.start()
        try {
            withContext(Dispatchers.IO) {
                threads!!.join()
            }
        } catch (e: InterruptedException) {
            bot.logger.warning("启动验证线程时出现异常，原因: $e")
            throw loginErrorException
        }
        return if (!deviceVerifyCanceled.containsKey(bot) || deviceVerifyCanceled[bot]!!) {
            deviceVerifyCanceled.remove(bot)
            deviceVerifyContinue.remove(bot)
            deviceVerifyCode.remove(bot)
            throw loginCancelException
        } else {
            deviceVerifyCanceled.remove(bot)
            deviceVerifyContinue.remove(bot)
            deviceVerifyCode.remove(bot)
            null
        }
    }

    companion object {
        /**
         * 是否继续线程（用于判断用户侧是否完成验证）
         */
        private val deviceVerifyContinue = HashMap<Bot, Boolean>()

        /**
         * 是否取消线程（用于判断用户是否决定终止验证）
         */
        private val deviceVerifyCanceled = HashMap<Bot, Boolean>()

        /**
         * 验证码（用户提供的验证码或ticket）
         */
        private val deviceVerifyCode = HashMap<Bot, String>()
        fun solveUnsafeDeviceLoginVerify(BotAccount: Long, Canceled: Boolean) {
            deviceVerifyContinue[Bot.getInstance(BotAccount)] = true
            deviceVerifyCanceled[Bot.getInstance(BotAccount)] = Canceled
        }

        fun solveSliderCaptcha(BotAccount: Long, Canceled: Boolean) {
            deviceVerifyContinue[Bot.getInstance(BotAccount)] = true
            deviceVerifyCanceled[Bot.getInstance(BotAccount)] = Canceled
        }

        fun solveSliderCaptcha(BotAccount: Long, ticket: String) {
            deviceVerifyContinue[Bot.getInstance(BotAccount)] = true
            deviceVerifyCanceled[Bot.getInstance(BotAccount)] = false
            deviceVerifyCode[Bot.getInstance(BotAccount)] = ticket
        }

        fun solvePicCaptcha(BotAccount: Long, Canceled: Boolean) {
            deviceVerifyContinue[Bot.getInstance(BotAccount)] = true
            deviceVerifyCanceled[Bot.getInstance(BotAccount)] = Canceled
        }

        fun solvePicCaptcha(BotAccount: Long, Captcha: String) {
            deviceVerifyContinue[Bot.getInstance(BotAccount)] = true
            deviceVerifyCanceled[Bot.getInstance(BotAccount)] = false
            deviceVerifyCode[Bot.getInstance(BotAccount)] = Captcha
        }

        fun closeAllVerifyThreads() {
            deviceVerifyContinue.clear()
            deviceVerifyCanceled.clear()
            deviceVerifyCode.clear()
        }
    }
}