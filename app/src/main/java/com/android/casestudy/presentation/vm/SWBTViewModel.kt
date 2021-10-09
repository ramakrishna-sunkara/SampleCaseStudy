package com.android.casestudy.presentation.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.casestudy.data.local.IPreferenceHelper
import com.android.casestudy.data.local.PreferenceManager
import com.android.casestudy.domain.usecase.SWBTUseCase
import com.android.casestudy.states.ConverterState
import com.android.casestudy.states.UiState
import kotlinx.coroutines.launch

class SWBTViewModel @ViewModelInject internal constructor(
    private val useCase: SWBTUseCase
) : ViewModel(), SWBTViewModelContract {

    private val _uiStates by lazy { MutableLiveData<UiState>() }
    private val _selectedCurrency = MutableLiveData<String>()
    private val _currencies = MutableLiveData<ArrayList<String>>()
    private val _converterState by lazy { MutableLiveData<ConverterState>() }

    val uiState: LiveData<UiState>
        get() = _uiStates

    val selectedCurrency: LiveData<String>
        get() = _selectedCurrency

    val currencies: LiveData<ArrayList<String>>
        get() = _currencies

    val converterState: LiveData<ConverterState>
        get() = _converterState

    override fun getStarWarsTournamentDetails() {
        viewModelScope.launch {
            _uiStates.value = UiState.InitialLoading
            _uiStates.value = useCase.fetchSWBTTournamentDetails(this)
        }
    }

    override fun loadConvertCurrencyData() {
        viewModelScope.launch {
            _converterState.value = ConverterState.InitialLoading
            _converterState.value = useCase.fetchQuotes(this, selectedCurrency.value)
        }
    }

    override fun navigateToPlayerResultScreen() {
        TODO("Not yet implemented")
    }

    fun setSelectedCurrency(item: String?) {
        _selectedCurrency.value = item
    }

    fun setCurrencies(item: ArrayList<String>) {
        _currencies.value = item
    }

}
