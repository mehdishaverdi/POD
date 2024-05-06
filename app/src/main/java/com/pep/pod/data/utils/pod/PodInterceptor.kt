package com.pep.pod.data.utils.pod

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class PodInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            return chain.proceed(
                chain.request().newBuilder()
//                    .addHeader("_token_", "fdb0863ab4e74c069e7dfc4cfb23358c.XzIwMjIxMQ")
//                    .addHeader("_token_", "1311f8de81c54fafadcaf4222659dac9.XzIwMjMx")
                    .addHeader("_token_", "21306818ebce437caf74aced700b06a0.XzIwMjM5")
                    .addHeader("_token_issuer_", "1").build()
            )
        }
    }
}