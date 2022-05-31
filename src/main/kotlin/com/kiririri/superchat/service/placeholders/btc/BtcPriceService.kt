package com.kiririri.superchat.service.placeholders.btc

import java.math.BigDecimal
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class BtcPriceServiceClient {

    fun callExternalServiceToGetBitcoinPrice(): BigDecimal {
        return BigDecimal("28486.43")
    }
}