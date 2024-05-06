package com.pep.pod.ui.screen.pay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pep.pod.R
import com.pep.pod.domain.dto.PodNeedSettlement
import com.pep.pod.domain.useCases.podUseCase.PodAddNeedSettlement
import com.pep.pod.domain.useCases.podUseCase.PodSettlementByIDUseCase
import com.pep.pod.payment.Receipt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PayVM @Inject constructor(private val podAddNeedSettlement: PodAddNeedSettlement, private val podSettlementByIDUseCase: PodSettlementByIDUseCase) : ViewModel() {
    private val _state = MutableStateFlow(PayState())
    val state = _state.asStateFlow()
    private var receipt: Receipt.Purchase? = null

    fun startOnPay() {
        _state.update { it.copy(payStatusId = R.string.onPay) }
    }

    fun failOnPay() {
        _state.update { it.copy(payStatusId = R.string.unsuccess) }
    }

    fun paidNeedSettlement() {
        _state.update { it.copy(payStatusId = R.string.paid_need_settlement) }
    }

    fun onSetReceipt(_receipt: Receipt.Purchase) {
        receipt = _receipt
        insertReceiptDB()
    }

    private fun insertReceiptDB() {
        viewModelScope.launch(Dispatchers.IO) {
            receipt?.let {
                podAddNeedSettlement.invoke(
                    PodNeedSettlement(id = it.invoice.toLong(), terminal = it.terminal, merchant = it.merchant, date = it.timeDate, sequence = it.sequence, reference = it.reference)
                ).catch { e ->
                    e.printStackTrace()
                }.collect{
                    settlementInvoice()
                }
            }
        }
    }

    fun settlementInvoice() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(payStatusId = R.string.success_on_settlement, settlementProgress = true)
            }
            podSettlementByIDUseCase.invoke(Unit).catch { e ->
                    e.printStackTrace()
                    _state.update {
                        it.copy(payStatusId = R.string.unsuccess_settlement, settlementProgress = false)
                    }
                }.collect { status ->
                    if (status) {
                        _state.update {
                            it.copy(
                                payStatusId = R.string.success_settlement,
                                settlementProgress = false
                            )
                        }
                    }
                    else {
                        _state.update {
                            it.copy(
                                payStatusId = R.string.unsuccess_settlement,
                                settlementProgress = false
                            )
                        }
                    }
                }
        }
    }
}

data class PayState(val payStatusId: Int = R.string.notPay, val settlementProgress: Boolean = false)