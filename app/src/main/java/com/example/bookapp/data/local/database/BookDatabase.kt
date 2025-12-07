package com.example.bookapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookapp.data.local.dao.BookDao
import com.example.bookapp.data.local.dao.NoteDao
import com.example.bookapp.data.local.dao.UserDao
import com.example.bookapp.data.local.entities.BookEntity
import com.example.bookapp.data.local.entities.NoteEntity
import com.example.bookapp.data.local.entities.UserEntity

@Database(
    entities = [BookEntity::class, UserEntity::class, NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_database"
                )
                    .allowMainThreadQueries() // Allow queries on the main thread (temporary fix)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
