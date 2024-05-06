package com.pep.pod.data.remote

import com.pep.pod.data.remote.services.MmsService
import com.pep.pod.data.repository.MMSResponse
import javax.inject.Inject

class MmsRemote @Inject constructor(private val mmsService: MmsService) {
    suspend fun getToken(
        UserName: String,
        Password: String
    ): MMSResponse =
        mmsService.getToken(
            UserName = UserName,
            Password = Password
        ).let {
            if (it.Success)
                throw Throwable(message = it.Message)
            it
        }
}