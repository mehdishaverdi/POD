package com.pep.pod.domain.useCases.podUseCase

import com.pep.pod.domain.dto.PodNeedSettlement
import com.pep.pod.domain.repository.PodRepository
import com.pep.pod.domain.useCases.base.CallableUseCase
import kotlinx.coroutines.flow.Flow

class PodAddNeedSettlement(private val podRepository: PodRepository): CallableUseCase< Flow<Unit>, PodNeedSettlement>() {
    override suspend fun invoke(param: PodNeedSettlement):Flow<Unit> =
        podRepository.insertNeedSettlement(param)
}
