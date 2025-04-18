package com.rsjd.dicodingeventmuji.data.api

import com.rsjd.dicodingeventmuji.data.model.EventDetailResponse
import com.rsjd.dicodingeventmuji.data.model.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getActiveEvents(
        @Query("active") active: Int = 1
    ): EventResponse

    @GET("events")
    suspend fun getFinishedEvents(
        @Query("active") active: Int = 0
    ): EventResponse

    @GET("events/{id}")
    suspend fun getEventDetail(
        @Path("id") id: String
    ): EventDetailResponse

    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Int = -1,
        @Query("q") query: String
    ): EventResponse
}