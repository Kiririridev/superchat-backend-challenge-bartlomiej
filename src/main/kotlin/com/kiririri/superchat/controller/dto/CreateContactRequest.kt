package com.kiririri.superchat.controller.dto

data class CreateContactRequest(
    val contactOwnerId: Long,
    val contactTargetId: Long,
    val contactName: String,
    val contactEmail: String
)