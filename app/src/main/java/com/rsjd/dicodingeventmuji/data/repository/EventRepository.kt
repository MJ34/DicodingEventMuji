package com.rsjd.dicodingeventmuji.data.repository

import com.rsjd.dicodingeventmuji.data.api.ApiService
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class EventRepository(private val apiService: ApiService) {

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
                }
                else {
                    emit(Resource.Error("Event tidak ditemukan"))
                }
            } else {
                emit(Resource.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Terjadi kesalahan"))
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(apiService: ApiService): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService).also { instance = it }
            }
    }
}