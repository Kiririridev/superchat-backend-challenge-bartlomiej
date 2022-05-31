package com.kiririri.superchat.repository

import com.kiririri.superchat.core.MessageContent
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

@ApplicationScoped
class MessageContentRepository(private val entityManager: EntityManager) {

    fun saveMessageContent(messageContent: MessageContent): MessageContent {
        messageContent.placeholders?.forEach {
            entityManager.persist(it)
        }
        entityManager.persist(messageContent)

        return messageContent
    }
}