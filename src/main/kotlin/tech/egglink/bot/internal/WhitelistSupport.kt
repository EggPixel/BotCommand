package tech.egglink.bot.internal

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import tech.egglink.bot.untils.Logger
import tech.egglink.bot.untils.Untils
import java.io.File
import java.io.FileWriter

class WhitelistSupport {
    /**
     * 追加白名单
     * @param playerName 玩家名
     * @param qqID QQ号
     * */
    fun appendWhitelist(playerName: String, qqID: Long, force: Boolean = false, override: Boolean = false): Status {
        val config = Untils.config
        if (!config.enableWhiteList) {  // 白名单没开
            return Status.DisableWhitelist
        }
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        val append = "$qqID|$playerName"  // 获取要设置的内容
        if (force) {   // 强制添加
            config.whiteList.add(append)  // 直接添加并写入
            FileWriter(File(Untils.plugin.dataFolder, "config.json")).use {
                it.write(gson.toJson(config))
            }
        } else {  // 不强制添加
            val qid: ArrayList<Long> = ArrayList()
            val list: ArrayList<String> = ArrayList()
            for (i in config.whiteList) {  // 获取是否存在
                try {
                    qid.add(java.lang.Long.parseLong(i.split("|")[0]))
                } catch (e: Exception) {
                    Logger().error("Config has been broken!")
                    qid.add(0L)
                    e.printStackTrace()
                }
                list.add(i)
            }
            if (qqID in qid && !override) {  // 存在且未指定override
                return Status.NoOverride
            }
            if (qqID in qid) {  // 存在并指定true
                list[qid.indexOf(qqID)] = append
            } else {  // 不存在
                list.add(append)
            }
            config.whiteList = list
            // 写入
            FileWriter(File(Untils.plugin.dataFolder, "config.json")).use {
                it.write(gson.toJson(config))
            }
        }
        return Status.Successful
    }

    /**
     * 玩家是否有白名单
     * @param playerName 玩家的名字
     * */
    fun hasWhitelist(playerName: String):Boolean {
        val config = Untils.config
        val player: ArrayList<String> = ArrayList()
        for (i in config.whiteList) {  // 获取是否存在
            try {
                player.add(i.split("|")[1])
            } catch (e: Exception) {
                Logger().error("Config has been broken!")
                e.printStackTrace()
            }
        }
        return playerName in player
    }

    fun hasWhitelist(qqID: Long):String {
        val config = Untils.config
        val player: ArrayList<String> = ArrayList()
        val qqIDs: ArrayList<Long> = ArrayList()
        for (i in config.whiteList) {  // 获取是否存在
            try {
                qqIDs.add(java.lang.Long.parseLong(i.split("|")[0]))
                player.add(i.split("|")[1])
            } catch (e: Exception) {
                Logger().error("Config has been broken!")
                e.printStackTrace()
            }
        }
        return player[qqIDs.indexOf(qqID)]
    }

    /**
     * 无视一号一人规则强制添加
     * @param playerName 玩家名
     * QQ id将为0
     * */
    fun forceAppendWhitelist(playerName: String): Status {
        return appendWhitelist(playerName, 0, true)
    }

    /**
     * 无视一号一人规则强制添加
     * @param playerName 玩家名
     * @param qqID QQ号
     * */
    fun forceAppendWhitelist(playerName: String, qqID: Long): Status {
        return appendWhitelist(playerName, qqID, true)
    }

    enum class Status {
        Successful,
        DisableWhitelist,
        NoOverride
    }
}