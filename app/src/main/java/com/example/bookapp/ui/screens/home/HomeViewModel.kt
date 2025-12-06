package com.example.bookapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.local.entities.BookEntity
import com.example.bookapp.data.repository.BookRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class HomeViewModel(private val repository: BookRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val books: StateFlow<List<BookEntity>> = searchQuery
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.allBooks
            } else {
                repository.searchBooks(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(bookId: Long, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(bookId, currentStatus)
        }
    }

    fun addSampleBook() {
        viewModelScope.launch {
            val book = BookEntity(
                title = "Sample Book ${System.currentTimeMillis()}",
                author = "Sample Author",
                synopsis = "This is a sample book for testing purposes. It demonstrates the book app functionality with a longer synopsis to show how text wrapping works in the UI.",
                coverUrl = "https://via.placeholder.com/300x450/FF6B6B/FFFFFF?text=Book+Cover",
                rating = 4.5f,
                review = "",
                isFavorite = false
            )
            repository.insertBook(book)
        }
    }
}

class HomeViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
