package com.pep.pod.ui.screen.invoices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.pep.pod.domain.useCases.podUseCase.PodInvoicesUseCase
import com.pep.pod.ui.navigation.BaseFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class InvoiceVM @AssistedInject constructor(@Assisted private val customerId: String, private val ucGetInvoice: PodInvoicesUseCase, ) : ViewModel() {
    @AssistedFactory
    interface Factory : BaseFactory<InvoiceVM, String>

    fun getInvoices() = ucGetInvoice.invoke(customerId).cachedIn(viewModelScope)
}
