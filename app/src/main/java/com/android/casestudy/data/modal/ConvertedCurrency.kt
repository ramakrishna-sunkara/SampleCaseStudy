package com.android.casestudy.data.modal

data class ConvertedCurrency(var currency: String) {
    var currencyList: List<String> = mutableListOf()
    var currencyConvertedInfoList: List<CurrencyConvertedInfo> = mutableListOf()
}