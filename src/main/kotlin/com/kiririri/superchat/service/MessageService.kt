package com.kiririri.superchat.service

import com.kiririri.superchat.controller.dto.ExternalMessage
import com.kiririri.superchat.core.Message
import com.kiririri.superchat.core.MessageContent
import com.kiririri.superchat.repository.ChatUserRepository
import com.kiririri.superchat.repository.ContactRepository
import com.kiririri.superchat.repository.MessageContentRepository
import com.kiririri.superchat.repository.MessageRepository
import com.kiririri.superchat.service.dto.ConversationKey
import com.kiririri.superchat.service.dto.ResolvedMessage
import com.kiririri.superchat.service.exception.ContactNotFoundException
import com.kiririri.superchat.service.exception.UserNotFoundException
import com.kiririri.superchat.service.placeholders.PlaceholdersService
import java.sql.Timestamp
import java.time.Instant
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional

@ApplicationScoped
class MessageService(
    private val messageRepository: MessageRepository,
    private val contactRepository: ContactRepository,
    private val chatUserRepository: ChatUserRepository,
    private val chatUserService: ChatUserService,
    private val placeholderService: PlaceholdersService,
    private val messageContentRepository: MessageContentRepository
) {
    @Transactional
    fun sendMessageToContact(contactId: Long, messageContent: String): Message {
        val contact = contactRepository.getContact(contactId) ?: throw ContactNotFoundException(contactId)

        val parsedMessageContent = processMessageContent(messageContent)
        val newMessage = Message(null, contact.contactOwner, contact.contactTarget, parsedMessageContent, now())

        messageContentRepository.saveMessageContent(parsedMessageContent)
        return messageRepository.saveMessage(newMessage)
    }

    fun getConversationsForUser(contactOwnerId: Long): Map<ConversationKey, List<ResolvedMessage>> {
        val contactOwner = chatUserRepository.getUserById(contactOwnerId) ?: throw UserNotFoundException(contactOwnerId)

        return messageRepository.getAllMessagesForUser(contactOwner)
            .groupBy { ConversationKey.fromMessage(it) }
            .map { it.key to resolveMessageList(it.value) }
            .toMap()
    }

    @Transactional
    fun receiveExternalMessage(externalMessage: ExternalMessage): Message {
        val senderUser = chatUserService.getOrCreateExternalUser(externalMessage.senderExternalUsername)
        val receiverUser = chatUserService.getOrCreateExternalUser(externalMessage.receiverExternalUsername)

        val messageContent = processMessageContent(externalMessage.messageContent)
        val newMessage = Message(null, senderUser, receiverUser, messageContent, now())

        messageContentRepository.saveMessageContent(messageContent)
        return messageRepository.saveMessage(newMessage)
    }

    private fun resolveMessageList(messageList: List<Message>): List<ResolvedMessage> {
        return messageList.map { placeholderService.resolvePlaceholders(it) }
    }

    private fun processMessageContent(messageContent: String): MessageContent {
        val placeholders = placeholderService.analyzePlaceholders(messageContent)

        return MessageContent(null, messageContent, placeholders)
    }

    private fun now(): Timestamp {
        return Timestamp.from(Instant.now())
    }
}

