package com.rsjd.dicodingeventmuji.ui.event.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.data.repository.EventRepository
import com.rsjd.dicodingeventmuji.utils.Resource
import kotlinx.coroutines.launch

class FinishedViewModel(private val repository: EventRepository) : ViewModel() {

    private val _events = MutableLiveData<Resource<List<Event>>>()
    val events: LiveData<Resource<List<Event>>> = _events

    init {
        fetchFinishedEvents()
    }

    fun fetchFinishedEvents() {
        viewModelScope.launch {
            repository.getFinishedEvents().collect { result ->
                _events.value = result
            }
        }
    }
}