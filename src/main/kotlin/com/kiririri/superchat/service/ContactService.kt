package com.kiririri.superchat.service

import com.kiririri.superchat.controller.dto.CreateContactRequest
import com.kiririri.superchat.core.Contact
import com.kiririri.superchat.repository.ChatUserRepository
import com.kiririri.superchat.repository.ContactRepository
import com.kiririri.superchat.service.exception.UserNotFoundException
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ContactService(
    private val contactRepository: ContactRepository,
    private val chatUserRepository: ChatUserRepository
) {

    fun createContact(contactRequest: CreateContactRequest): Contact {
        val contactOwner = chatUserRepository.getUserById(contactRequest.contactOwnerId) ?: throw UserNotFoundException(contactRequest.contactOwnerId)
        val contactTarget = chatUserRepository.getUserById(contactRequest.contactTargetId) ?: throw UserNotFoundException(contactRequest.contactTargetId)

        val newContact = Contact(
            id = null,
            contactOwner = contactOwner,
            contactTarget = contactTarget,
            contactName = contactRequest.contactName,
            contactEmail = contactRequest.contactEmail
        )

        return contactRepository.saveContact(newContact)
    }

    fun listAllContactForContactOwner(contactOwnerId: Long): List<Contact> {
        val contactOwner = chatUserRepository.getUserById(contactOwnerId) ?: throw UserNotFoundException(contactOwnerId)

        return contactRepository.getAllContactsForContactOwner(contactOwner)
    }

    fun listAllContacts(): List<Contact> {
        return contactRepository.getAllContacts()
    }
}