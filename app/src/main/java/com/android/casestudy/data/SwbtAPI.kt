package com.android.casestudy.data

import com.android.casestudy.data.modal.dto.ConverterResponseDTO
import com.android.casestudy.data.modal.dto.MatchResponseDTO
import com.android.casestudy.data.modal.dto.PlayerResponseDTO
import retrofit2.http.GET

interface SwbtAPI {
    @GET("JNYL")
    suspend fun getMatchList(): List<MatchResponseDTO>

    @GET("IKQQ")
    suspend fun getPlayerList(): List<PlayerResponseDTO>

    @GET("live?access_key=7c6d2075ea7ae383217819ab133034ed")
    suspend fun getQuotes(): ConverterResponseDTO
}