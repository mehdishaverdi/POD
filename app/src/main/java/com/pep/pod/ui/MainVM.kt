package com.pep.pod.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pep.pod.domain.useCases.resourceUseCase.GetResourceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(private val ucGetResource: GetResourceUseCase) : ViewModel() {
    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState.ResourceLoading)
    val state = _state.asStateFlow()

    fun updateBusiness(serial: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update {
                    MainState.ResourceLoading
                }
                withTimeout(15000) {
                    val response = ucGetResource.invoke(serial)
                    _state.update {
                        if (response)
                            MainState.ResourceSuccess
                        else
                            MainState.ResourceFail
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update {
                    MainState.ResourceFail
                }
            }
        }
    }
}

sealed class MainState {
    object ResourceSuccess : MainState()
    object ResourceFail : MainState()
    object ResourceLoading : MainState()
}