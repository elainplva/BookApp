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
import com.example.bookapp.BookApplication
import com.example.bookapp.ui.screens.account.AccountScreen
import com.example.bookapp.ui.screens.account.AccountViewModel
import com.example.bookapp.ui.screens.account.AccountViewModelFactory
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
import com.example.bookapp.ui.screens.privacy.PrivacyScreen
import com.example.bookapp.ui.screens.privacy.PrivacyViewModel
import com.example.bookapp.ui.screens.privacy.PrivacyViewModelFactory
import com.example.bookapp.ui.screens.settings.SettingsScreen
import com.example.bookapp.ui.screens.settings.SettingsViewModel
import com.example.bookapp.ui.screens.settings.SettingsViewModelFactory


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object BookDetail : Screen("book_detail/{bookId}") {
        fun createRoute(bookId: Long) = "book_detail/$bookId"
    }
    object Favorites : Screen("favorites")
    object Login : Screen("login")
    object Settings : Screen("settings")
    object Account : Screen("account")
    object Privacy : Screen("privacy")
}

@Composable
fun NavGraph(navController: NavHostController) {
    val application = LocalContext.current.applicationContext as BookApplication

    // Get the singleton repository instances from the Application class.
    val bookRepository = remember { application.bookRepository }
    val userRepository = remember { application.userRepository }
    val settingsRepository = remember { application.settingsRepository }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(bookRepository)
            )
            HomeScreen(
                viewModel = viewModel,
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                },
                onFavoritesClick = { navController.navigate(Screen.Favorites.route) },
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(
            route = Screen.BookDetail.route,
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getLong("bookId") ?: 0L
            val viewModel: BookDetailViewModel = viewModel(
                factory = BookDetailViewModelFactory(bookRepository, bookId)
            )
            BookDetailScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Favorites.route) {
            val viewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModelFactory(bookRepository)
            )
            FavoritesScreen(
                viewModel = viewModel,
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Login.route) {
            val viewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(userRepository)
            )
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Settings.route) {
            val viewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(settingsRepository)
            )
            SettingsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onAccountClick = { navController.navigate(Screen.Account.route) },
                onPrivacyClick = { navController.navigate(Screen.Privacy.route) }
            )
        }

        composable(route = Screen.Account.route) {
            val viewModel: AccountViewModel = viewModel(
                factory = AccountViewModelFactory(userRepository)
            )
            AccountScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(route = Screen.Privacy.route) {
            val viewModel: PrivacyViewModel = viewModel(
                factory = PrivacyViewModelFactory(userRepository)
            )
            PrivacyScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}