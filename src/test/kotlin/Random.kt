import com.google.gson.Gson
import com.google.gson.GsonBuilder
import tech.egglink.bot.untils.Logger
import tech.egglink.bot.untils.Untils
import java.io.File
import java.io.FileReader
import java.io.FileWriter

lateinit var x: A
fun main() {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    val config = File("config.json")
    if (!config.exists()) {
        try {
            FileWriter(config).use { it.write(gson.toJson(A())) }
        } catch (e: Exception) {
        }
    }
    try {
        FileReader(config).use {
            x = gson.fromJson(it, A::class.java)
        }
    } catch (exception: Exception) {
    }
    println(x.x)
    try {
        FileWriter(config).use { it.write(gson.toJson(x)) }
    } catch (e: Exception) {
    }
}

class A {
    val x = "a"
    val y = "b"
}
