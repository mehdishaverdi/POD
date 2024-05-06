package com.pep.pod.ui.di

import com.pep.pod.domain.repository.MMSRepository
import com.pep.pod.domain.repository.PodRepository
import com.pep.pod.domain.useCases.mmsUseCase.GetTokenUseCase
import com.pep.pod.domain.useCases.podUseCase.PodAddNeedSettlement
import com.pep.pod.domain.useCases.podUseCase.PodInvoicesUseCase
import com.pep.pod.domain.useCases.podUseCase.PodSettlementByIDUseCase
import com.pep.pod.domain.useCases.resourceUseCase.GetResourceUseCase
import com.pep.pod.domain.useCases.resourceUseCase.UCIsBusinessValid
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun mmsGetToken(mmsRepository: MMSRepository): GetTokenUseCase =
        GetTokenUseCase(mmsRepository)

    @Singleton
    @Provides
    fun provideGetResource(repository: PodRepository): GetResourceUseCase =
        GetResourceUseCase(repository)

    @Singleton
    @Provides
    fun provideInvoiceList(repository: PodRepository): PodInvoicesUseCase =
        PodInvoicesUseCase(repository)

    @Singleton
    @Provides
    fun provideUCIsBusinessValid(repository: PodRepository): UCIsBusinessValid =
        UCIsBusinessValid(repository)

    @Singleton
    @Provides
    fun provideNeedSettlement(repository: PodRepository): PodAddNeedSettlement =
        PodAddNeedSettlement(repository)

    @Singleton
    @Provides
    fun provideSettlementByIDUseCase(repository: PodRepository): PodSettlementByIDUseCase =
        PodSettlementByIDUseCase(repository)
}