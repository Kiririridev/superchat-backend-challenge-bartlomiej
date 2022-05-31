package com.kiririri.superchat.repository

import com.kiririri.superchat.core.ChatUser
import com.kiririri.superchat.core.Message
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaQuery

@ApplicationScoped
class MessageRepository(private val entityManager: EntityManager) {

    fun saveMessage(message: Message): Message {
        entityManager.persist(message)

        return message
    }

    fun getAllMessagesForUser(user: ChatUser): List<Message> {
        val queryCriteria = getCriteriaForMessagesQueryForChatUser(user)

        return entityManager.createQuery(queryCriteria).resultList
    }

    private fun getCriteriaForMessagesQueryForChatUser(user: ChatUser): CriteriaQuery<Message> {
        val cb = entityManager.criteriaBuilder

        val query = cb.createQuery(Message::class.java)
        val messageRoot = query.from(Message::class.java)
        val receiver = messageRoot.get<String>("receiver")
        val sender = messageRoot.get<String>("sender")

        val whereClause = cb.or(
            cb.equal(receiver, user.id),
            cb.equal(sender, user.id)
        )

        return query.where(whereClause)
    }
}

