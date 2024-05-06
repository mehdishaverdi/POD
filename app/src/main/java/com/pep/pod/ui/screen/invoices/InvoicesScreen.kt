package com.pep.pod.ui.screen.invoices

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.pep.pod.R
import com.pep.pod.domain.dto.PodInvoiceModel
import com.pep.pod.ui.theme.OrangeLevel0
import com.pep.pod.ui.theme.PODTheme
import com.pep.pod.ui.theme.PODTypography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun InvoicesScreen(onBack: () -> Unit, toPayInvoice: (PodInvoiceModel) -> Unit, vm: InvoiceVM) {
    InvoicesContent(invoices = vm.getInvoices(), toPayInvoice = toPayInvoice, onBack = onBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InvoicesContent(invoices: Flow<PagingData<PodInvoiceModel>>, toPayInvoice: (PodInvoiceModel) -> Unit, onBack: () -> Unit, ) {
    val invoiceList = remember {
        invoices
    }.collectAsLazyPagingItems(Dispatchers.IO)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "فاکتور مشتری", style = PODTypography.titleSmall)
                },
                actions = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = OrangeLevel0)
            )
        }
    ) { paddingValues ->

        when (invoiceList.loadState.refresh) {
            is LoadState.Error ->
                InvoiceNotFound()

            LoadState.Loading -> if (invoiceList.itemCount == 0)
                CircularProgressIndicator()

            is LoadState.NotLoading -> {
                if (invoiceList.itemCount == 0)
                    InvoiceNotFound()
            }
        }

        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(invoiceList.itemCount) { idx ->
                invoiceList.itemSnapshotList[idx]?.let {
                    InvoicesItem(invoice = it, onItemClick = toPayInvoice)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InvoicesItem(invoice: PodInvoiceModel, onItemClick: (PodInvoiceModel) -> Unit) {
    val scope = rememberCoroutineScope()
    val onClick: () -> Unit = remember {
        {
            scope.launch {
                onItemClick(invoice)
            }
        }
    }

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = if(invoice.needSettlement) Color.Red.copy(alpha = 0.2f) else Color.Cyan)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            Text(text = "${stringResource(id = R.string.bill_number)}: ${invoice.billNumber}", style = PODTypography.titleSmall)
            Text(text = "${stringResource(id = R.string.amount)}: ${invoice.billAmount} ${stringResource(id = R.string.rial)}", style = PODTypography.titleSmall)
        }
    }
}

@Composable
@Preview
private fun InvoicesPreview() {
    PODTheme {
        InvoicesItem(
            PodInvoiceModel(businessName = "سپاهان باتری",
                billAmount = "12000 ".plus(stringResource(id = R.string.rial)),
                billNumber = "1",
                id = "",
                needSettlement = false
            ),
            onItemClick = { _ -> }
        )
    }
}

@Composable
@Preview
private fun InvoicesContentPreview() {
    PODTheme {
        InvoicesContent(
            onBack = {},
            toPayInvoice = {},
            invoices = flowOf(
                PagingData.from(
                    listOf(
                        PodInvoiceModel(
                            businessName = "سپاهان باتری",
                            billAmount = "12000 ".plus(stringResource(id = R.string.rial)),
                            billNumber = "1",
                            id = "",
                            needSettlement = false
                        ),
                        PodInvoiceModel(
                            businessName = "سپاهان باتری",
                            billAmount = "13000 ".plus(stringResource(id = R.string.rial)),
                            billNumber = "2",
                            id = "",
                            needSettlement = true
                        ),
                        PodInvoiceModel(
                            businessName = "سپاهان باتری",
                            billAmount = "14000 ".plus(stringResource(id = R.string.rial)),
                            billNumber = "3",
                            id = "",
                            needSettlement = true
                        )
                    )
                )
            )
        )
    }
}