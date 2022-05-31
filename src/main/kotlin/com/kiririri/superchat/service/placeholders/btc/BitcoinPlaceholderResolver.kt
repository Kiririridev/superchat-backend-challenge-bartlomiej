package com.kiririri.superchat.service.placeholders.btc

import com.kiririri.superchat.core.MessageContent
import com.kiririri.superchat.core.Placeholder
import com.kiririri.superchat.core.PlaceholderType
import com.kiririri.superchat.service.placeholders.PlaceholderResolver
import javax.enterprise.context.ApplicationScoped

const val BTC_PLACEHOLDER: String = "[\$btc]"

@ApplicationScoped
class BitcoinPlaceholderResolver(private val btcPriceServiceClient: BtcPriceServiceClient) : PlaceholderResolver {

    override fun analyzePlaceholders(messageContent: String): List<Placeholder> {
        return findBtcPlaceholders(messageContent).map {
            Placeholder(null, getBtcPriceString(), it, PlaceholderType.BTC)
        }
    }

    override fun resolvePlaceholders(content: String, messageContent: MessageContent): String {
        val btcPlaceholders = messageContent.placeholders
            ?.filter { it.placeholderType == PlaceholderType.BTC }
            .orEmpty()

        return btcPlaceholders
            .sortedBy { it.index }
            .fold(content) { acc, placeholder ->
                acc.replaceFirst(BTC_PLACEHOLDER, placeholder.value ?: "[NO VALUE]")
            }
    }

    private fun findBtcPlaceholders(messageContent: String): List<Int> {
        val listOfIndexes = mutableListOf<Int>()
        var index = -1
        do {
            index = messageContent.indexOf(BTC_PLACEHOLDER, index + 1)
            if (index != -1) {
                listOfIndexes.add(index)
            }
        } while (index != -1)

        return listOfIndexes
    }

    private fun getBtcPriceString(): String {
        val btcPrice = btcPriceServiceClient.callExternalServiceToGetBitcoinPrice()

        return "$btcPrice EUR"
    }
}