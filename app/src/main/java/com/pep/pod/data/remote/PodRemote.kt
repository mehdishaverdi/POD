package com.pep.pod.data.remote

import com.pep.pod.data.Const
import com.pep.pod.data.Const.metadataField
import com.pep.pod.data.dto.PodResponse
import com.pep.pod.data.dto.response.Invoice
import com.pep.pod.data.remote.services.PodService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PodRemote @Inject constructor(private val podService: PodService, ) {
    suspend fun fetchInvoices(
        offset: Int,
        size: Int = Const.PAGE_SIZE,
        userId: String
    ): PodResponse<List<Invoice>> =
        podService.fetchInvoice(
            offset = offset.toString(),
            size = size.toString(),
            metaQuery = "{\'field\': \'$metadataField\',\'is\':\'$userId\'}",
            isPayed = false
        ).let {
            if (it.hasError)
                throw Throwable(message = it.message)
            it
        }

    suspend fun settlementInvoice(
        invoiceId: String,
        terminal: String,
        merchant: String,
        wallet: String = "PODLAND_WALLET",
        reference: String,
        sequence: String,
        date: String,
        inquiryNeed: Boolean = false,
    ): PodResponse<Boolean> =
        podService.settlementInvoice(
            invoiceId=invoiceId,
            terminal=terminal,
            merchant = merchant,
            reference = reference,
            wallet = wallet,
            sequence = sequence,
            date = date,
            inquiryNeed = inquiryNeed
        )
}
