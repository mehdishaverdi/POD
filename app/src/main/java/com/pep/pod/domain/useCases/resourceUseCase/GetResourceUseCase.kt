package com.pep.pod.domain.useCases.resourceUseCase

import com.pep.pod.domain.repository.PodRepository
import com.pep.pod.domain.useCases.base.CallableUseCase

class GetResourceUseCase(private val podRepository: PodRepository) : CallableUseCase<Boolean, String>() {
    override suspend fun invoke(param: String): Boolean =
        podRepository.getResource(param)    //inja
}
