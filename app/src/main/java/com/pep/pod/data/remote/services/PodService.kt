package com.pep.pod.data.remote.services

import com.pep.pod.data.dto.PodResponse
import com.pep.pod.data.dto.response.Invoice
import com.pep.pod.data.dto.response.PodResourceResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PodService {
    @GET("acl/v2/clients/resources/{unique_id}")
    suspend fun getResourceToken(
        @Path("unique_id") uniqueId: String
    ): PodResourceResponse

    @GET("core/nzh/biz/getInvoiceListByMetadata")
    suspend fun fetchInvoice(
        @Query("metaQuery") metaQuery: String,
        @Query("offset") offset: String,
        @Query("size") size: String,
        @Query("isPayed") isPayed: Boolean,
    ): PodResponse<List<Invoice>>

    @POST("core/nzh/biz/payInvoiceByPos")
    suspend fun settlementInvoice(
        @Query("invoiceId") invoiceId: String,
        @Query("terminalNumber") terminal: String,
        @Query("merchantCode") merchant: String,
        @Query("wallet") wallet: String ,
        @Query("referenceNumber") reference: String,
        @Query("trackingNumber") sequence: String,
        @Query("transactionDate") date: String,
        @Query("inquiryNeeded") inquiryNeed: Boolean,
    ): PodResponse<Boolean>
}
