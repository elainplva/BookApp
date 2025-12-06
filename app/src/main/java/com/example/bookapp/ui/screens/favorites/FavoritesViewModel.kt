package com.example.bookapp.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.local.entities.BookEntity
import com.example.bookapp.data.repository.BookRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: BookRepository) : ViewModel() {

    val favoriteBooks: StateFlow<List<BookEntity>> = repository.favoriteBooks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun toggleFavorite(bookId: Long, currentStatus: Boolean) {
        // Launch a coroutine in the viewModelScope
        // This ensures the operation is cancelled if the ViewModel is cleared
        viewModelScope.launch {
            // Call repository to update favorite status
            // The repository will toggle the current status
            repository.toggleFavorite(bookId, currentStatus)

            // No need to manually update the UI here
            // The StateFlow will automatically emit the new list
            // when the database is updated
        }
    }

    fun getFavoriteCount(): Int {
        return favoriteBooks.value.size
    }

    fun clearAllFavorites() {
        viewModelScope.launch {
            favoriteBooks.value.forEach { book ->
                repository.toggleFavorite(book.id.toLong(), book.isFavorite)
            }
        }
    }

    fun isBookFavorited(bookId: Long): Boolean {
        return favoriteBooks.value.any { it.id.toLong() == bookId }
    }
}


class FavoritesViewModelFactory(
    private val repository: BookRepository
) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the given ViewModel class
     *
     * @param modelClass The class of the ViewModel to create
     * @return A newly created ViewModel instance
     * @throws IllegalArgumentException if the ViewModel class is unknown
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel class matches FavoritesViewModel
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            // Create and return the ViewModel with the repository
            return FavoritesViewModel(repository) as T
        }
        // Throw exception if the ViewModel class is not recognized
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}