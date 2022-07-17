package tech.egglink.bot.internal.commands

import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Stranger
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.StrangerMessageEvent

/**
 * 建议在传入参数时传入事件而不是直接传入捕获地方
 * */
class Sender(private val type: Any) {

    /**
     * 发送者是否为Group内的成员
     * */
    fun isGroup(): Boolean {
        return type is GroupMessageEvent
    }

    /**
     * 发送者是否为好友
     * */
    fun isFriend(): Boolean {
        return type is FriendMessageEvent
    }

    /**
     * 发送者是否为陌生人(临时会话)
     * */
    fun isStranger(): Boolean {
        return type is StrangerMessageEvent
    }

    /**
     * 可返回null 用于获取GroupMessageEvent实例 建议搭配 isGroup()
     * */
    fun getGroup(): GroupMessageEvent? {
        if (!isGroup())
            return null
        return type as GroupMessageEvent
    }

    /**
     * 可返回null 用于获取Friend实例 建议搭配 isFriend()
     * */
    fun getFriend(): FriendMessageEvent? {
        if (!isFriend())
            return null
        return type as FriendMessageEvent
    }

    /**
     * 可返回null 用于获取Stranger实例 建议搭配 isStranger()
     * */
    fun getStranger(): StrangerMessageEvent? {
        if (!isStranger())
            return null
        return type as StrangerMessageEvent
    }
}