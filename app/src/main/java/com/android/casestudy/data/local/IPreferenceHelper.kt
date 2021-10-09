package com.android.casestudy.data.local

import com.android.casestudy.data.modal.CurrencyConvertedInfo

interface IPreferenceHelper {

    fun setCurrencyInfoList(currencyConvertedInfoList: List<CurrencyConvertedInfo>)

    fun getCurrencyInfoList(): List<CurrencyConvertedInfo>

    fun setCurrencyList(currencyList: List<String>)

    fun getCurrencyList(): List<String>

    fun clearPrefs()

}