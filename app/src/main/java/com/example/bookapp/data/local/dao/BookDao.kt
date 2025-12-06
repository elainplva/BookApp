package com.example.bookapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bookapp.data.local.entities.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity): Long

    @Query("SELECT * FROM books ORDER BY createdAt DESC")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: Long): BookEntity?

    @Query("SELECT * FROM books WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'" )
    fun searchBooks(query: String): Flow<List<BookEntity>>

    @Update
    suspend fun updateBook(book: BookEntity)

    @Query("UPDATE books SET isFavorite = :isFavorite WHERE id = :bookId")
    suspend fun updateFavoriteStatus(bookId: Long, isFavorite: Boolean)

    @Query("UPDATE books SET rating = :rating, review = :review WHERE id = :bookId")
    suspend fun updateRatingAndReview(bookId: Long, rating: Float, review: String)

    @Query("UPDATE books SET pdfPath = :pdfPath WHERE id = :bookId")
    suspend fun updatePdfPath(bookId: Long, pdfPath: String?)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBookById(bookId: Long)
}
