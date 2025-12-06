package com.example.bookapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.work.*
import com.example.bookapp.ui.navigation.NavGraph
import com.example.bookapp.ui.theme.BookAppTheme
import com.example.bookapp.workers.DataSyncWorker
import java.util.concurrent.TimeUnit

/**
 * Main Activity - Entry point of the application
 * Sets up navigation and schedules background work
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Schedule background work
        scheduleDataSync()

        setContent {
            BookAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }

    /**
     * Schedule periodic data sync using WorkManager
     * Runs once per day
     */
    private fun scheduleDataSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "DataSync",
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
            )
    }
}