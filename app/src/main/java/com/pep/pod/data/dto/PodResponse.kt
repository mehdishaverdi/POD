package com.pep.pod.data.dto

data class PodResponse<out T : Any>(
    val hasError: Boolean,
    val messageId: Int,
    val errorCode: Int,
    val count: Int,
    val ott: String,
    val referenceNumber: String,
    val message: String?,
    val result: T
)
