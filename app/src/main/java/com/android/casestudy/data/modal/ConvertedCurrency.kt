package com.android.casestudy.data.modal

data class ConvertedCurrency(
    val currency: String,
    val quotes: Map<String, String>,
    val currencyInfoList: List<CurrencyInfo>)