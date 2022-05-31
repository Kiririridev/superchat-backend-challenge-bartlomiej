package com.kiririri.superchat.repository

import com.kiririri.superchat.core.ChatUser
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import javax.transaction.Transactional

@ApplicationScoped
class ChatUserRepository(private val entityManager: EntityManager) {

    @Transactional
    fun addUser(username: String, externalUsername: String?): ChatUser {
        val chatUser = ChatUser(null, username, externalUsername)

        entityManager.persist(chatUser)

        return chatUser
    }

    fun getUserById(userId: Long): ChatUser? {
        return entityManager.find(ChatUser::class.java, userId)
    }

    fun getUserByExternalUsername(externalUsername: String): ChatUser? {
        val queryCriteria = getQueryCriteriaForFindByExternalUsername(externalUsername)

        return entityManager.createQuery(queryCriteria).resultList.singleOrNull()
    }

    fun getUserByUsername(userName: String): ChatUser? {
        val queryCriteria = getQueryCriteriaForFindByUsername(userName)

        return entityManager.createQuery(queryCriteria).resultList.singleOrNull()
    }

    fun getAllUsers(): List<ChatUser> {
        val queryCriteria = getQueryCriteriaForAllUsers()

        return entityManager.createQuery(queryCriteria).resultList
    }

    @Transactional
    fun deleteUser(userId: Long) {
        val chatUser = entityManager.find(ChatUser::class.java, userId)

        entityManager.remove(chatUser)
    }

    private fun getQueryCriteriaForAllUsers(): CriteriaQuery<ChatUser> {
        val query = entityManager.criteriaBuilder.createQuery(ChatUser::class.java)
        val contactRoot: Root<ChatUser> = query.from(ChatUser::class.java)
        query.select(contactRoot)

        return query
    }

    private fun getQueryCriteriaForFindByExternalUsername(externalUsername: String): CriteriaQuery<ChatUser> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(ChatUser::class.java)
        val contactRoot: Root<ChatUser> = query.from(ChatUser::class.java)
        query.select(contactRoot).where(
            cb.equal(contactRoot.get<String>("externalUsername"), externalUsername)
        )

        return query
    }

    private fun getQueryCriteriaForFindByUsername(username: String): CriteriaQuery<ChatUser> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(ChatUser::class.java)
        val contactRoot: Root<ChatUser> = query.from(ChatUser::class.java)
        query.select(contactRoot).where(
            cb.equal(contactRoot.get<String>("username"), username)
        )

        return query
    }
}