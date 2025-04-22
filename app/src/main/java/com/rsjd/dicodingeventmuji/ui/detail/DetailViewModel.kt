package com.rsjd.dicodingeventmuji.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.data.repository.EventRepository
import com.rsjd.dicodingeventmuji.utils.Resource
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: EventRepository) : ViewModel() {

    private val _event = MutableLiveData<Resource<Event>>()
    val event: LiveData<Resource<Event>> = _event

    private val _favoriteStatus = MutableLiveData<Resource<Boolean>>()
    val favoriteStatus: LiveData<Resource<Boolean>> = _favoriteStatus

    fun getEventDetail(id: Int) {
        viewModelScope.launch {
            repository.getEventDetail(id).collect { result ->
                _event.value = result
            }
        }
    }

    fun isFavorite(id: Int): LiveData<Boolean> = repository.isFavorite(id)

    fun addToFavorite(event: Event) {
        viewModelScope.launch {
            try {
                repository.addToFavorite(event)
                _favoriteStatus.value = Resource.Success(true)
            } catch (e: Exception) {
                _favoriteStatus.value = Resource.Error("Failed to add to favorites")
            }
        }
    }

    fun removeFromFavorite(eventId: Int) {
        viewModelScope.launch {
            try {
                val result = repository.removeFromFavorite(eventId)
                _favoriteStatus.value = if (result) {
                    Resource.Success(false)
                } else {
                    Resource.Error("Gagal menghapus dari favorit")
                }
            } catch (e: Exception) {
                _favoriteStatus.value = Resource.Error("Error: ${e.message}")
            }
        }
    }
}