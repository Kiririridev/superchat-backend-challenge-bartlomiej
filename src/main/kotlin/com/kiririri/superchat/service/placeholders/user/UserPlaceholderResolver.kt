package com.kiririri.superchat.service.placeholders.user

import com.kiririri.superchat.core.MessageContent
import com.kiririri.superchat.core.Placeholder
import com.kiririri.superchat.core.PlaceholderType
import com.kiririri.superchat.repository.ChatUserRepository
import com.kiririri.superchat.service.placeholders.PlaceholderResolver
import javax.enterprise.context.ApplicationScoped

val USER_PLACEHOLDER_PATTERN: Regex = "\\[\\\$user=([a-zA-Z1-9]*)\\]".toRegex()

@ApplicationScoped
class UserPlaceholderResolver(private val chatUserRepository: ChatUserRepository) : PlaceholderResolver {

    override fun analyzePlaceholders(messageContent: String): List<Placeholder> {
        return findUserPlaceholders(messageContent).map {
            Placeholder(
                null,
                resolveUser(it.second),
                it.first,
                PlaceholderType.USER
            )
        }
    }

    private fun resolveUser(user: String) =
        chatUserRepository.getUserByUsername(user)?.toString() ?: "USER COULD NOT BE RESOLVED"

    override fun resolvePlaceholders(content: String, messageContent: MessageContent): String {
        val btcPlaceholders =
            messageContent.placeholders?.filter { it.placeholderType == PlaceholderType.USER }.orEmpty()
        return btcPlaceholders.sortedBy { it.index }.fold(content) { acc, placeholder ->
            acc.replaceFirst(USER_PLACEHOLDER_PATTERN, placeholder.value ?: "[NO VALUE]")
        }
    }

    private fun findUserPlaceholders(messageContent: String): List<Pair<Int, String>> {
        val matches = USER_PLACEHOLDER_PATTERN.findAll(messageContent).toList()
        return matches.map { it.groupValues[1] }.map { messageContent.indexOf(getUserPlaceholder(it)) to it }
    }

    private fun getUserPlaceholder(user: String): String {
        return "[\$user=$user]"
    }
}