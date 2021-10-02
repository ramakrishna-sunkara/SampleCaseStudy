package com.android.casestudy.data.remote

import com.android.casestudy.states.UiState
import javax.inject.Inject

class NetworkStateHandler @Inject constructor() {

    suspend inline fun fetchAPIResponse(
        crossinline onSuccess: suspend () -> UiState,
        noinline onError: (throwable: Throwable) -> UiState = ::onErrorNetwork
    ): UiState =
        runCatching {
            onSuccess.invoke()
        }.getOrElse { throwable ->
            return onError.invoke(throwable)
        }

    fun onErrorNetwork(error: Throwable) = UiState.ApiError(error.message)
}
