package com.example.bookapp.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit,
    onAccountClick: () -> Unit,
    onPrivacyClick: () -> Unit
) {
    val theme by viewModel.theme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SettingItem(icon = Icons.Default.Settings, text = "Theme: $theme") {
                val nextTheme = when (theme) {
                    "Light" -> "Dark"
                    "Dark" -> "System"
                    else -> "Light"
                }
                viewModel.setTheme(nextTheme)
            }
            SettingItem(icon = Icons.Default.Person, text = "Account", onClick = onAccountClick)
            SettingItem(icon = Icons.Default.Lock, text = "Privacy", onClick = onPrivacyClick)
        }
    }
}

@Composable
private fun SettingItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke() }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(30.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}
