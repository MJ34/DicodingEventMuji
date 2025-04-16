package com.rsjd.dicodingeventmuji.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.data.repository.EventRepository
import com.rsjd.dicodingeventmuji.utils.EventResult
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: EventRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<EventResult<List<Event>>>()
    val searchResults: LiveData<EventResult<List<Event>>> = _searchResults

    private var currentQuery: String? = null

    fun searchEvents(query: String) {
        if (query == currentQuery) return
        currentQuery = query

        viewModelScope.launch {
            _searchResults.value = EventResult.Loading
            try {
                val results = repository.searchEventsFromApi(query)
                _searchResults.value = EventResult.Success(results)
            } catch (e: Exception) {
                _searchResults.value = EventResult.Error(e.message ?: "An error occurred")
                // Try to search locally
                repository.searchEventsFromDb(query)
                    .collect { events ->
                        _searchResults.value = EventResult.Success(events)
                    }
            }
        }
    }
}