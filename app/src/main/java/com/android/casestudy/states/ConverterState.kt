package com.android.casestudy.states

import com.android.casestudy.data.modal.ConvertedCurrency

sealed class ConverterState {
    object InitialLoading : ConverterState()
    data class ApiError(val errorMessage: String?) : ConverterState()
    data class Success(val convertedCurrency: ConvertedCurrency) : ConverterState()
    data class conversionSuccess(val convertedCurrency: ConvertedCurrency) : ConverterState()
}


