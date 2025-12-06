package com.example.bookapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var author: String,
    var description: String,
    val coverUrl: String? = null,
    var rating: Float = 0f,
    var review: String = "",
    var isFavorite: Boolean = false,
    var pdfPath: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
