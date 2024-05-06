package com.pep.pod.domain.useCases.podUseCase

import androidx.paging.PagingData
import com.pep.pod.domain.dto.PodInvoiceModel
import com.pep.pod.domain.dto.PodTransaction
import com.pep.pod.domain.repository.PodRepository
import com.pep.pod.domain.useCases.base.CallableUseCase
import kotlinx.coroutines.flow.Flow

class PodSettlementByIDUseCase(private val podRepository: PodRepository) : CallableUseCase< Flow<Boolean>,Unit>() {
    override suspend fun invoke(param: Unit):Flow<Boolean> = podRepository.podSettlement()
}
