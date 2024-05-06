package com.pep.pod.data.di

import com.pep.pod.data.di.ServiceRequirementModule.MMS_AUTH
import com.pep.pod.data.di.ServiceRequirementModule.RESOURCE_AUTH
import com.pep.pod.data.di.ServiceRequirementModule.POD_AUTH
import com.pep.pod.data.remote.services.MmsService
import com.pep.pod.data.remote.services.PodService
import com.pep.pod.data.remote.services.ResourceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ServiceRequirementModule::class])
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun providePodAuthenticationService(@Named(POD_AUTH) retrofit: Retrofit): PodService =
        retrofit.create(PodService::class.java)

    @Provides
    @Singleton
    fun provideResourceAuthenticationService(@Named(RESOURCE_AUTH) retrofit: Retrofit): ResourceService =
        retrofit.create(ResourceService::class.java)

    @Provides
    @Singleton
    fun provideMmsAuthenticationService(@Named(MMS_AUTH) retrofit: Retrofit): MmsService =
        retrofit.create(MmsService::class.java)
}