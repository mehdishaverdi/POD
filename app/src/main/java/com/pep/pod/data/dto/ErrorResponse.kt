package com.pep.pod.data.dto

import com.pep.pod.data.utils.json.SingleToArray

data class ErrorResponse(
    @SingleToArray val message: List<String>,
)