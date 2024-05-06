package com.pep.pod.domain.useCases.mmsUseCase

import com.pep.pod.domain.dto.MMSGetToken
import com.pep.pod.domain.repository.MMSRepository
import com.pep.pod.domain.useCases.base.CallableUseCase

class GetTokenUseCase(private val mmsRepository: MMSRepository): CallableUseCase<String, MMSGetToken>() {
    override suspend fun invoke(param: MMSGetToken): String =
        mmsRepository.getToken(param)

}