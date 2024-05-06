package com.pep.pod.data.utils.mms

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class MMSInterceptor@Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            return chain.proceed(
                chain
                    .request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer 1311f8de81c54fafadcaf4222659dac9.XzIwMjMx")
                    .build()
            )
        }
    }
}