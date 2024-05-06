package com.pep.pod.domain.useCases.podUseCase

import androidx.paging.PagingData
import com.pep.pod.domain.dto.PodInvoiceModel
import com.pep.pod.domain.repository.PodRepository
import com.pep.pod.domain.useCases.base.CallableUseCase
import com.pep.pod.domain.useCases.base.UseCase
import kotlinx.coroutines.flow.Flow

class PodInvoicesUseCase(private val podRepository: PodRepository) : UseCase< Flow<PagingData<PodInvoiceModel>>,String>() {
    override fun invoke(param: String):Flow<PagingData<PodInvoiceModel>> =
        podRepository.getInvoices(param)
}
