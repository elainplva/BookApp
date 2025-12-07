package com.example.bookapp.ui.screens.account

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    onBackClick: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (currentUser != null) {
                // Username Field
                AccountField(
                    icon = Icons.Outlined.Person,
                    label = "Username",
                    value = currentUser!!.username
                )

                // Email Field
                AccountField(
                    icon = Icons.Outlined.Email,
                    label = "Email",
                    value = currentUser!!.email
                )

                // Password Field
                PasswordField(
                    password = currentUser!!.password,
                    passwordVisible = passwordVisible,
                    onToggleVisibility = { passwordVisible = !passwordVisible }
                )
            } else {
                Text(
                    text = "No user logged in",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

/**
 * Account Field - Simple card with icon, label and value
 */
@Composable
private fun AccountField(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )

            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Password Field - Card with visibility toggle
 */
@Composable
private fun PasswordField(
    password: String,
    passwordVisible: Boolean,
    onToggleVisibility: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = "Password",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Password",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (passwordVisible) password else "••••••••",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (passwordVisible) {
                        Icons.Outlined.Visibility
                    } else {
                        Icons.Outlined.VisibilityOff
                    },
                    contentDescription = if (passwordVisible) "Hide" else "Show",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}