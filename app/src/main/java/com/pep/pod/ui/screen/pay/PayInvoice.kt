package com.pep.pod.ui.screen.pay

import TransactionData.Companion.INTENT_KEY
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.pep.pod.R
import com.pep.pod.domain.dto.PodInvoiceModel
import com.pep.pod.payment.Receipt
import com.pep.pod.ui.theme.BlueLeveL0
import com.pep.pod.ui.theme.BlueLevel2
import com.pep.pod.ui.theme.OrangeLevel0
import com.pep.pod.ui.theme.OrangeLevel1
import com.pep.pod.ui.theme.PODTheme
import com.pep.pod.ui.theme.PODTypography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayInvoice(id: String, number: String, amount: String, needSettlement: Boolean, onBack: () -> Unit, vm: PayVM = hiltViewModel()) {
    LaunchedEffect(Unit){
        if(needSettlement)
            vm.paidNeedSettlement()
    }
    var needForSettlement by remember(needSettlement) {
        mutableStateOf(needSettlement)
    }
    val state by vm.state.collectAsState()
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                scope.launch {
                    result.data?.getStringExtra(INTENT_KEY)?.let {
                        val receipt = TransactionData.moshi.adapter(Receipt::class.java).fromJson(it)
                        if (receipt is Receipt.Purchase && receipt.isSuccess) {
                            vm.onSetReceipt(receipt)
                            needForSettlement=true
                        } else {
                            vm.failOnPay()
                        }
                    }
                }
            } else {
                vm.failOnPay()
            }
        }
    )

    val onStartTransaction: () -> Unit = remember {
        {
            scope.launch {
                Intent(TransactionData.ACTION_PURCHASE_TRANSACTION).apply {
                    type = "text/json"
                    putExtra(INTENT_KEY, TransactionData.Purchase(amount = amount, invoice = id.takeIf { it.isDigitsOnly() } ?: "").toJson())
                }.also {
                    vm.startOnPay()
                    launcher.launch(it)
                }
            }
        }
    }

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
        Column(modifier = Modifier.fillMaxSize().absolutePadding(16.dp, 70.dp, 16.dp, 0.dp)) {
            Card(shape = RoundedCornerShape(15.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.invoice_id), modifier = Modifier.weight(0.1f), style = PODTypography.titleSmall)
                        Text(id, style = PODTypography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.invoice_number), modifier = Modifier.weight(0.1f), style = PODTypography.titleSmall)
                        Text(number, style = PODTypography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.invoice_amount), modifier = Modifier.weight(0.1f), style = PODTypography.titleSmall)
                        Text(amount.plus(" ").plus(stringResource(id = R.string.rial)), style = PODTypography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.invoice_status), modifier = Modifier.weight(0.1f), style = PODTypography.titleSmall)
                        Text(stringResource(state.payStatusId), style = PODTypography.titleSmall.copy(fontWeight = FontWeight.SemiBold))

                        if (state.settlementProgress)
                            CircularProgressIndicator(modifier = Modifier.size(16.dp).padding(4.dp), strokeWidth = 2.dp)
                    }
                }
            }
            Button(modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = OrangeLevel1), shape = RoundedCornerShape(15.dp), enabled = state.payStatusId != R.string.success_settlement, onClick = if (needForSettlement) vm::settlementInvoice else onStartTransaction,) {
                Text(if (needForSettlement) stringResource(R.string.settlement) else stringResource(R.string.pay))
            }
//            Button(onClick = onBack, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)) {
//                Text(stringResource(R.string.back))
//            }
        }
    }
}

@Composable
@Preview
fun PayInvoicePreview() {
    PODTheme {
        PayInvoice(
            amount = "12000 ".plus(stringResource(id = R.string.rial)),
            number = "1",
            id = "439594",
            needSettlement = false,
            onBack = {}
        )
    }
}