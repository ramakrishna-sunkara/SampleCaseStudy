package com.android.casestudy.domain.usecase

import com.android.casestudy.data.modal.ConvertedCurrency
import com.android.casestudy.data.modal.CurrencyInfo
import com.android.casestudy.data.repository.SWBTNetworkRepository
import com.android.casestudy.domain.mapper.SWBTMapper
import com.android.casestudy.states.ConverterState
import com.android.casestudy.states.UiState
import com.android.casestudy.util.DataCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import java.math.BigDecimal
import javax.inject.Inject

interface  SWBTUseCase {
    suspend fun fetchSWBTTournamentDetails(coroutineScope: CoroutineScope,): UiState
    suspend fun fetchQuotes(coroutineScope: CoroutineScope, selectedCurrency: String?,): ConverterState
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

    override suspend fun fetchQuotes(coroutineScope: CoroutineScope, selectedCurrency: String?): ConverterState {
        runCatching {
            val converterData = DataCache.getData(DataCache.CONVERTER_DATA);
            if (null == converterData) {
                val fetchQuotes = coroutineScope.async(start = CoroutineStart.DEFAULT) {
                    repository.getQuotes()
                }
                val result = fetchQuotes.await()
                val converterCurrency = mapper.convertQuotesResponse(result)
                val currencyList: ArrayList<String> = converterCurrency.quotes.entries.map {
                    it.key.replace(result.source, "")
                }.toList() as ArrayList<String>
                currencyList.remove("")
                currencyList.add(0, result.source)
                DataCache.putData(DataCache.CURRENCY_LIST, currencyList)
                DataCache.putData(DataCache.CONVERTER_DATA, converterCurrency)
                return ConverterState.Success(converterCurrency)
            }else {
                var mConverterData = converterData as ConvertedCurrency
                var fromCurrencyValue = 0.0
                mConverterData.currencyInfoList.map {
                    if (it.countryName == selectedCurrency) {
                        //selectedCurrencyInfo = it
                        fromCurrencyValue = it.currencyValue
                    }
                }
                mConverterData.currencyInfoList.map {
                        it.currencyValue = it.currencyValue/fromCurrencyValue
                }
                selectedCurrency?.let {
                    mConverterData.currency = it
                }
                return ConverterState.Success(mConverterData)
            }
        }.getOrElse {
            return  ConverterState.ApiError("Error while fetching quotes")
        }
    }

}