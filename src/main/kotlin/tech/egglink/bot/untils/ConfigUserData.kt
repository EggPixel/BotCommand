package tech.egglink.bot.untils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileWriter

class ConfigUserData {
    var qid: Long = 0L  // 作为文件名
    val bindName: String = "Somebody" // 绑定的MC用户名
    val permissionTrue: ArrayList<String> = arrayListOf("bot.true") // 权限列表
    val notepad: ArrayList<String> = arrayListOf()  // 用户的备忘录 使用Base64加密
    val todo: ArrayList<String> = arrayListOf() // 用户的待办事项 使用Base64加密

    fun save() {
        val parent = File(Untils.plugin.dataFolder ,"data")
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        val file = File(parent, "$qid.json")
        if (!file.exists()) {
            try {
                FileWriter(file).use { it.write(gson.toJson(this)) }
            } catch (e: Exception) {
                Logger().error("Unable to save file...")
            }
        }
    }
}