package com.example.bookapp.data.repository

import com.example.bookapp.data.local.dao.BookDao
import com.example.bookapp.data.local.entities.BookEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository for Book data operations
 * Acts as a single source of truth for book data
 */
class BookRepository(private val bookDao: BookDao) {

    // Get all books as Flow (auto-updates UI)
    val allBooks: Flow<List<BookEntity>> = bookDao.getAllBooks()

    // Get favorite books
    val favoriteBooks: Flow<List<BookEntity>> = bookDao.getFavoriteBooks()

    // Search books
    fun searchBooks(query: String): Flow<List<BookEntity>> {
        return bookDao.searchBooks(query)
    }

    // Get single book
    suspend fun getBookById(bookId: Int): BookEntity? {
        return bookDao.getBookById(bookId)
    }

    // Insert new book
    suspend fun insertBook(book: BookEntity): Long {
        return bookDao.insertBook(book)
    }

    // Update book
    suspend fun updateBook(book: BookEntity) {
        bookDao.updateBook(book)
    }

    // Toggle favorite status
    suspend fun toggleFavorite(bookId: Int, isFavorite: Boolean) {
        bookDao.updateFavoriteStatus(bookId, isFavorite)
    }

    // Update rating and review
    suspend fun updateRatingAndReview(bookId: Int, rating: Float, review: String) {
        bookDao.updateRatingAndReview(bookId, rating, review)
    }

    // Update PDF path
    suspend fun updatePdfPath(bookId: Int, pdfPath: String?) {
        bookDao.updatePdfPath(bookId, pdfPath)
    }

    // Delete book
    suspend fun deleteBook(book: BookEntity) {
        bookDao.deleteBook(book)
    }
}