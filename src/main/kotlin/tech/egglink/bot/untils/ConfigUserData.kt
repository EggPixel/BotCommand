package tech.egglink.bot.untils

class ConfigUserData {
    val qid: Long = 0L  // 作为文件名
    val bindName: String = "Somebody" // 绑定的MC用户名
    val permissionTrue: ArrayList<String> = arrayListOf("bot.true") // 权限列表
    val permissionFalse: ArrayList<String> = arrayListOf() // 权限列表
    val notepad: ArrayList<String> = arrayListOf()  // 用户的备忘录 使用Base64加密
    val todo: ArrayList<String> = arrayListOf() // 用户的待办事项 使用Base64加密
}