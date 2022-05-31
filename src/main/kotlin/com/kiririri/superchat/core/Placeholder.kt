package com.kiririri.superchat.core

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Placeholder(
    @Id @GeneratedValue var id: Long?,
    var value: String?,
    var index: Int?,
    var placeholderType: PlaceholderType?
)

enum class PlaceholderType {
    BTC, USER
}