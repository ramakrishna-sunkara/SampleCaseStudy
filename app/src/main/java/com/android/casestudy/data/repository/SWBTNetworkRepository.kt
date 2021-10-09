package com.android.casestudy.data.repository

import android.content.Context
import com.android.casestudy.data.SwbtAPI
import com.android.casestudy.data.modal.dto.ConverterResponseDTO
import com.android.casestudy.data.modal.dto.MatchResponseDTO
import com.android.casestudy.data.modal.dto.PlayerResponseDTO
import javax.inject.Inject

interface SWBTNetworkRepository {
    suspend fun getSWBTPlayerList(): List<PlayerResponseDTO>
    suspend fun getSWBTMatchList(): List<MatchResponseDTO>
    suspend fun getQuotes(): ConverterResponseDTO
    fun getContext(): Context
}

class SWBTNetworkRepositoryImpl @Inject constructor(private val context: Context, private val swbtApi:SwbtAPI): SWBTNetworkRepository {
    override suspend fun getSWBTPlayerList(): List<PlayerResponseDTO> {
        return swbtApi.getPlayerList()
    }

    override suspend fun getSWBTMatchList(): List<MatchResponseDTO> {
        return swbtApi.getMatchList()
    }

    override suspend fun getQuotes(): ConverterResponseDTO {
        return swbtApi.getQuotes()
    }

    override fun getContext(): Context {
        return context
    }
}