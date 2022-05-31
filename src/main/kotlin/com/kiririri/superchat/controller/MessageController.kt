package com.kiririri.superchat.controller

import com.kiririri.superchat.controller.dto.NewMessage
import com.kiririri.superchat.core.Message
import com.kiririri.superchat.service.MessageService
import com.kiririri.superchat.service.dto.ResolvedMessage
import javax.ws.rs.GET
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.QueryParam

@Path("/message")
class MessageController(private val messageService: MessageService) {

    @PUT
    @Path("/send")
    fun sendMessage(
        messageContent: NewMessage
    ): Message {
        return messageService.sendMessageToContact(messageContent.contactId, messageContent.messageContent)
    }

    @GET
    @Path("/listByUser")
    fun listMessagesForContact(@QueryParam("userId") userId: Long): Map<List<Long>, List<ResolvedMessage>> {
        return messageService.getConversationsForUser(userId)
            .mapKeys { it.key.ids }
    }
}

