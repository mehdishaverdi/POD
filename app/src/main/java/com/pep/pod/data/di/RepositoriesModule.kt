package com.pep.pod.data.di

import com.pep.pod.data.repository.PodRepositoryImpl
import com.pep.pod.domain.repository.PodRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {
  @Binds
  fun bindPodRepository(impl:PodRepositoryImpl):PodRepository
}