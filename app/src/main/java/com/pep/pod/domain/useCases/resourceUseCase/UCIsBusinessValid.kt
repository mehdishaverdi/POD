package com.pep.pod.domain.useCases.resourceUseCase

import com.pep.pod.domain.dto.Business
import com.pep.pod.domain.repository.PodRepository
import com.pep.pod.domain.useCases.base.CallableUseCase
import kotlinx.coroutines.flow.Flow

class UCIsBusinessValid(private val podRepository: PodRepository) : CallableUseCase< Flow<Business>,Unit>() {
    override suspend fun invoke(param: Unit):Flow<Business> = podRepository.isBusinessValid()
}
