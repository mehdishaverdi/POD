package com.pep.pod.domain.dto

data class PodNeedSettlement(
    val id: Long,
    val terminal: String,
    val merchant: String,
    val reference: String,
    val sequence: String,
    val date: String,
)
