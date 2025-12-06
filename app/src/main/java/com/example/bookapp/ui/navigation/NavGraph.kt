package com.example.bookapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bookapp.data.local.database.BookDatabase
import com.example.bookapp.data.repository.BookRepository
import com.example.bookapp.data.repository.UserRepository
import com.example.bookapp.ui.screens.auth.AuthViewModel
import com.example.bookapp.ui.screens.auth.AuthViewModelFactory
import com.example.bookapp.ui.screens.auth.LoginScreen
import com.example.bookapp.ui.screens.bookdetail.BookDetailScreen
import com.example.bookapp.ui.screens.bookdetail.BookDetailViewModel
import com.example.bookapp.ui.screens.bookdetail.BookDetailViewModelFactory
import com.example.bookapp.ui.screens.favorites.FavoritesScreen
import com.example.bookapp.ui.screens.favorites.FavoritesViewModel
import com.example.bookapp.ui.screens.favorites.FavoritesViewModelFactory
import com.example.bookapp.ui.screens.home.HomeScreen
import com.example.bookapp.ui.screens.home.HomeViewModel
import com.example.bookapp.ui.screens.home.HomeViewModelFactory
import com.example.bookapp.ui.screens.settings.SettingsScreen

/**
 * Sealed class representing all navigation destinations in the app
 * Provides type-safe route definitions and route creation functions
 */
sealed class Screen(val route: String) {
    /**
     * Home screen route
     * Starting destination of the app
     */
    object Home : Screen("home")

    /**
     * Book detail screen route with dynamic bookId parameter
     * Shows detailed information about a specific book
     */
    object BookDetail : Screen("book_detail/{bookId}") {
        /**
         * Creates a route with the specific bookId
         * @param bookId The ID of the book to display
         * @return Complete route string with bookId parameter
         */
        fun createRoute(bookId: Long) = "book_detail/$bookId"
    }

    /**
     * Favorites screen route
     * Displays all books marked as favorites
     */
    object Favorites : Screen("favorites")

    /**
     * Login/Register screen route
     * Handles user authentication
     */
    object Login : Screen("login")

    /**
     * Settings screen route
     * Displays app settings and information
     */
    object Settings : Screen("settings")
}

/**
 * Main Navigation Graph
 * Defines all navigation routes and their corresponding screens
 * Manages navigation between different screens in the app
 *
 * @param navController Navigation controller for handling navigation actions
 */
@Composable
fun NavGraph(navController: NavHostController) {
    // Get application context
    val context = LocalContext.current

    // Initialize database and repositories
    // Use remember to create instances only once and survive recompositions
    val database = remember { BookDatabase.getDatabase(context) }
    val bookRepository = remember { BookRepository(database.bookDao()) }
    val userRepository = remember { UserRepository(database.userDao()) }

    // Define navigation host with all routes
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        // ==========================================
        // HOME SCREEN ROUTE
        // ==========================================
        composable(route = Screen.Home.route) {
            // Create ViewModel with factory
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(bookRepository)
            )

            // Display home screen with navigation callbacks
            HomeScreen(
                viewModel = viewModel,
                onBookClick = { bookId ->
                    // Navigate to book detail screen with bookId
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                },
                onFavoritesClick = {
                    // Navigate to favorites screen
                    navController.navigate(Screen.Favorites.route)
                },
                onLoginClick = {
                    // Navigate to login screen
                    navController.navigate(Screen.Login.route)
                },
                onSettingsClick = {
                    // Navigate to settings screen
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        // ==========================================
        // BOOK DETAIL SCREEN ROUTE
        // ==========================================
        composable(
            route = Screen.BookDetail.route,
            arguments = listOf(
                navArgument("bookId") {
                    type = NavType.LongType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            // Extract bookId from navigation arguments
            val bookId = backStackEntry.arguments?.getLong("bookId") ?: 0L

            // Create ViewModel with factory and bookId
            val viewModel: BookDetailViewModel = viewModel(
                factory = BookDetailViewModelFactory(bookRepository, bookId)
            )

            // Display book detail screen
            BookDetailScreen(
                viewModel = viewModel,
                onBackClick = {
                    // Navigate back to previous screen
                    navController.popBackStack()
                }
            )
        }

        // ==========================================
        // FAVORITES SCREEN ROUTE
        // ==========================================
        composable(route = Screen.Favorites.route) {
            // Create ViewModel with factory
            val viewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModelFactory(bookRepository)
            )

            // Display favorites screen
            FavoritesScreen(
                viewModel = viewModel,
                onBookClick = { bookId ->
                    // Navigate to book detail screen
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                },
                onBackClick = {
                    // Navigate back to previous screen (home)
                    navController.popBackStack()
                }
            )
        }

        // ==========================================
        // LOGIN/REGISTER SCREEN ROUTE
        // ==========================================
        composable(route = Screen.Login.route) {
            // Create ViewModel with factory
            val viewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(userRepository)
            )

            // Display login/register screen
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    // Navigate back to previous screen after successful login
                    navController.popBackStack()
                },
                onBackClick = {
                    // Navigate back to previous screen
                    navController.popBackStack()
                }
            )
        }

        // ==========================================
        // SETTINGS SCREEN ROUTE
        // ==========================================
        composable(route = Screen.Settings.route) {
            // Display settings screen (no ViewModel needed)
            SettingsScreen(
                onBackClick = {
                    // Navigate back to previous screen (home)
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * Navigation Helper Functions
 * Provide convenient methods for common navigation actions
 */

/**
 * Navigates to the home screen and clears the back stack
 * Useful for logout or app reset scenarios
 *
 * @param navController Navigation controller
 */
fun navigateToHomeAndClearBackStack(navController: NavHostController) {
    navController.navigate(Screen.Home.route) {
        // Pop up to the home screen and clear all back stack
        popUpTo(Screen.Home.route) {
            inclusive = true
        }
        // Avoid multiple copies of the same destination
        launchSingleTop = true
    }
}

/**
 * Navigates to a book detail screen with animation
 *
 * @param navController Navigation controller
 * @param bookId ID of the book to display
 */
fun navigateToBookDetail(navController: NavHostController, bookId: Long) {
    navController.navigate(Screen.BookDetail.createRoute(bookId)) {
        // Avoid multiple copies of the same destination
        launchSingleTop = true
    }
}

/**
 * Safely navigates back, or navigates to home if back stack is empty
 * Prevents app from closing unexpectedly
 *
 * @param navController Navigation controller
 */
fun navigateBackSafely(navController: NavHostController) {
    if (!navController.popBackStack()) {
        // If back stack is empty, navigate to home
        navigateToHomeAndClearBackStack(navController)
    }
}