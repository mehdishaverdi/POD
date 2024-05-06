package com.pep.pod.data.di

import com.pep.pod.data.Const
import com.pep.pod.data.remote.services.MmsService
import com.pep.pod.data.utils.json.SingleToArrayAdapter
import com.pep.pod.data.utils.mms.MMSInterceptor
import com.pep.pod.data.utils.network.NetworkResponseAdapterFactory
import com.pep.pod.data.utils.pod.PodInterceptor
import com.pep.pod.data.utils.pod.ResourceInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object ServiceRequirementModule {
    const val POD_AUTH = "POD_AUTH"
    const val RESOURCE_AUTH = "RESOURCE_AUTH"
    const val MMS_AUTH = "MMS_AUTH"

    @Provides
    @Named(POD_AUTH)
    fun retrofitNoAuth(@Named(POD_AUTH) podHttpClient: OkHttpClient, generalRetrofit: Builder): Retrofit =
        generalRetrofit.client(podHttpClient).build()

    @Provides
    @Named(RESOURCE_AUTH)
    fun retrofitRESOURCEAuth(@Named(RESOURCE_AUTH) httpClient: OkHttpClient, generalRetrofit: Builder): Retrofit =
        generalRetrofit.client(httpClient).build()

    @Provides
    @Named(MMS_AUTH)
    fun retrofitMMSAuth(@Named(MMS_AUTH) httpClient: OkHttpClient, generalRetrofit: Builder): Retrofit =
        generalRetrofit.client(httpClient).build()

    @Provides
    fun retrofitGeneral(moshiConverterFactory: MoshiConverterFactory, networkResponseAdapterFactory: NetworkResponseAdapterFactory, ): Builder =
        Builder().baseUrl(Const.BASE_URL).addConverterFactory(moshiConverterFactory).addCallAdapterFactory(networkResponseAdapterFactory)

    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    fun interceptor(): Interceptor =
        Interceptor { chain ->
            val request = chain
                .request()
                .newBuilder()
                .header("User-Agent", "POD-Android-Application")
                .build()
            chain.proceed(request)
        }

    @Provides
    fun kotlinJsonAdapterFactory(): KotlinJsonAdapterFactory =
        KotlinJsonAdapterFactory()

    @Provides
    fun moshi(kotlinJsonAdapterFactory: KotlinJsonAdapterFactory): Moshi = Moshi.Builder()
        .add(kotlinJsonAdapterFactory)
        .add(SingleToArrayAdapter.INSTANCE)
        .build()

    @Provides
    fun moshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Named(POD_AUTH)
    fun httpClientPod(generalOkHttpClient: OkHttpClient.Builder, podInterceptor: PodInterceptor, ): OkHttpClient =
        generalOkHttpClient
            .addInterceptor(podInterceptor)
            .build()

    @Provides
    @Named(RESOURCE_AUTH)
    fun resourceHttpClient(generalOkHttpClient: OkHttpClient.Builder, resourceInterceptor: ResourceInterceptor, ): OkHttpClient =
        generalOkHttpClient
            .addInterceptor(resourceInterceptor)
            .build()

    @Provides
    @Named(MMS_AUTH)
    fun mmsHttpClient(generalOkHttpClient: OkHttpClient.Builder, mmsInterceptor: MMSInterceptor ): OkHttpClient =
        generalOkHttpClient
            .addInterceptor(mmsInterceptor)
            .build()

    @Provides
    fun generalHttpClientBuilder(interceptor: Interceptor, httpLoggingInterceptor: HttpLoggingInterceptor, ): OkHttpClient.Builder =
        OkHttpClient()
            .newBuilder()
            .connectTimeout(Const.REQ_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(Const.REQ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(Const.REQ_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(httpLoggingInterceptor)
}