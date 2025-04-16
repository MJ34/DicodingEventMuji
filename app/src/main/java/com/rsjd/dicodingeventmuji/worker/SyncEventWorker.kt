package com.rsjd.dicodingeventmuji.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rsjd.dicodingeventmuji.data.api.ApiConfig
import com.rsjd.dicodingeventmuji.data.local.database.EventDatabase
import com.rsjd.dicodingeventmuji.data.local.entity.EventEntity
import com.rsjd.dicodingeventmuji.data.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Worker class for synchronizing events data in background
 */
class SyncEventWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Create repository instance
            val apiService = ApiConfig.getApiService()
            val database = EventDatabase.getInstance(applicationContext)
            val dao = database.eventDao()
            val repository = EventRepository(apiService, dao)

            // Sync active events
            val activeEvents = repository.getActiveEventsFromApi()
            val activeEventEntities = activeEvents.map { EventEntity.fromDomain(it) }
            dao.deleteEventsByType(isActive = true)
            dao.insertEvents(activeEventEntities)

            // Sync finished events
            val finishedEvents = repository.getFinishedEventsFromApi()
            val finishedEventEntities = finishedEvents.map { EventEntity.fromDomain(it) }
            dao.deleteEventsByType(isActive = false)
            dao.insertEvents(finishedEventEntities)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "com.example.eventapp.worker.SyncEventWorker"
    }
}