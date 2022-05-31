package com.kiririri.superchat.service.dto

import com.kiririri.superchat.core.ChatUser
import java.sql.Timestamp

data class ResolvedMessage(
    val id: Long, val sender: ChatUser, val receiver: ChatUser, val messageContent: String, val timestamp: Timestamp
)

