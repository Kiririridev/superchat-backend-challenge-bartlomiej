package com.kiririri.superchat.service.dto

import com.kiririri.superchat.core.Message

data class ConversationKey(val ids: List<Long>) {

    companion object {
        fun fromMessage(message: Message): ConversationKey {
            return ConversationKey(listOf(message.sender!!.id!!, message.receiver!!.id!!).sorted())
        }
    }
}