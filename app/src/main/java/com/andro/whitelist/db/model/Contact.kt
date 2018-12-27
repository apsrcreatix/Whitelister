package com.andro.whitelist.db.model

import androidx.room.Entity

@Entity(primaryKeys = ["id"])
data class Contact(
    val id: String,
    val name: String,
    val phoneNumber: String
)