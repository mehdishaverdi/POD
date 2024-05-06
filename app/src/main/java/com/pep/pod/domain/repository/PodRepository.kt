package com.pep.pod.domain.repository

import androidx.paging.PagingData
import com.pep.pod.domain.dto.Business
import com.pep.pod.domain.dto.PodInvoiceModel
import com.pep.pod.domain.dto.PodNeedSettlement
import kotlinx.coroutines.flow.Flow

interface PodRepository {
    suspend fun getResource(serial: String): Boolean
    fun getInvoices(id: String): Flow<PagingData<PodInvoiceModel>>
    fun isBusinessValid(): Flow<Business>
    fun insertNeedSettlement(receipt: PodNeedSettlement):Flow<Unit>
    fun podSettlement():Flow<Boolean>
}