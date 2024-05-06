package com.pep.pod.data.di

import android.content.Context
import androidx.room.Room
import com.pep.pod.data.db.DataBase
import com.pep.pod.data.db.dao.PodDao
import com.pep.pod.data.db.dao.TransactionRunner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext application: Context, ): DataBase =
        Room.databaseBuilder(application, DataBase::class.java, DataBase.DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun providePodDao(database: DataBase): PodDao =
        database.getPodDao()

    @Provides
    @Singleton
    fun provideTransactionRunner(database: DataBase): TransactionRunner =
        database.getTransactionRunner()
}