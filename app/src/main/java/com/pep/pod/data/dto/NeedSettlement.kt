package com.pep.pod.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NeedSettlement(
    @PrimaryKey(autoGenerate = false)
    val id : Long,
    val terminal:String,
    val merchant:String,
    val reference:String,
    val sequence:String,
    val date:String,
)

