package com.example.bookapp.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bookapp.data.local.database.BookDatabase
import kotlinx.coroutines.flow.first

/**
 * Background worker for periodic data sync
 * Runs once per day to perform cleanup or sync operations
 */
class DataSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("DataSyncWorker", "Starting background sync...")

            // Get database instance
            val database = BookDatabase.getDatabase(applicationContext)

            // Example: You could clean up old data, sync with server, etc.
            // For now, just log the operation
            val bookCount = database.bookDao().getAllBooks().first().size

            Log.d("DataSyncWorker", "Background sync completed successfully. Found $bookCount books.")

            Result.success()
        } catch (e: Exception) {
            Log.e("DataSyncWorker", "Error during sync: ${e.message}")
            Result.retry()
        }
    }
}