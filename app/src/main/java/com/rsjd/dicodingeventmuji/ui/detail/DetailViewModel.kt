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

    fun getEventDetail(id: Int) {
        viewModelScope.launch {
            repository.getEventDetail(id).collect { result ->
                _event.value = result
            }
        }
    }
}