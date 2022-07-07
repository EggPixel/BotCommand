package tech.egglink.bot.internal.commands

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Command(val name: String, val usage: String, val description: String, val argsCount: Int = -1,
                         val target: TargetType = TargetType.GROUP, val permission: String = "bot.true")
