package com.pep.pod.data.dto.response

import androidx.room.ColumnInfo
import androidx.room.Embedded

data class Business(
    @ColumnInfo(name = "business_id")
    val id: Long?=null,
    @ColumnInfo(name = "business_name")
    val name: String?,
)