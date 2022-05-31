package com.kiririri.superchat.core

import com.kiririri.superchat.util.NoArgConstructor
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne


@Entity
data class Contact constructor(
    @GeneratedValue @Id var id: Long?,
    @OneToOne var contactOwner: ChatUser?,
    @OneToOne var contactTarget: ChatUser?,
    var contactName: String?,
    var contactEmail: String?
)