package com.kiririri.superchat.controller.dto

data class ExternalMessage(
    val senderExternalUsername: String,
    val receiverExternalUsername: String,
    val messageContent: String
)