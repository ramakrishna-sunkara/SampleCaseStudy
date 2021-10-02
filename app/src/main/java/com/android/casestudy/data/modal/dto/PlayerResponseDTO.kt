package com.android.casestudy.data.modal.dto

import com.google.gson.annotations.SerializedName

data class PlayerResponseDTO constructor(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("icon") val icon: String,
)
