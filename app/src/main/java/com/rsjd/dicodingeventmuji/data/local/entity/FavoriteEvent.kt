package com.rsjd.dicodingeventmuji.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEvent(
    @PrimaryKey
    val id: Int,
    val name: String,
    val summary: String? = null,
    val description: String? = null,
    val imageLogo: String? = null,
    val mediaCover: String? = null,
    val category: String? = null,
    val ownerName: String? = null,
    val cityName: String? = null,
    val quota: Int = 0,
    val registrants: Int = 0,
    val beginTime: String? = null,
    val endTime: String? = null,
    val link: String? = null
)