package com.kiririri.superchat.service

import com.kiririri.superchat.core.ChatUser
import com.kiririri.superchat.repository.ChatUserRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ChatUserService(private val chatUserRepository: ChatUserRepository) {

    fun createUser(username: String, externalUsername: String?): ChatUser {
        return chatUserRepository.addUser(username, externalUsername)
    }

    fun getOrCreateExternalUser(externalUsername: String): ChatUser {
        val externalUser = chatUserRepository.getUserByExternalUsername(externalUsername)

        return externalUser ?: chatUserRepository.addUser("$externalUsername-EXTERNAL", externalUsername)
    }

    fun listAllUsers(): List<ChatUser> {
        return chatUserRepository.getAllUsers()
    }

    fun deleteUser(userId: Long) {
        chatUserRepository.deleteUser(userId)
    }
}