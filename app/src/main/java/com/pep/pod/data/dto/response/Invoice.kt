package com.pep.pod.data.dto.response

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Invoice(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Long? = null,
    val id: Long,
    val totalAmountWithoutDiscount: Long?,
    val totalAmount: Long?,
    val payableAmount: Long?,
    val issuanceDate: Long?,
    val billNumber: String?,
    val payed: Boolean?,
    val canceled: Boolean?,
    @Embedded
    val business: Business?,
    val metadata: String
)