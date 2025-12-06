package com.example.bookapp.ui.screens.bookdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.local.entities.BookEntity
import com.example.bookapp.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class BookDetailViewModel(
    private val repository: BookRepository,
    private val bookId: Long
) : ViewModel() {

    private val _book = MutableStateFlow<BookEntity?>(null)
    val book: StateFlow<BookEntity?> = _book.asStateFlow()

    private val _rating = MutableStateFlow(0f)
    val rating: StateFlow<Float> = _rating.asStateFlow()

    private val _review = MutableStateFlow("")
    val review: StateFlow<String> = _review.asStateFlow()

    init {
        loadBook()
    }

    private fun loadBook() {
        viewModelScope.launch {
            val bookData = repository.getBookById(bookId)
            _book.value = bookData
            _rating.value = bookData?.rating ?: 0f
            _review.value = bookData?.review ?: ""
        }
    }

    fun updateRating(newRating: Float) {
        _rating.value = newRating
    }

    fun updateReview(newReview: String) {
        _review.value = newReview
    }

    fun saveRatingAndReview() {
        viewModelScope.launch {
            repository.updateRatingAndReview(bookId, rating.value, review.value)
            loadBook()
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            _book.value?.let { currentBook ->
                repository.toggleFavorite(currentBook.id, currentBook.isFavorite)
                loadBook()
            }
        }
    }
}

class BookDetailViewModelFactory(
    private val repository: BookRepository,
    private val bookId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookDetailViewModel(repository, bookId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
