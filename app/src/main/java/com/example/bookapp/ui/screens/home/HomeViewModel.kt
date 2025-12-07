package com.example.bookapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.local.entities.BookEntity
import com.example.bookapp.data.repository.BookRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * HomeViewModel - Manages home screen state and book operations
 */
class HomeViewModel(private val repository: BookRepository) : ViewModel() {

    // Search query state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Books based on search query
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

    /**
     * Updates search query
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    /**
     * Toggles favorite status of a book
     */
    fun toggleFavorite(bookId: Long, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(bookId, currentStatus)
        }
    }
}

/**
 * Factory for creating HomeViewModel with dependencies
 */
class HomeViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
