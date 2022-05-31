package com.kiririri.superchat.controller.handler

import com.kiririri.superchat.service.exception.ContactNotFoundException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider


@Provider
class ContactNotFoundExceptionHandler : ExceptionMapper<ContactNotFoundException> {

    override fun toResponse(e: ContactNotFoundException): Response {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(e.message)
            .build()
    }
}