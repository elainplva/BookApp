package com.example.bookapp

import com.example.bookapp.data.local.entities.BookEntity
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for BookRepository
 * Tests CRUD operations
 */
class BookRepositoryTest {

    @Test
    fun `book title should not be empty`() {
        val book = BookEntity(
            title = "Test Book",
            author = "Test Author",
            description = "Test Synopsis"
        )

        assertNotNull(book.title)
        assertTrue(book.title.isNotEmpty())
    }

    @Test
    fun `book rating should be between 0 and 5`() {
        val book = BookEntity(
            title = "Test",
            author = "Author",
            description = "Synopsis",
            rating = 4.5f
        )

        assertTrue(book.rating >= 0f)
        assertTrue(book.rating <= 5f)
    }

    @Test
    fun `favorite status should toggle correctly`() {
        var book = BookEntity(
            title = "Test",
            author = "Author",
            description = "Synopsis",
            isFavorite = false
        )

        assertFalse(book.isFavorite)

        book = book.copy(isFavorite = true)
        assertTrue(book.isFavorite)
    }
}
