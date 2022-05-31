package com.kiririri.superchat.controller

import com.kiririri.superchat.controller.dto.ExternalMessage
import com.kiririri.superchat.core.Message
import com.kiririri.superchat.service.MessageService
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType

@Path("/external")
class ExternalMessageController(private val messageService: MessageService) {

    @POST
    @Path("/receiveMessage")
    @Consumes(MediaType.APPLICATION_JSON)
    fun receiveExternalMessage(externalMessage: ExternalMessage): Message {
        return messageService.receiveExternalMessage(externalMessage)
    }
}

