package com.pep.pod.ui.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pep.pod.ui.MainActivity
import com.pep.pod.ui.screen.invoices.InvoicesScreen
import com.pep.pod.ui.screen.customer.CustomerScreen
import com.pep.pod.ui.screen.invoices.InvoiceVM
import com.pep.pod.ui.screen.not_found.NotFound
import com.pep.pod.ui.screen.pay.PayInvoice
import dagger.hilt.android.EntryPointAccessors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NavGraph(navHostController: NavHostController, startDestination: String) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val onBack: () -> Unit = remember {
        {
            scope.launch(Dispatchers.Main) {
                navHostController.popBackStack()
            }
        }
    }

    NavHost(navController = navHostController, startDestination = startDestination) {
        composable(Destination.PayInvoice().route) { backStackEntry ->
            PayInvoice(
                id = backStackEntry.arguments?.getString(Destination.InvoiceID.route, "") ?: "",
                amount = backStackEntry.arguments?.getString(Destination.InvoiceAmount.route, "") ?: "",
                number = backStackEntry.arguments?.getString(Destination.InvoiceNumber.route, "") ?: "",
                needSettlement = (backStackEntry.arguments?.getString(Destination.InvoiceNeedSettlement.route, "false") ?: "false").toBoolean(),
                onBack = onBack,
            )
        }

        composable(Destination.Invoices().route) { backStackEntry ->
            InvoicesScreen(
                onBack = onBack,
                toPayInvoice = {
                    scope.launch {
                        navHostController.navigate(
                            Destination.PayInvoice(
                                id = it.id,
                                amount = it.billAmount,
                                number = it.billNumber,
                                needSettlement = it.needSettlement.toString()
                            ).route
                        )
                    }
                },
                vm = viewModel(
                    factory = providesFactory(
                        factory = injectProvider().invoiceProviderFactory(),
                        data = backStackEntry.arguments?.getString(Destination.InvoicesCustomerID.route, "") ?: ""
                    )
                )
            )
        }

        composable(Destination.Customer.route) {
            CustomerScreen(
                toInvoices = { id ->
                    scope.launch {
                        navHostController.navigate(Destination.Invoices(id = id).route)
                    }
                },
            )
        }
    }
}

interface BaseFactory<T : ViewModel, R> {
    fun create(data: R): T
}

@Composable
private fun injectProvider() = EntryPointAccessors.fromActivity(
    LocalContext.current as Activity,
    MainActivity.ViewModelFactoryProvider::class.java
)

@Suppress("UNCHECKED_CAST")
fun <X : ViewModel, R> providesFactory(factory: BaseFactory<X, R>, data: R): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return factory.create(data) as T
    }
}
