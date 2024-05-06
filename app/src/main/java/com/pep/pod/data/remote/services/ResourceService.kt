package com.pep.pod.data.remote.services

import com.pep.pod.data.dto.response.PodResourceResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ResourceService {
    @GET("acl/v2/clients/resources/{unique_id}")
    suspend fun getResource(
        @Path("unique_id") uniqueId: String
    ): PodResourceResponse
}
