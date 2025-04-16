package com.rsjd.dicodingeventmuji.ui.event.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.data.repository.EventRepository
import com.rsjd.dicodingeventmuji.utils.EventResult
import kotlinx.coroutines.launch

class FinishedEventViewModel(private val repository: EventRepository) : ViewModel() {

    private val _events = MutableLiveData<EventResult<List<Event>>>()
    val events: LiveData<EventResult<List<Event>>> = _events

    private var currentQuery: String? = null

    fun getFinishedEvents() {
        viewModelScope.launch {
            _events.value = EventResult.Loading
            try {
                val finishedEvents = repository.getFinishedEventsFromApi()
                _events.value = EventResult.Success(finishedEvents)
            } catch (e: Exception) {
                _events.value = EventResult.Error(e.message ?: "An error occurred")
                // Try to get from local database
                repository.getFinishedEventsFromDb()
                    .collect { events ->
                        if (events.isNotEmpty()) {
                            _events.value = EventResult.Success(events)
                        }
                    }
            }
        }
    }

    fun searchEvents(query: String) {
        currentQuery = query
        viewModelScope.launch {
            _events.value = EventResult.Loading
            try {
                val searchResults = repository.searchEventsFromApi(query, active = 0)
                _events.value = EventResult.Success(searchResults)
            } catch (e: Exception) {
                _events.value = EventResult.Error(e.message ?: "An error occurred")
                // Try to search locally
                repository.searchEventsFromDb(query)
                    .collect { events ->
                        _events.value = EventResult.Success(events.filter { !it.isActive })
                    }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Clear any resources if needed
    }
}