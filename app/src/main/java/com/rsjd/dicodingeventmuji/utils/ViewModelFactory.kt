package com.rsjd.dicodingeventmuji.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rsjd.dicodingeventmuji.data.repository.EventRepository
import com.rsjd.dicodingeventmuji.di.Injection
import com.rsjd.dicodingeventmuji.ui.detail.DetailEventViewModel
import com.rsjd.dicodingeventmuji.ui.event.active.ActiveEventViewModel
import com.rsjd.dicodingeventmuji.ui.event.finished.FinishedEventViewModel
import com.rsjd.dicodingeventmuji.ui.home.HomeViewModel
import com.rsjd.dicodingeventmuji.ui.search.SearchViewModel

class ViewModelFactory private constructor(
    private val repository: EventRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ActiveEventViewModel::class.java) -> {
                ActiveEventViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FinishedEventViewModel::class.java) -> {
                FinishedEventViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailEventViewModel::class.java) -> {
                DetailEventViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val repository = Injection.provideRepository(context)
                ViewModelFactory(repository).also {
                    INSTANCE = it
                }
            }
        }
    }
}