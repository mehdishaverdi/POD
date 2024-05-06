package com.pep.pod.ui.screen.invoices

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pep.pod.R
import com.pep.pod.ui.theme.PODTheme
import com.pep.pod.ui.theme.PODTypography

@Composable
fun InvoiceNotFound() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.ic_receipt_), contentDescription = null, modifier = Modifier.size(64.dp))
        Text(text = stringResource(id = R.string.invoice_not_found), style = PODTypography.titleSmall,)
    }
}

@Preview
@Composable
private fun NotTransactionFoundPreview() {
    PODTheme {
        Column {
            InvoiceNotFound()
        }
    }
}
