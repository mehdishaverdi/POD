package com.pep.pod.domain.dto

data class PodTransaction(
    val invoiceId:String,
    val terminalNum:String,
    val merchant:String,
    val reference: String,
    val sequence:String,
    val date:String,
)
