package com.android.casestudy.data.modal

data class ConvertedCurrency(
    var currency: String,
    val quotes: Map<String, String>,
    var currencyInfoList: List<CurrencyInfo>)