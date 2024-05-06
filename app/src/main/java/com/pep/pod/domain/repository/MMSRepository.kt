package com.pep.pod.domain.repository

import com.pep.pod.domain.dto.MMSGetToken

interface MMSRepository {
    suspend fun getToken(param: MMSGetToken): String
}