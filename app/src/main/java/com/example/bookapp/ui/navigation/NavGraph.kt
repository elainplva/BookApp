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
 * Sealed class representing all navigation destinations
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object BookDetail : Screen("book_detail/{bookId}") {
        fun createRoute(bookId: Int) = "book_detail/$bookId"
    }
    object Favorites : Screen("favorites")
    object Login : Screen("login")
    object Settings : Screen("settings")
}

/**
 * Main Navigation Graph
 * Handles navigation between all screens
 */
@Composable
fun NavGraph(
    navController: NavHostController
) {
    val context = LocalContext.current

    // Initialize database and repositories
    val database = remember { BookDatabase.getDatabase(context) }
    val bookRepository = remember { BookRepository(database.bookDao()) }
    val userRepository = remember { UserRepository(database.userDao()) }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Home Screen
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(bookRepository)
            )

            HomeScreen(
                viewModel = viewModel,
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorites.route)
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        // Book Detail Screen
        composable(
            route = Screen.BookDetail.route,
            arguments = listOf(
                navArgument("bookId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?: 0

            val viewModel: BookDetailViewModel = viewModel(
                factory = BookDetailViewModelFactory(bookRepository, bookId)
            )

            BookDetailScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Favorites Screen
        composable(Screen.Favorites.route) {
            val viewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModelFactory(bookRepository)
            )

            FavouritesScreen(
                viewModel = viewModel,
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Login Screen
        composable(Screen.Login.route) {
            val viewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(userRepository)
            )

            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Settings Screen
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}