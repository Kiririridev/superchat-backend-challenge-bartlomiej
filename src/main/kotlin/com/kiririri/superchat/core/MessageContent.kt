package com.kiririri.superchat.core

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class MessageContent(
    @Id @GeneratedValue
    var id: Long?,
    var plainMessageContent: String?,
    @OneToMany
    var placeholders: List<Placeholder>?
)