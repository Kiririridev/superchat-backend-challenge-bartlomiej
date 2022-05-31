package com.kiririri.superchat.controller

import com.kiririri.superchat.core.ChatUser
import com.kiririri.superchat.service.ChatUserService
import javax.ws.rs.*

@Path("/user")
class UserController(private val chatUserService: ChatUserService) {

    @POST
    @Path("add")
    fun addUser(
        @QueryParam("username") username: String, @QueryParam("externalUsername") externalUsername: String?
    ): ChatUser {
        return chatUserService.createUser(username, externalUsername)
    }

    @GET
    @Path("all")
    fun getAllUsers(): List<ChatUser> {
        return chatUserService.listAllUsers()
    }

    @DELETE
    @Path("delete")
    fun deleteUser(@QueryParam("userId") userId: Long) {
        chatUserService.deleteUser(userId)
    }
}