package com.pep.pod.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.pep.pod.R
import com.pep.pod.ui.navigation.Destination
import com.pep.pod.ui.navigation.NavGraph
import com.pep.pod.ui.screen.invoices.InvoiceVM
import com.pep.pod.ui.screen.not_found.NotFound
import com.pep.pod.ui.theme.PODTheme
import com.pep.pod.ui.theme.PODTypography
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: MainVM by viewModels()

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ViewModelFactoryProvider {
        fun invoiceProviderFactory(): InvoiceVM.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            installSplashScreen()

            val context = LocalContext.current
            val state by vm.state.collectAsState()
            val scope = rememberCoroutineScope()

            val deviceSerial = remember {
                context.contentResolver.call(Uri.parse("content://com.pep.pos.provider.Reports"), "DeviceSerialNumber", null, null).let {
                    it?.getString("Data") ?: ""
                }
            }

            val onRetry: () -> Unit = remember(deviceSerial) {
                {
                    scope.launch {
                        vm.updateBusiness(serial = deviceSerial)
                    }
                }
            }

            LaunchedEffect(Unit) {
                onRetry()
            }

            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                when (state) {
                    MainState.ResourceFail -> NotFound(onRetry = onRetry)
                    MainState.ResourceLoading -> Loading()
                    MainState.ResourceSuccess -> {
                        val navController = rememberNavController()
                        PODTheme {
                            NavGraph(navHostController = navController, startDestination = Destination.Customer.route)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Loading() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(modifier = Modifier.padding(8.dp))
        Text(text = stringResource(id = R.string.resource_fetch_in_progress), style = PODTypography.titleMedium)
    }
}

@Preview
@Composable
fun LoadingPreview() {
    PODTheme {
        Loading()
    }
}