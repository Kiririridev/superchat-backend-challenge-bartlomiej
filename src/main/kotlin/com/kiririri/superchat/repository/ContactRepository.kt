package com.kiririri.superchat.repository

import com.kiririri.superchat.core.ChatUser
import com.kiririri.superchat.core.Contact
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import javax.transaction.Transactional

@ApplicationScoped
class ContactRepository(private val entityManager: EntityManager) {

    @Transactional
    fun saveContact(contact: Contact): Contact {
        entityManager.persist(contact)

        return contact
    }

    fun getContact(contactId: Long): Contact? {
        return entityManager.find(Contact::class.java, contactId)
    }

    fun getAllContactsForContactOwner(contactOwner: ChatUser): List<Contact> {
        val queryCriteria = getQueryCriteriaForContactOwnerFiltering(contactOwner)

        return entityManager.createQuery(queryCriteria).resultList.toList()
    }

    fun getAllContacts(): List<Contact> {
        val query = getQueryForAllContacts()

        return entityManager.createQuery(query).resultList.toList()
    }

    private fun getQueryForAllContacts(): CriteriaQuery<Contact> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Contact::class.java)

        val contactRoot: Root<Contact> = query.from(Contact::class.java)

        return query.select(contactRoot)
    }

    private fun getQueryCriteriaForContactOwnerFiltering(contactOwner: ChatUser): CriteriaQuery<Contact> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Contact::class.java)

        val contactRoot: Root<Contact> = query.from(Contact::class.java)

        return query.select(contactRoot).where(
            cb.equal(contactRoot.get<Long>("contactOwner"), contactOwner.id)
        )
    }
}
