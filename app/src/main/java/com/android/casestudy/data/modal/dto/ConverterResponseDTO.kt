package com.android.casestudy.data.modal.dto

import com.google.gson.annotations.SerializedName

data class ConverterResponseDTO constructor(
    @SerializedName("success") val success: String,
    @SerializedName("source") val source: String,
    @SerializedName("quotes") val quotes: Map<String, String>,
)
