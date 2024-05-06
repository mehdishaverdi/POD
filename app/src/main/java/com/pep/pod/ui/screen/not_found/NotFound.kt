package com.pep.pod.ui.screen.not_found

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
fun NotFound(onRetry: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.ic_not_found), contentDescription = null, modifier = Modifier.padding(8.dp).size(96.dp))
        Text(text = stringResource(id = R.string.business_not_found), style = PODTypography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Row(verticalAlignment = Alignment.CenterVertically) {
               Icon(imageVector = Icons.Filled.Refresh, contentDescription =null , modifier = Modifier.padding(4.dp))
               Text(text = stringResource(id = R.string.retry))
            }
        }
    }
}

@Composable
@Preview
private fun NotFoundPreview() {
    PODTheme {
        NotFound({})
    }
}