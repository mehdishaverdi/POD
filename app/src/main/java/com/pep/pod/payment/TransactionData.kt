import com.pep.pod.payment.Receipt
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.Serializable

sealed class TransactionData : Serializable {
    companion object {
        const val ACTION_PURCHASE_TRANSACTION: String = "com.pep.pos.ACTION_PURCHASE_TRANSACTION"
        const val ACTION_SETTING: String = "com.pep.pos.ACTION_SETTING"
        const val ACTION_CHARGE_VOUCHER_TRANSACTION: String = "com.pep.pos.ACTION_CHARGE_VOUCHER_TRANSACTION"
        const val ACTION_CHARGE_TOPUP_TRANSACTION: String = "com.pep.pos.ACTION_CHARGE_TOPUP_TRANSACTION"
        const val ACTION_BILL_TRANSACTION: String = "com.pep.pos.ACTION_BILL_TRANSACTION"
        const val ACTION_BALANCE_TRANSACTION: String = "com.pep.pos.ACTION_BALANCE_TRANSACTION"
        const val INTENT_KEY: String = "Data"
        val moshi: Moshi =
            Moshi.Builder()
                .add(
                    PolymorphicJsonAdapterFactory
                        .of(Receipt::class.java, "subClassType")
                        .withSubtype(Receipt.Purchase::class.java, "Purchase")
                        .withSubtype(Receipt.BalanceInquiry::class.java, "BalanceInquiry")
                        .withSubtype(Receipt.Bill::class.java, "Bill")
                        .withSubtype(Receipt.ChargeEvoucher::class.java, "ChargeEvoucher")
                        .withSubtype(Receipt.ChargeTopup::class.java, "ChargeTopup")
                        .withSubtype(Receipt.FailedReceipt::class.java, "FailedReceipt")
                )
                .add(KotlinJsonAdapterFactory())
                .build()
    }

    enum class Operators(val voucher: Long, val topUp: Long) {
        HAMRAH_AVAL(voucher = 9912, topUp = 9914),
        IRANCELL(voucher = 9935, topUp = 9937),
        RIGHTEL(voucher = 9920, topUp = 9921)
    }

    abstract fun toJson(): String

    data class Purchase(val amount: String, val invoice: String, ) : TransactionData() {
        override fun toJson(): String = moshi.adapter(Purchase::class.java).toJson(this)
    }

    data class ChargeVoucher(val amount: String, val operator: Long, val number: Int, ) : TransactionData() {
        override fun toJson(): String = moshi.adapter(ChargeVoucher::class.java).toJson(this)
    }

    data class ChargeTopup(val mobile: String, val amount: String) : TransactionData() {
        override fun toJson(): String = moshi.adapter(ChargeTopup::class.java).toJson(this)
    }

    data class Bill(val billId: String, val paymentId: String, ) : TransactionData() {
        override fun toJson(): String = moshi.adapter(Bill::class.java).toJson(this)
    }
}


