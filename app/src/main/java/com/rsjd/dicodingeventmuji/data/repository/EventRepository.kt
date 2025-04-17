package com.rsjd.dicodingeventmuji.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.rsjd.dicodingeventmuji.data.api.ApiService
import com.rsjd.dicodingeventmuji.data.local.database.EventDao
import com.rsjd.dicodingeventmuji.data.local.entity.EventEntity
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.utils.EventResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EventRepository(
    private val apiService: ApiService,
    private val eventDao: EventDao
) {
    private val TAG = "EventRepository"

    fun getActiveEvents(): LiveData<EventResult<List<Event>>> = liveData {
        emit(EventResult.Loading)
        try {
            val response = apiService.getActiveEvents()
            // Convert nullable list menjadi non-nullable (empty list jika null)
            val eventsList = response.events ?: emptyList()
            val events = eventsList.map { EventEntity.fromDomain(it) }
            eventDao.deleteEventsByType(isActive = true)
            eventDao.insertEvents(events)
            emit(EventResult.Success(eventsList))
        } catch (e: Exception) {
            Log.e(TAG, "Error getting active events: ${e.message}", e)
            emit(EventResult.Error(e.message ?: "An error occurred"))

            // Try to get from database if API fails
            eventDao.getActiveEvents()
                .collect { entities ->
                    val events = entities!!.map { it.toDomain() }
                    emit(EventResult.Success(events))
                }
        }
    }

    fun getFinishedEvents(): LiveData<EventResult<List<Event>>> = liveData {
        emit(EventResult.Loading)
        try {
            val response = apiService.getActiveEvents()
            // Convert nullable list menjadi non-nullable (empty list jika null)
            val eventsList = response.events ?: emptyList()
            val events = eventsList.map { EventEntity.fromDomain(it) }
            eventDao.deleteEventsByType(isActive = false)
            eventDao.insertEvents(events)
            emit(EventResult.Success(eventsList))
        } catch (e: Exception) {
            Log.e(TAG, "Error getting finished events: ${e.message}", e)
            emit(EventResult.Error(e.message ?: "An error occurred"))

            // Try to get from database if API fails
            eventDao.getActiveEvents()
                .collect { entities ->
                    val events = entities!!.map { it.toDomain() }
                    emit(EventResult.Success(events))
                }
        }
    }

    fun getEventDetail(id: String): LiveData<EventResult<Event>> = liveData {
        emit(EventResult.Loading)
        try {
            val response = apiService.getEventDetail(id)
            emit(EventResult.Success(response.event))
            // Cache the event detail
            eventDao.insertEvents(listOf(EventEntity.fromDomain(response.event)))
        } catch (e: Exception) {
            Log.e(TAG, "Error getting event detail: ${e.message}", e)
            emit(EventResult.Error(e.message ?: "An error occurred"))

            // Try to get from database if API fails
            eventDao.getEventById(id)
                .collect { entity ->
                    entity?.let {
                        emit(EventResult.Success(it.toDomain()))
                    }
                }
        }
    }

    fun getLimitedActiveEvents(limit: Int): Flow<List<Event>> {
        return eventDao.getLimitedActiveEvents(limit)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    fun getLimitedFinishedEvents(limit: Int): Flow<List<Event>> {
        return eventDao.getLimitedFinishedEvents(limit)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    fun searchEvents(query: String): LiveData<EventResult<List<Event>>> = liveData {
        emit(EventResult.Loading)
        try {
            val response = apiService.searchEvents(query = query)
            emit(EventResult.Success(response.events))
        } catch (e: Exception) {
            Log.e(TAG, "Error searching events: ${e.message}", e)
            emit(EventResult.Error(e.message ?: "An error occurred"))

            // Try to get from database if API fails
            eventDao.searchEvents(query)
                .map { entities ->
                    entities.map { it.toDomain() }
                }
                .collect { events ->
                    emit(EventResult.Success(events))
                }
        }
    }

    // API methods
    suspend fun getActiveEventsFromApi(): List<Event> {
        val response = apiService.getActiveEvents()
        return response.events
    }

    suspend fun getFinishedEventsFromApi(): List<Event> {
        val response = apiService.getFinishedEvents()
        return response.events
    }

    suspend fun getEventDetailFromApi(id: String): Event {
        val response = apiService.getEventDetail(id)
        return response.event
    }

    suspend fun searchEventsFromApi(query: String, active: Int = -1): List<Event> {
        val response = apiService.searchEvents(active, query)
        return response.events
    }

    // Database methods
    fun getActiveEventsFromDb(): Flow<List<Event>> {
        return eventDao.getActiveEvents()
            .map { entities ->
                entities!!.map { it.toDomain() }
            }
    }

    fun getFinishedEventsFromDb(): Flow<List<Event>> {
        return eventDao.getFinishedEvents()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    fun getEventDetailFromDb(id: String): Flow<Event?> {
        return eventDao.getEventById(id)
            .map { entity ->
                entity?.toDomain()
            }
    }

    fun searchEventsFromDb(query: String): Flow<List<Event>> {
        return eventDao.searchEvents(query)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    // Save event to DB
    suspend fun saveEventToDb(event: Event) {
        eventDao.insertEvents(listOf(EventEntity.fromDomain(event)))
    }

    // Clear events by type
    suspend fun clearEvents(isActive: Boolean) {
        eventDao.deleteEventsByType(isActive)
    }
}