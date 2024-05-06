package com.pep.pod.payment

sealed class Receipt {
    abstract val type: String
    abstract val isSuccess: Boolean
    abstract val bankName: String
    abstract val cardNum: String
    abstract val timeDate: String
    abstract val sequence: String
    abstract val reference: String
    abstract val switchMsg: String
    abstract val terminal:String
    abstract val merchant:String

    data class Purchase(
        override val type: String,
        override val isSuccess: Boolean,
        override val bankName: String,
        override val cardNum: String,
        override val timeDate: String,
        override val sequence: String,
        override val reference: String,
        override val switchMsg: String,
        override val terminal: String,
        override val merchant: String,
        val amount: String,
        val invoice: String
    ) : Receipt()

    data class Bill(
        override val type: String,
        override val isSuccess: Boolean,
        override val bankName: String,
        override val cardNum: String,
        override val timeDate: String,
        override val sequence: String,
        override val reference: String,
        override val switchMsg: String,
        override val terminal: String,
        override val merchant: String,
        val billCompany: String,
        val billId: String,
        val billPaymentId: String,
        val amount: String,
    ) : Receipt()

    data class BalanceInquiry(
        override val type: String,
        override val isSuccess: Boolean,
        override val bankName: String,
        override val cardNum: String,
        override val timeDate: String,
        override val sequence: String,
        override val reference: String,
        override val switchMsg: String,
        override val terminal: String,
        override val merchant: String,
    ) : Receipt()

    data class ChargeEvoucher(
        override val type: String,
        override val isSuccess: Boolean,
        override val bankName: String,
        override val cardNum: String,
        override val timeDate: String,
        override val sequence: String,
        override val reference: String,
        override val switchMsg: String,
        override val terminal: String,
        override val merchant: String,
        val overallAmount: String,
        val singleAmount: String,
        val chargeCount: String,
        val chargeSerial: String,
    ) : Receipt()

    data class ChargeTopup(
        override val type: String,
        override val isSuccess: Boolean,
        override val bankName: String,
        override val cardNum: String,
        override val timeDate: String,
        override val sequence: String,
        override val reference: String,
        override val switchMsg: String,
        override val terminal: String,
        override val merchant: String,
        val amount: String,
        val chargePhone: String,
    ) : Receipt()

    data class FailedReceipt(
        override val type: String,
        override val isSuccess: Boolean,
        override val bankName: String,
        override val cardNum: String,
        override val timeDate: String,
        override val sequence: String,
        override val switchMsg: String,
        override val terminal: String,
        override val merchant: String,
        override val reference: String="",
    ) : Receipt()
}





