package com.pep.pod.ui.screen.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pep.pod.domain.useCases.resourceUseCase.UCIsBusinessValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerVM @Inject constructor(private val isBusinessValid: UCIsBusinessValid) : ViewModel() {
    private val _state = MutableStateFlow(CustomerState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            combine(isBusinessValid.invoke(Unit)) { (business) ->
                CustomerState(icon = business.icon, name = business.name)
            }.catch { e ->
                e.printStackTrace()
            }.collect {
                _state.value = it
            }
        }
    }
}

data class CustomerState(val icon: String = "", val name: String = "")