package com.rsjd.dicodingeventmuji.di

import android.content.Context
import com.rsjd.dicodingeventmuji.data.api.ApiConfig
import com.rsjd.dicodingeventmuji.data.local.database.EventDatabase
import com.rsjd.dicodingeventmuji.data.repository.EventRepository
import com.rsjd.dicodingeventmuji.utils.ConnectivityObserver
import com.rsjd.dicodingeventmuji.utils.NetworkConnectivityObserver

/**
 * Dependency Injection container at application level
 */
object Injection {
    /**
     * Provides repository instance
     */
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        return EventRepository(apiService, dao)
    }

    /**
     * Provides ConnectivityObserver instance
     */
    fun provideConnectivityObserver(context: Context): ConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }
}