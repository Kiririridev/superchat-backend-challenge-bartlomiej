package com.kiririri.superchat.service.exception

class ContactNotFoundException(contactId: Long) : Exception("Contact with id=[$contactId] not found")