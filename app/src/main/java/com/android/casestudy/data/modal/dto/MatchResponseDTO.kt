package com.android.casestudy.data.modal.dto

import com.google.gson.annotations.SerializedName

data class MatchResponseDTO(
    @SerializedName("match") val match: Int,
    @SerializedName("player1") val player1: PlayerDTO,
    @SerializedName("player2") val player2: PlayerDTO,
)

data class PlayerDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("score") val score: Int,
)
