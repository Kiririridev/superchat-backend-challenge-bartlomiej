package com.kiririri.superchat.service.placeholders

import com.kiririri.superchat.core.MessageContent
import com.kiririri.superchat.core.Placeholder

interface PlaceholderResolver {

    fun analyzePlaceholders(messageContent: String, ): List<Placeholder>

    fun resolvePlaceholders(content: String, messageContent: MessageContent): String
}
