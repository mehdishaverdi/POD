package com.pep.pod.domain.dto

data class PodInvoiceModel(
    val id :String,
    val businessName: String ,
    val billNumber: String ,
    val billAmount:String ,
    val needSettlement: Boolean,
)
