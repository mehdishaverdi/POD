package com.pep.pod.data.dto

data class ResourceMetadata(
    val businessList: List<ResourceBusiness>?,
    val deviceModel: String?
)

data class ResourceBusiness(
    val bizId: String?,
    val userId: String?,
    val name: String?,
    val icon: String?,
    val apiToken: String?,
)
