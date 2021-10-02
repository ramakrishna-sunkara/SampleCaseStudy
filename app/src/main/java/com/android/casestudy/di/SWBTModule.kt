package com.android.casestudy.di

import com.android.casestudy.data.modal.PlayersInfo
import com.android.casestudy.data.modal.dto.MatchResponseDTO
import com.android.casestudy.data.modal.dto.PlayerResponseDTO
import com.android.casestudy.data.repository.SWBTNetworkRepository
import com.android.casestudy.data.repository.SWBTNetworkRepositoryImpl
import com.android.casestudy.domain.mapper.SWBTMapper
import com.android.casestudy.domain.usecase.SWBTUseCase
import com.android.casestudy.domain.usecase.SWBTUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
abstract class CurrencyModule {

    @Binds
    abstract fun bindRepository(impl: SWBTNetworkRepositoryImpl):
        SWBTNetworkRepository

    @Binds
    abstract fun bindUsecase(impl: SWBTUseCaseImpl): SWBTUseCase

}

@InstallIn(ActivityComponent::class)
@Module
object CurrencyProviderModule {

    @Provides
    @JvmStatic
    fun bindDTOtoUiStateMapper():
        Function2<List<PlayerResponseDTO>, List<MatchResponseDTO>, List<PlayersInfo?>> =
        SWBTMapper()
}