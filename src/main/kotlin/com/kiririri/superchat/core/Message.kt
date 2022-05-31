package com.kiririri.superchat.core

import java.sql.Timestamp
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
class Message constructor(
    @Id @GeneratedValue var id: Long?,
    @OneToOne
    var sender: ChatUser?,
    @OneToOne
    var receiver: ChatUser?,
    @OneToOne
    var messageContent: MessageContent?,
    var timestamp: Timestamp?
)
