package com.kiririri.superchat.controller

import com.kiririri.superchat.controller.dto.CreateContactRequest
import com.kiririri.superchat.core.Contact
import com.kiririri.superchat.service.ContactService
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/contact")
class ContactController(private val contactService: ContactService) {

    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun addContact(contactRequest: CreateContactRequest): Contact {
        return contactService.createContact(contactRequest)
    }

    @Path("/listByOwner")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getContacts(@QueryParam("contactOwnerId") contactOwnerId: Long): List<Contact> {
        return contactService.listAllContactForContactOwner(contactOwnerId)
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllContacts(): List<Contact> {
        return contactService.listAllContacts()
    }
}

