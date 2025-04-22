package com.rsjd.dicodingeventmuji.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.rsjd.dicodingeventmuji.data.api.ApiService
import com.rsjd.dicodingeventmuji.data.local.dao.FavoriteEventDao
import com.rsjd.dicodingeventmuji.data.local.entity.FavoriteEvent
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class EventRepository(
    private val apiService: ApiService,
    private val favoriteEventDao: FavoriteEventDao
) {

    fun getUpcomingEvents(): Flow<Resource<List<Event>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getEvents(active = 1)
            if (!response.error) {
                emit(Resource.Success(response.listEvents))
            } else {
                emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Terjadi kesalahan"))
        }
    }.flowOn(Dispatchers.IO)

    fun getFinishedEvents(): Flow<Resource<List<Event>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getEvents(active = 0)
            if (!response.error) {
                emit(Resource.Success(response.listEvents))
            } else {
                emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Terjadi kesalahan"))
        }
    }.flowOn(Dispatchers.IO)

    fun getEventDetail(id: Int): Flow<Resource<Event>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getEventDetail(id)
            if (!response.error) {
                // Cek apakah respons memiliki field 'event'
                if (response.event != null) {
                    emit(Resource.Success(response.event))
                }
                // Cek apakah respons memiliki listEvents yang tidak kosong
                else if (response.listEvents.isNotEmpty()) {
                    emit(Resource.Success(response.listEvents[0]))
                } else {
                    emit(Resource.Error("Event tidak ditemukan"))
                }
            } else {
                emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Terjadi kesalahan"))
        }
    }.flowOn(Dispatchers.IO)

    // Favorite Event Functions
    fun getAllFavorites(): LiveData<List<FavoriteEvent>> = favoriteEventDao.getAllFavorites()

    fun isFavorite(id: Int): LiveData<Boolean> = favoriteEventDao.isFavorite(id)

    suspend fun addToFavorite(event: Event) {
        val favoriteEvent = FavoriteEvent(
            id = event.id,
            name = event.name,
            summary = event.summary,
            description = event.description,
            imageLogo = event.imageLogo,
            mediaCover = event.mediaCover,
            category = event.category,
            ownerName = event.ownerName,
            cityName = event.cityName,
            quota = event.quota,
            registrants = event.registrants,
            beginTime = event.beginTime,
            endTime = event.endTime,
            link = event.link
        )
        favoriteEventDao.insertFavorite(favoriteEvent)
    }

    suspend fun removeFromFavorite(eventId: Int): Boolean {
        return try {
            // Gunakan metode query langsung
            val favoriteEvent = favoriteEventDao.getFavoriteByIdDirect(eventId)
            if (favoriteEvent != null) {
                val deletedRows = favoriteEventDao.deleteFavorite(favoriteEvent)
                deletedRows > 0
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("EventRepository", "Error removing favorite", e)
            false
        }
    }

    suspend fun removeFromFavorite(favoriteEvent: FavoriteEvent) {
        favoriteEventDao.deleteFavorite(favoriteEvent)
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(
            apiService: ApiService,
            favoriteEventDao: FavoriteEventDao
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, favoriteEventDao).also { instance = it }
            }
    }
}