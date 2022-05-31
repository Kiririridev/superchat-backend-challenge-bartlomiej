package com.kiririri.superchat.service.placeholders

import com.kiririri.superchat.core.Message
import com.kiririri.superchat.core.Placeholder
import com.kiririri.superchat.service.dto.ResolvedMessage
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Instance

@ApplicationScoped
class PlaceholdersService(
    private val placeholderResolvers: Instance<PlaceholderResolver>
) {

    fun analyzePlaceholders(messageContent: String): List<Placeholder> {
        return placeholderResolvers.flatMap {
            it.analyzePlaceholders(messageContent)
        }
    }

    fun resolvePlaceholders(message: Message): ResolvedMessage {
        val resolvedMessageContent = placeholderResolvers
            .fold(message.messageContent!!.plainMessageContent.orEmpty()) { content, resolver ->
                resolver.resolvePlaceholders(content, message.messageContent!!)
            }

        return ResolvedMessage(
            message.id!!,
            message.sender!!,
            message.receiver!!,
            resolvedMessageContent,
            message.timestamp!!
        )
    }
}