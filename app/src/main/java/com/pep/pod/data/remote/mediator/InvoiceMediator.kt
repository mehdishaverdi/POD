package com.pep.pod.data.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.pep.pod.data.db.dao.PodDao
import com.pep.pod.data.db.dao.TransactionRunner
import com.pep.pod.data.dto.response.Invoice
import com.pep.pod.data.remote.PodRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class InvoiceMediator @Inject constructor(private val dao: PodDao, private val transactionRunner: TransactionRunner, private val remote: PodRemote, ) : RemoteMediator<Int, Invoice>() {
    private var id: String = ""

    fun setId(_id: String) = apply { id = _id }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Invoice>): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH ->
                    0

                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    state.lastItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = true)
                    withContext(Dispatchers.IO) { dao.count() }
                }
            }

            val response = withContext(Dispatchers.IO) {
                remote.fetchInvoices(offset = loadKey, userId = id)
            }

            transactionRunner {
                if (loadType == LoadType.REFRESH)
                    dao.deleteInvoices()
                dao.insertAll(response.result)
            }

            MediatorResult.Success(endOfPaginationReached = response.count == withContext(Dispatchers.IO) { dao.count() })
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
