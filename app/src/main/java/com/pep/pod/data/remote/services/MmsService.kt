package com.pep.pod.data.remote.services

import com.pep.pod.data.repository.MMSResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface MmsService {
    @POST("api/v1/ProductDetail/UpdateTokenKey")
    suspend fun getToken(
        @Query("UserName") UserName: String,
        @Query("Password") Password: String,
    ): MMSResponse
}