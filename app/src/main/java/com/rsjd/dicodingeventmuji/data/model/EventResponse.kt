package com.rsjd.dicodingeventmuji.data.model

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("listEvents")
    val listEvents: List<Event> = emptyList(),

    @field:SerializedName("event")
    val event: Event? = null
)