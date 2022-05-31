package com.kiririri.superchat.controller.handler

import com.kiririri.superchat.service.exception.UserNotFoundException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider


@Provider
class UserNotFoundExceptionHandler : ExceptionMapper<UserNotFoundException> {

    override fun toResponse(e: UserNotFoundException): Response {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(e.message)
            .build()
    }
}