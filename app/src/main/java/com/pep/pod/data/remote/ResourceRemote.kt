package com.pep.pod.data.remote

import com.pep.pod.data.dto.response.PodResourceResponse
import com.pep.pod.data.remote.services.ResourceService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceRemote @Inject constructor(private val resourceService: ResourceService, ) {
    suspend fun getResource(uniqueId: String): PodResourceResponse =
        resourceService.getResource(uniqueId)
}
