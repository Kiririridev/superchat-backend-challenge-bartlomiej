package com.kiririri.superchat.service.exception

class UserNotFoundException(contactOwnerId: Long) : Exception("User with id=[$contactOwnerId] not found")