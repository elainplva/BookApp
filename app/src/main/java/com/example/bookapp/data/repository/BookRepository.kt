package com.example.bookapp.data.repository

import com.example.bookapp.data.local.dao.BookDao
import com.example.bookapp.data.local.entities.BookEntity
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {

    val allBooks: Flow<List<BookEntity>> = bookDao.getAllBooks()

    val favoriteBooks: Flow<List<BookEntity>> = bookDao.getFavoriteBooks()

    fun searchBooks(query: String): Flow<List<BookEntity>> {
        return bookDao.searchBooks(query)
    }

    suspend fun getBookById(bookId: Long): BookEntity? {
        return bookDao.getBookById(bookId)
    }

    suspend fun insertBook(book: BookEntity): Long {
        return bookDao.insertBook(book)
    }

    suspend fun updateBook(book: BookEntity) {
        bookDao.updateBook(book)
    }

    suspend fun toggleFavorite(bookId: Long, isFavorite: Boolean) {
        bookDao.updateFavoriteStatus(bookId, isFavorite)
    }

    suspend fun updateRatingAndReview(bookId: Long, rating: Float, review: String) {
        bookDao.updateRatingAndReview(bookId, rating, review)
    }

    suspend fun updatePdfPath(bookId: Long, pdfPath: String?) {
        bookDao.updatePdfPath(bookId, pdfPath)
    }

    suspend fun deleteBook(book: BookEntity) {
        bookDao.deleteBook(book)
    }
}
