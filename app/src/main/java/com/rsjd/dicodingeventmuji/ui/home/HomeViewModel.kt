package com.rsjd.dicodingeventmuji.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.data.repository.EventRepository
import com.rsjd.dicodingeventmuji.utils.EventResult
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: EventRepository) : ViewModel() {

    private val _activeEvents = MutableLiveData<EventResult<List<Event>>>()
    val activeEvents: LiveData<EventResult<List<Event>>> = _activeEvents

    private val _finishedEvents = MutableLiveData<EventResult<List<Event>>>()
    val finishedEvents: LiveData<EventResult<List<Event>>> = _finishedEvents

    fun getActiveEvents() {
        viewModelScope.launch {
            _activeEvents.value = EventResult.Loading
            try {
                val response = repository.getActiveEventsFromApi()
                _activeEvents.value = EventResult.Success(response)
            } catch (e: Exception) {
                _activeEvents.value = EventResult.Error(e.message ?: "An error occurred")
                // Try to get from local database
                repository.getLimitedActiveEvents(5)
                    .collect { events ->
                        if (events.isNotEmpty()) {
                            _activeEvents.value = EventResult.Success(events)
                        }
                    }
            }
        }
    }

    fun getFinishedEvents() {
        viewModelScope.launch {
            _finishedEvents.value = EventResult.Loading
            try {
                val response = repository.getFinishedEventsFromApi()
                _finishedEvents.value = EventResult.Success(response)
            } catch (e: Exception) {
                _finishedEvents.value = EventResult.Error(e.message ?: "An error occurred")
                // Try to get from local database
                repository.getLimitedFinishedEvents(5)
                    .collect { events ->
                        if (events.isNotEmpty()) {
                            _finishedEvents.value = EventResult.Success(events)
                        }
                    }
            }
        }
    }
}