package com.pep.pod.ui.navigation

sealed class Destination(val route: String) {
    object Customer : Destination("Customer")
    object InvoicesCustomerID : Destination("InvoicesCustomerID")
    object InvoiceID : Destination("InvoiceID")
    object InvoiceNumber : Destination("InvoiceNumber")
    object InvoiceAmount : Destination("InvoiceAmount")
    object InvoiceNeedSettlement : Destination("InvoiceNeedSettlement")

    data class Invoices(private val id: String = "{${InvoicesCustomerID.route}}") :
        Destination("Invoices/$id")

    data class PayInvoice(
        private val id: String = "{${InvoiceID.route}}",
        private val number: String = "{${InvoiceNumber.route}}",
        private val amount: String = "{${InvoiceAmount.route}}",
        private val needSettlement: String = "{${InvoiceNeedSettlement.route}}"
    ) : Destination("Invoices/$id/$number/$amount/$needSettlement")
}