package tech.egglink.bot.internal.commands

import tech.egglink.bot.untils.Untils

@Command("cmd", "usage", "description")
abstract class Commands {
    /**
     * true时代表命令成功执行
     * @return 返回值为false时代表命令执行失败 (通常是因为参数不正确)
     */
    abstract suspend fun onCommand(sender: Sender, args: List<String>, all: String): Boolean
    open fun argSolver(args: List<String>): Boolean {
        return Untils.regCmd.getParamCount(this) == args.size || Untils.regCmd.getParamCount(this) == -1
    }
    override fun toString(): String {
        return Untils.regCmd.getCommandName(this)
    }
}