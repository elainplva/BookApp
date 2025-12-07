package com.example.bookapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bookapp.data.local.dao.BookDao
import com.example.bookapp.data.local.dao.NoteDao
import com.example.bookapp.data.local.dao.UserDao
import com.example.bookapp.data.local.entities.BookEntity
import com.example.bookapp.data.local.entities.NoteEntity
import com.example.bookapp.data.local.entities.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(CoroutineScope(Dispatchers.IO)))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                scope.launch {
                    prepopulateDatabase(it.bookDao())
                }
            }
        }

        suspend fun prepopulateDatabase(bookDao: BookDao) {
            val books = listOf(
                BookEntity(
                    title = "Harry Potter and the Philosopher's Stone",
                    author = "J.K. Rowling",
                    synopsis = "Harry Potter has never even heard of Hogwarts when the letters start dropping on the doormat at number four, Privet Drive. Addressed in green ink on yellowish parchment with a purple seal, they are swiftly confiscated by his grisly aunt and uncle.",
                    coverUrl = "https://m.media-amazon.com/images/I/81m1s4wIPML._AC_UF1000,1000_QL80_.jpg"
                ),
                BookEntity(
                    title = "Throne of Glass",
                    author = "Sarah J. Maas",
                    synopsis = "After serving out a year of hard labor in the salt mines of Endovier for her crimes, 18-year-old assassin Celaena Sardothien is dragged before the Crown Prince. Prince Dorian offers her her freedom on one condition: she must act as his champion in a competition to find a new royal assassin.",
                    coverUrl = "https://m.media-amazon.com/images/I/81i9+4c-uUL._AC_UF1000,1000_QL80_.jpg"
                ),
                BookEntity(
                    title = "To Kill a Mockingbird",
                    author = "Harper Lee",
                    synopsis = "The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it, To Kill A Mockingbird became both an instant bestseller and a critical success when it was first published in 1960. It went on to win the Pulitzer Prize in 1961 and was later made into an Academy Award-winning film, also a classic.",
                    coverUrl = "https://m.media-amazon.com/images/I/81a4kCNuH+L._AC_UF1000,1000_QL80_.jpg"
                ),
                BookEntity(
                    title = "Narnia The Lion, the Witch and the Wardrobe",
                    author = "C.S. Lewis",
                    synopsis = "Narnia… a land of talking animals and mythical creatures that one has only ever dreamed of… a world where magic meets reality…",
                    coverUrl = "https://m.media-amazon.com/images/I/81f2M1sQ+2L._AC_UF1000,1000_QL80_.jpg"
                )
            )
            books.forEach { bookDao.insertBook(it) }
        }
    }
}
