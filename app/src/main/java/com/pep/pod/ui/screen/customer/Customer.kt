package com.pep.pod.ui.screen.customer

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.pep.pod.R
import com.pep.pod.ui.theme.OrangeLevel1
import com.pep.pod.ui.theme.PODTheme
import com.pep.pod.ui.theme.PODTypography
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerScreen(toInvoices: (String) -> Unit, vm: CustomerVM = hiltViewModel()) {
    val state by vm.state.collectAsState()

    Scaffold {
        CustomerContent(businessIcon = state.icon, businessName = state.name, toInvoices = toInvoices)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomerContent(businessName: String, businessIcon: String, toInvoices: (String) -> Unit) {
    val scope = rememberCoroutineScope()
    var username by rememberSaveable {
        mutableStateOf("")
    }
    var isError by remember {
        mutableStateOf(false)
    }
    val toInvoicesPage: () -> Unit = remember {
        {
            scope.launch {
                toInvoices(username)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            SubcomposeAsyncImage(
                model = businessIcon,
                contentDescription = null,
                modifier = Modifier
                    .padding(4.dp)
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit,
                loading = {
                    CircularProgressIndicator()
                },
                error = {
                    Image(painter = painterResource(id = R.drawable.pod_logo), contentDescription = null, contentScale = ContentScale.Fit)
                },
            )

            Text(text = businessName, style = PODTypography.titleLarge)
            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = username,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = PODTypography.titleMedium,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = isError,
                onValueChange = {
                    isError = false
                    if (it.isDigitsOnly())
                        username = it
                },
                label = {
                    Text(text = stringResource(R.string.customer_number), style = PODTypography.titleSmall)
                },
            )

            AnimatedVisibility(visible = isError) {
                Text(text = stringResource(id = R.string.username_not_valid), style = PODTypography.bodyMedium.copy(color = Color.Red), modifier = Modifier.padding(top = 4.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = toInvoicesPage, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = OrangeLevel1), shape = RoundedCornerShape(15.dp)) {
                Text(text = stringResource(R.string.ok))
            }
        }
    }
}

@Preview
@Composable
private fun CustomerPreview() {
    PODTheme {
        CustomerContent(businessName = "سپاهان باتری", businessIcon = "", toInvoices = {},)
    }
}