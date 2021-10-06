package com.android.casestudy.util

import java.util.*

object DataCache {
    const val CURRENCY_LIST = "CurrencyList"
    const val CONVERTER_DATA = "ConverterData"
    private val dataCache = HashMap<String, Any?>()
    fun putData(key: String, data: Any?) {
        dataCache[key] = data
    }

    fun getData(key: String): Any? {
        return getData(key, null)
    }

    fun getData(key: String, default: Any?): Any? {
        return if (dataCache.containsKey(key)) {
            dataCache[key]
        } else {
            default
        }
    }
}