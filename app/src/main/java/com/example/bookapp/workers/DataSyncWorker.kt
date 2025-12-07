package com.example.bookapp.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bookapp.BookApplication
import kotlinx.coroutines.flow.first

class DataSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val application = applicationContext as BookApplication
        val database = application.database

        return try {
            Log.d("DataSyncWorker", "Starting background sync...")

            val bookCount = database.bookDao().getAllBooks().first().size

            Log.d("DataSyncWorker", "Background sync completed successfully. Found $bookCount books.")

            Result.success()
        } catch (e: Exception) {
            Log.e("DataSyncWorker", "Error during sync: ${e.message}")
            Result.retry()
        }
    }
}
