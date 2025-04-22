package com.rsjd.dicodingeventmuji.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsjd.dicodingeventmuji.data.local.entity.FavoriteEvent
import com.rsjd.dicodingeventmuji.data.repository.EventRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: EventRepository) : ViewModel() {

    val favoriteEvents: LiveData<List<FavoriteEvent>> = repository.getAllFavorites()

    fun removeFromFavorite(favoriteEvent: FavoriteEvent) {
        viewModelScope.launch {
            repository.removeFromFavorite(favoriteEvent)
        }
    }
}