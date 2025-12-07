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
 * Automatically loads books on first launch
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

    // All books for checking if database is empty
    val allBooksForCheck: StateFlow<List<BookEntity>> = repository.allBooks
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

    /**
     * Loads books into database if it's empty
     * Called automatically when HomeScreen is first displayed
     */
    fun loadBooksIfNeeded() {
        viewModelScope.launch {
            // Check if database already has books
            val existingBooks = repository.allBooks.first()

            if (existingBooks.isEmpty()) {
                // Database is empty, load initial books
                loadInitialBooks()
            }
        }
    }

    /**
     * Loads initial set of books into the database
     */
    private suspend fun loadInitialBooks() {
        val books = listOf(
            BookEntity(
                title = "Harry Potter and the Philosopher's Stone",
                author = "J.K. Rowling",
                synopsis = "Harry Potter has never even heard of Hogwarts when the letters start dropping on the doormat at number four, Privet Drive. Addressed in green ink on yellowish parchment with a purple seal, they are swiftly confiscated by his grisly aunt and uncle.",
                coverUrl = "https://m.media-amazon.com/images/I/81m1s4wIPML._AC_UF1000,1000_QL80_.jpg",
                rating = 4.8f,
                review = "",
                isFavorite = false
            ),
            BookEntity(
                title = "Throne of Glass",
                author = "Sarah J. Maas",
                synopsis = "After serving out a year of hard labor in the salt mines of Endovier for her crimes, 18-year-old assassin Celaena Sardothien is dragged before the Crown Prince. Prince Dorian offers her her freedom on one condition: she must act as his champion in a competition to find a new royal assassin.",
                coverUrl = "https://m.media-amazon.com/images/I/81i9+4c-uUL._AC_UF1000,1000_QL80_.jpg",
                rating = 4.6f,
                review = "",
                isFavorite = false
            ),
            BookEntity(
                title = "To Kill a Mockingbird",
                author = "Harper Lee",
                synopsis = "The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it, To Kill A Mockingbird became both an instant bestseller and a critical success when it was first published in 1960. It went on to win the Pulitzer Prize in 1961 and was later made into an Academy Award-winning film, also a classic.",
                coverUrl = "https://m.media-amazon.com/images/I/81a4kCNuH+L._AC_UF1000,1000_QL80_.jpg",
                rating = 4.9f,
                review = "",
                isFavorite = false
            ),
            BookEntity(
                title = "Narnia: The Lion, the Witch and the Wardrobe",
                author = "C.S. Lewis",
                synopsis = "Narnia… a land of talking animals and mythical creatures that one has only ever dreamed of… a world where magic meets reality…",
                coverUrl = "https://m.media-amazon.com/images/I/81f2M1sQ+2L._AC_UF1000,1000_QL80_.jpg",
                rating = 4.7f,
                review = "",
                isFavorite = false
            ),
            BookEntity(
                title = "1984",
                author = "George Orwell",
                synopsis = "Among the seminal texts of the 20th century, Nineteen Eighty-Four is a rare work that grows more haunting as its futuristic purgatory becomes more real. Published in 1949, the book offers political satirist George Orwell's nightmarish vision of a totalitarian, bureaucratic world and one poor stiff's attempt to find individuality.",
                coverUrl = "https://m.media-amazon.com/images/I/71kxa1-0mfL._AC_UF1000,1000_QL80_.jpg",
                rating = 4.7f,
                review = "",
                isFavorite = false
            ),
            BookEntity(
                title = "Pride and Prejudice",
                author = "Jane Austen",
                synopsis = "Since its immediate success in 1813, Pride and Prejudice has remained one of the most popular novels in the English language. Jane Austen called this brilliant work 'her own darling child' and its vivacious heroine, Elizabeth Bennet, 'as delightful a creature as ever appeared in print.'",
                coverUrl = "https://m.media-amazon.com/images/I/71Q1tPupKjL._AC_UF1000,1000_QL80_.jpg",
                rating = 4.8f,
                review = "",
                isFavorite = false
            ),
            BookEntity(
                title = "The Hobbit",
                author = "J.R.R. Tolkien",
                synopsis = "This is the story of how a Baggins had an adventure, and found himself doing and saying things altogether unexpected. Bilbo Baggins is a hobbit who enjoys a comfortable, unambitious life, rarely travelling further than the pantry of his hobbit-hole in Bag End.",
                coverUrl = "https://m.media-amazon.com/images/I/712cDO7d73L._AC_UF1000,1000_QL80_.jpg",
                rating = 4.9f,
                review = "",
                isFavorite = false
            ),
            BookEntity(
                title = "The Great Gatsby",
                author = "F. Scott Fitzgerald",
                synopsis = "The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald. Set in the Jazz Age on Long Island, near New York City, the novel depicts first-person narrator Nick Carraway's interactions with mysterious millionaire Jay Gatsby and Gatsby's obsession to reunite with his former lover, Daisy Buchanan.",
                coverUrl = "https://m.media-amazon.com/images/I/71FTb9X6wsL._AC_UF1000,1000_QL80_.jpg",
                rating = 4.5f,
                review = "",
                isFavorite = false
            ),
            BookEntity(
                title = "The Catcher in the Rye",
                author = "J.D. Salinger",
                synopsis = "The hero-narrator of The Catcher in the Rye is an ancient child of sixteen, a native New Yorker named Holden Caulfield. Through circumstances that tend to preclude adult, secondhand description, he leaves his prep school in Pennsylvania and goes underground in New York City for three days.",
                coverUrl = "https://m.media-amazon.com/images/I/8125BDk3l9L._AC_UF1000,1000_QL80_.jpg",
                rating = 4.4f,
                review = "",
                isFavorite = false
            ),
            BookEntity(
                title = "Lord of the Flies",
                author = "William Golding",
                synopsis = "At the dawn of the next world war, a plane crashes on an uncharted island, stranding a group of schoolboys. At first, with no adult supervision, their freedom is something to celebrate. This far from civilization they can do anything they want. Anything.",
                coverUrl = "https://m.media-amazon.com/images/I/71JLTa9GmpL._AC_UF1000,1000_QL80_.jpg",
                rating = 4.3f,
                review = "",
                isFavorite = false
            )
        )

        // Insert all books into database
        books.forEach { book ->
            repository.insertBook(book)
        }
    }

    /**
     * Optional: Add a sample book for testing
     */
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