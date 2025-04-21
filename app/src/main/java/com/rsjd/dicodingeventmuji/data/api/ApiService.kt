package com.rsjd.dicodingeventmuji.data.api

import com.rsjd.dicodingeventmuji.data.model.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int
    ): EventResponse

    @GET("events/{id}")
    suspend fun getEventDetail(
        @Path("id") id: Int
    ): EventResponse
}