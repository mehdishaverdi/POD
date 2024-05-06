package com.pep.pod.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pep.pod.data.dto.response.Invoice
import com.pep.pod.data.db.dao.PodDao
import com.pep.pod.data.db.dao.TransactionRunnerDao
import com.pep.pod.data.dto.NeedSettlement

@Database(
    entities = [Invoice::class, NeedSettlement::class],
    version = 5,
    exportSchema = false
)

abstract class DataBase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "PodDataBase"
    }

    abstract fun getPodDao(): PodDao
    abstract fun getTransactionRunner(): TransactionRunnerDao
}