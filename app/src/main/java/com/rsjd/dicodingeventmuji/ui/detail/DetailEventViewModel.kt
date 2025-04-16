package com.rsjd.dicodingeventmuji.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.data.repository.EventRepository
import com.rsjd.dicodingeventmuji.utils.EventResult
import kotlinx.coroutines.launch

class DetailEventViewModel(private val repository: EventRepository) : ViewModel() {

    private val _event = MutableLiveData<EventResult<Event>>()
    val event: LiveData<EventResult<Event>> = _event

    fun getEventDetail(id: String) {
        viewModelScope.launch {
            _event.value = EventResult.Loading
            try {
                val response = repository.getEventDetailFromApi(id)
                _event.value = EventResult.Success(response)

                // Cache the event detail if needed
                // repository.saveEventToDb(response)
            } catch (e: Exception) {
                _event.value = EventResult.Error(e.message ?: "An error occurred")

                // Try to get from local database
                repository.getEventDetailFromDb(id)
                    .collect { event ->
                        event?.let {
                            _event.value = EventResult.Success(it)
                        }
                    }
            }
        }
    }
}