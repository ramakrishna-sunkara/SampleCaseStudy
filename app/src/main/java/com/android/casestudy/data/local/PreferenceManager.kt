package com.android.casestudy.data.local

import android.content.Context
import android.content.SharedPreferences
import com.android.casestudy.data.modal.CurrencyConvertedInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class PreferenceManager constructor(context: Context) : IPreferenceHelper {
    private val CC_PREFERENCE = "CCPreference"
    private var preferences: SharedPreferences

    init {
        preferences = context.getSharedPreferences(CC_PREFERENCE, Context.MODE_PRIVATE)
    }

    override fun setCurrencyInfoList(currencyConvertedInfoList: List<CurrencyConvertedInfo>) {
        val gson = Gson()
        val strCurrencyConverted = gson.toJson(currencyConvertedInfoList)
        preferences[CURRENCY_CONVERTED_INFO] = strCurrencyConverted
    }

    override fun getCurrencyInfoList(): List<CurrencyConvertedInfo> {
        val info = preferences[CURRENCY_CONVERTED_INFO] ?: ""
        return if (info.isEmpty())
            mutableListOf()
        else {
            val gson = Gson()
            val itemType = object : TypeToken<List<CurrencyConvertedInfo>>() {}.type
            gson.fromJson(info, itemType)
        }
    }

    override fun setCurrencyList(currencyList: List<String>) {
        val gson = Gson()
        val strCurrencyList = gson.toJson(currencyList)
        preferences[CURRENCY_LIST] = strCurrencyList
    }

    override fun getCurrencyList(): List<String> {
        val info = preferences[CURRENCY_LIST] ?: ""
        return if (info.isEmpty())
            mutableListOf()
        else {
            val gson = Gson()
            val itemType = object : TypeToken<List<String>>() {}.type
            gson.fromJson(info, itemType)
        }
    }

    override fun clearPrefs() {
        preferences.edit().clear().apply()
    }

    companion object {
        const val CURRENCY_CONVERTED_INFO = "currency_converted_info"
        const val CURRENCY_LIST = "currency_list"
    }
}

/**
 * SharedPreferences extension function, to listen the edit() and apply() fun calls
 * on every SharedPreferences operation.
 */
private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}

/**
 * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
 */
private operator fun SharedPreferences.set(key: String, value: Any?) {
    when (value) {
        is String? -> edit { it.putString(key, value) }
        is Int -> edit { it.putInt(key, value) }
        is Boolean -> edit { it.putBoolean(key, value) }
        is Float -> edit { it.putFloat(key, value) }
        is Long -> edit { it.putLong(key, value) }
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}

/**
 * finds value on given key.
 * [T] is the type of value
 * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
 */
private inline operator fun <reified T : Any> SharedPreferences.get(
    key: String,
    defaultValue: T? = null
): T? {
    return when (T::class) {
        String::class -> getString(key, defaultValue as? String) as T?
        Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
        Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
        Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
        Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}