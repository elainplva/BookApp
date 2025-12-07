package com.example.bookapp

import android.app.Application
import androidx.work.Configuration
import com.example.bookapp.data.local.database.BookDatabase

class BookApplication : Application(), Configuration.Provider {

    val database: BookDatabase by lazy { BookDatabase.getDatabase(this) }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}
