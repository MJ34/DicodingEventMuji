package com.rsjd.dicodingeventmuji.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rsjd.dicodingeventmuji.data.api.ApiConfig
import com.rsjd.dicodingeventmuji.data.repository.EventRepository
import com.rsjd.dicodingeventmuji.ui.detail.DetailViewModel
import com.rsjd.dicodingeventmuji.ui.event.finished.FinishedViewModel
import com.rsjd.dicodingeventmuji.ui.event.upcoming.UpcomingViewModel

class ViewModelFactory private constructor(private val eventRepository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UpcomingViewModel::class.java) -> {
                UpcomingViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(FinishedViewModel::class.java) -> {
                FinishedViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(eventRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    EventRepository.getInstance(
                        ApiConfig.getApiService()
                    )
                ).also { instance = it }
            }
    }
}