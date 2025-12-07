package com.example.bookapp.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Page title
            Text(
                text = "App Settings",
                style = MaterialTheme.typography.headlineSmall
            )

            // App Info Card
            AppInfoCard()

            Spacer(modifier = Modifier.height(8.dp))

            // Features Card
            FeaturesCard()

            Spacer(modifier = Modifier.height(8.dp))

            // Technical Details Card
            TechnicalDetailsCard()
        }
    }
}

/**
 * App Info Card Component
 * Displays basic app information
 */
@Composable
fun AppInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App icon
            Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )

            // App details
            Column {
                Text(
                    text = "BookApp",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Version 1.0.0",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Mobile Applications Assignment 2",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

/**
 * Features Card Component
 * Lists all implemented features of the app
 */
@Composable
fun FeaturesCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Section title
            Text(
                text = "Features",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Feature list
            FeatureItem(
                icon = Icons.Default.Book,
                text = "Book Management - Add, view, and manage your book collection"
            )
            FeatureItem(
                icon = Icons.Default.Search,
                text = "Search Functionality - Find books by title or author"
            )
            FeatureItem(
                icon = Icons.Default.Favorite,
                text = "Favorites System - Mark and organize your favorite books"
            )
            FeatureItem(
                icon = Icons.Default.Star,
                text = "Rating & Reviews - Rate books and write detailed reviews"
            )
            FeatureItem(
                icon = Icons.Default.Person,
                text = "User Authentication - Secure login and registration"
            )
            FeatureItem(
                icon = Icons.Default.Sync,
                text = "Background Sync - Automatic data synchronization"
            )
        }
    }
}

/**
 * Feature Item Component
 * Individual feature display with icon and description
 *
 * @param icon Icon to display
 * @param text Feature description
 */
@Composable
fun FeatureItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Technical Details Card Component
 * Shows technical implementation details
 */
@Composable
fun TechnicalDetailsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Section title
            Text(
                text = "Technical Implementation",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Technical details
            TechnicalItem("Architecture", "MVVM Pattern")
            TechnicalItem("UI Framework", "Jetpack Compose")
            TechnicalItem("Database", "Room Database")
            TechnicalItem("Navigation", "Navigation Component")
            TechnicalItem("State Management", "StateFlow & ViewModel")
            TechnicalItem("Background Tasks", "WorkManager")
            TechnicalItem("Image Loading", "Coil Library")
            TechnicalItem("Async Operations", "Kotlin Coroutines")
        }
    }
}

/**
 * Technical Item Component
 * Displays a key-value pair of technical information
 *
 * @param label Label for the technical detail
 * @param value Value of the technical detail
 */
@Composable
fun TechnicalItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}