package com.android.casestudy.states

import com.android.casestudy.data.modal.PlayersInfo

sealed class UiState {

    object InitialLoading : UiState()
    data class ApiError(val errorMessage:String?): UiState()
    data class Success(val response : List<PlayersInfo?>) : UiState()

}


