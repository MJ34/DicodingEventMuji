package com.rsjd.dicodingeventmuji.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("ownerName")
    val ownerName: String,

    @SerializedName("beginTime")
    val beginTime: String,

    @SerializedName("quota")
    val quota: Int,

    @SerializedName("registrant")
    val registrant: Int,

    @SerializedName("description")
    val description: String,

    @SerializedName("link")
    val link: String,

    @SerializedName("imageLogo")
    val imageLogo: String? = null,

    @SerializedName("mediaCover")
    val mediaCover: String? = null,

    @SerializedName("active")
    val isActive: Boolean
) : Parcelable