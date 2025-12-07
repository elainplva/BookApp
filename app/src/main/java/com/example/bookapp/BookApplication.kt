package com.example.bookapp

import android.app.Application
import androidx.work.Configuration
import com.example.bookapp.data.local.database.BookDatabase
import com.example.bookapp.data.repository.BookRepository
import com.example.bookapp.data.repository.UserRepository

/**
 * Custom Application class to provide a single instance of the database and repositories.
 */
class BookApplication : Application(), Configuration.Provider {

    // Using by lazy so the database and repositories are only created when they're first needed
    val database: BookDatabase by lazy { BookDatabase.getDatabase(this) }
    val bookRepository: BookRepository by lazy { BookRepository(database.bookDao()) }
    val userRepository: UserRepository by lazy { UserRepository(database.userDao()) }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}
