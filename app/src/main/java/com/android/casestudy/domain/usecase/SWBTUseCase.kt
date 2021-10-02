package com.android.casestudy.domain.usecase

import com.android.casestudy.data.repository.SWBTNetworkRepository
import com.android.casestudy.domain.mapper.SWBTMapper
import com.android.casestudy.states.ConverterState
import com.android.casestudy.states.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import javax.inject.Inject

interface  SWBTUseCase {
    suspend fun fetchSWBTTournamentDetails(coroutineScope: CoroutineScope,): UiState
    suspend fun fetchQuotes(coroutineScope: CoroutineScope,): ConverterState
}

class SWBTUseCaseImpl  @Inject constructor(private val repository: SWBTNetworkRepository,
private val mapper:SWBTMapper): SWBTUseCase {
    override suspend fun fetchSWBTTournamentDetails(coroutineScope: CoroutineScope): UiState {
        runCatching {
            val playersDeferred = coroutineScope.async(start = CoroutineStart.DEFAULT) {
                repository.getSWBTPlayerList()
            }

            val matchesDeferred = coroutineScope.async(start = CoroutineStart.DEFAULT) {
                repository.getSWBTMatchList()
            }

            val result1 = playersDeferred.await()
            val result2 = matchesDeferred.await()

            return UiState.Success(mapper.invoke(result1,result2))

        }.getOrElse {
          return  UiState.ApiError("Error while fetching the data")
        }
    }

    override suspend fun fetchQuotes(coroutineScope: CoroutineScope): ConverterState {
        runCatching {
            val fetchQuotes = coroutineScope.async(start = CoroutineStart.DEFAULT) {
                repository.getQuotes()
            }
            val result = fetchQuotes.await()
            return ConverterState.Success(mapper.convertQuotesResponse(result))

        }.getOrElse {
            return  ConverterState.ApiError("Error while fetching quotes")
        }
    }

}