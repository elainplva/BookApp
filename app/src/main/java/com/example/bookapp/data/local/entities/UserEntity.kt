package com.example.bookapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var username: String,
    var email: String,
    var password: String,
    var isLoggedIn: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
