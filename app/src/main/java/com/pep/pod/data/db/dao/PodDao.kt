package com.pep.pod.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.pep.pod.data.dto.NeedSettlement
import com.pep.pod.data.dto.response.Invoice
import kotlinx.coroutines.flow.Flow

@Dao
interface PodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Invoice>)

    @Query("Select * From invoice")
    fun getInvoices(): Flow<List<Invoice>>

    @Query("Delete From invoice")
    suspend fun deleteInvoices()

    @Query("SELECT COUNT(*) FROM invoice")
    suspend fun count(): Int

    @Query("SELECT * FROM Invoice ORDER BY issuanceDate DESC")
    fun pagingSource(): PagingSource<Int, Invoice>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNeedSettlement(item: NeedSettlement)

    @Query("Delete From needsettlement WHERE id = :id")
    suspend fun deleteFromSettlement(id: Long)

    @Query("Select * From needsettlement")
    suspend fun getNotSettlement():List<NeedSettlement>

    @Query("SELECT EXISTS(SELECT * FROM needsettlement WHERE id=:id)")
    suspend fun isNeedSettlement(id: Long):Boolean
}