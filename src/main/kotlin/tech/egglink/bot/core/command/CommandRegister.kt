package tech.egglink.bot.core.command

import tech.egglink.bot.internal.commands.Command
import tech.egglink.bot.internal.commands.Commands
import tech.egglink.bot.untils.Untils

class CommandRegister {
    private val commands = mutableListOf<Commands>()
    // 生成一个 Map，存储用法
    private val usageMap = mutableMapOf<Commands, String>()
    // 生成一个 Map，存储描述
    private val descriptionMap = mutableMapOf<Commands, String>()
    // 生成一个 Map，存储指令
    private val commandMap = mutableMapOf<String, Commands>()
    // 生命一个 Map，存储指令名称
    private val commandNameMap = mutableMapOf<Commands, String>()
    // 生成一个 Map，存储参数个数
    private val paramCountMap = mutableMapOf<Commands, Int>()
    fun register(command: Commands) {
        commands.add(command)
        // 获取command的注解
        val annotation = command.javaClass.getAnnotation(Command::class.java)
        // 获取注解的值
        val value = annotation.usage
        // 将注解的值作为value，command作为key存入map
        usageMap[command] = value
        // 获取注解的description
        val description = annotation.description
        // 将注解的description作为value，command作为key存入map
        descriptionMap[command] = description
        // 获取注解的commandName
        val commandName = annotation.name
        // 将注解的commandName作为key，command作为value存入map
        commandMap[commandName] = command
        // 将注解的commandName作为value，command作为key存入map
        commandNameMap[command] = commandName
        // 获取注解的argsCount
        val argsCount = annotation.argsCount
        // 将注解的argsCount作为value，command作为key存入map
        paramCountMap[command] = argsCount
    }

    fun getUsage(command: Commands): String {
        return translate(usageMap[command] ?: "")
    }

    fun getDescription(command: Commands): String {
        return translate(descriptionMap[command] ?: "")
    }

    fun getCommand(command: String): Commands? {
        return commandMap[command]
    }

    fun getParamCount(command: Commands): Int {
        return paramCountMap[command] ?: 0
    }

    // 获取命令名
    fun getCommandName(command: Commands): String {
        return commandNameMap[command] ?: ""
    }

    // 获取所有命令
    fun getCommands(): List<Commands> {
        return commands
    }

    fun translate(description: String): String {
        // 将描述翻译为指定的文字
        // 生成结果变量
        var result = description
        // 如果描述不为空，则将描述拆分为数组
        if (description.isNotEmpty()) {
            val descriptionArray = description.split(".")
            // 利用when来判断
            // 判断长度是否小于1
            if (descriptionArray.size == 3) {
                when (descriptionArray[0]) {
                    "command" -> {
                        when (descriptionArray[1]) {
                            "lucky" -> {
                                when (descriptionArray[2]) {
                                    "description" -> {
                                        result = Untils.config.commands.luckyDescription
                                    }
                                    "usage" -> {
                                        result = Untils.config.commands.luckyUsage
                                    }
                                }
                            }
                            "whitelist" -> {
                                when (descriptionArray[2]) {
                                    "description" -> {
                                        result = Untils.config.commands.whitelistDescription
                                    }
                                    "usage" -> {
                                        result = Untils.config.commands.whitelistUsage
                                    }
                                }
                            }
                            "whitelist-add" -> {
                                when (descriptionArray[2]) {
                                    "description" -> {
                                        result = Untils.config.commands.whitelistAddDescription
                                    }
                                    "usage" -> {
                                        result = Untils.config.commands.whitelistAddUsage
                                    }
                                }
                            }
                            "whitelist-remove" -> {
                                when (descriptionArray[2]) {
                                    "description" -> {
                                        result = Untils.config.commands.whitelistRemoveDescription
                                    }
                                    "usage" -> {
                                        result = Untils.config.commands.whitelistRemoveUsage
                                    }
                                }
                            }
                            "broadcast" -> {
                                when (descriptionArray[2]) {
                                    "description" -> {
                                        result = Untils.config.commands.broadcastDescription
                                    }
                                    "usage" -> {
                                        result = Untils.config.commands.broadcastUsage
                                    }
                                }
                            }
                            "online" -> {
                                when (descriptionArray[2]) {
                                    "description" -> {
                                        result = Untils.config.commands.onlineDescription
                                    }
                                    "usage" -> {
                                        result = Untils.config.commands.onlineUsage
                                    }
                                }
                            }
                            "debug" -> {
                                when (descriptionArray[2]) {
                                    "description" -> {
                                        result = Untils.config.commands.debugDescription
                                    }
                                    "usage" -> {
                                        result = Untils.config.commands.debugUsage
                                    }
                                }
                            }
                            "help" -> {
                                when (descriptionArray[2]) {
                                    "description" -> {
                                        result = Untils.config.commands.helpDescription
                                    }
                                    "usage" -> {
                                        result = Untils.config.commands.helpUsage
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        // 返回结果
        return result
    }
}