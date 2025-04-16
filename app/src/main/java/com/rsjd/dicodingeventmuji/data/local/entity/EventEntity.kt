package com.rsjd.dicodingeventmuji.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rsjd.dicodingeventmuji.data.model.Event

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val ownerName: String,
    val beginTime: String,
    val quota: Int,
    val registrant: Int,
    val description: String,
    val link: String,
    val imageLogo: String? = null,
    val mediaCover: String? = null,
    val isActive: Boolean
) {
    // Converter dari Entity ke Model Domain
    fun toDomain(): Event {
        return Event(
            id = id,
            name = name,
            ownerName = ownerName,
            beginTime = beginTime,
            quota = quota,
            registrant = registrant,
            description = description,
            link = link,
            imageLogo = imageLogo,
            mediaCover = mediaCover,
            isActive = isActive
        )
    }

    companion object {
        // Converter dari Model Domain ke Entity
        fun fromDomain(event: Event): EventEntity {
            return EventEntity(
                id = event.id,
                name = event.name,
                ownerName = event.ownerName,
                beginTime = event.beginTime,
                quota = event.quota,
                registrant = event.registrant,
                description = event.description,
                link = event.link,
                imageLogo = event.imageLogo,
                mediaCover = event.mediaCover,
                isActive = event.isActive
            )
        }
    }
}