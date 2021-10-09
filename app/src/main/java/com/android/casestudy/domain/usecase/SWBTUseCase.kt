package com.android.casestudy.domain.usecase

import com.android.casestudy.data.local.IPreferenceHelper
import com.android.casestudy.data.local.PreferenceManager
import com.android.casestudy.data.modal.ConvertedCurrency
import com.android.casestudy.data.repository.SWBTNetworkRepository
import com.android.casestudy.domain.mapper.SWBTMapper
import com.android.casestudy.states.ConverterState
import com.android.casestudy.states.UiState
import com.android.casestudy.util.Constants
import com.android.casestudy.util.DataCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
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
            //var mConverterData = DataCache.getData(DataCache.CONVERTER_DATA, null)
            val preferenceHelper: IPreferenceHelper by lazy { PreferenceManager(repository.getContext()) }
            val mCurrencyInfoList = preferenceHelper.getCurrencyInfoList()
            if (mCurrencyInfoList.isEmpty()) {
                val fetchQuotes = coroutineScope.async(start = CoroutineStart.DEFAULT) {
                    repository.getQuotes()
                }
                val result = fetchQuotes.await()
                val converterCurrency = mapper.convertQuotesResponse(result)
                preferenceHelper.setCurrencyInfoList(converterCurrency.currencyConvertedInfoList)
                preferenceHelper.setCurrencyList(converterCurrency.currencyList)
                return ConverterState.Success(converterCurrency)
            } else if(selectedCurrency == Constants.DEFAULT_CURRENCY) {
                val converterData = ConvertedCurrency(selectedCurrency)
                converterData.currencyConvertedInfoList = mCurrencyInfoList
                return ConverterState.Success(converterData)
            } else {
                val resultConverterData = ConvertedCurrency(selectedCurrency!!)
                var fromCurrencyValue = 0.0
                mCurrencyInfoList.map {
                    if (it.countryName == selectedCurrency) {
                        fromCurrencyValue = it.currencyValue
                    }
                }
                mCurrencyInfoList.map {
                    it.currencyValue = it.currencyValue / fromCurrencyValue
                }
                resultConverterData.currencyConvertedInfoList = mCurrencyInfoList
                return ConverterState.Success(resultConverterData)
            }
        }.getOrElse {
            return  ConverterState.ApiError("Error while fetching quotes")
        }
    }

}