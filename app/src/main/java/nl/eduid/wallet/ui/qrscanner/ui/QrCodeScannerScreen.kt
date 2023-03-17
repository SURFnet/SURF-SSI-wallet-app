package nl.eduid.wallet.ui.qrscanner.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import nl.eduid.wallet.ui.qrscanner.QrCodeScannerViewModel
import nl.eduid.wallet.ui.qrscanner.QrCodeScannerViewState

internal val TransparentGray = Color(0x3C3535E5)
private val emptyLambda = {}

@Composable
fun QrCodeScannerScreen(
    modifier: Modifier = Modifier,
    viewModel: QrCodeScannerViewModel = mavericksViewModel(),
    onBackButtonClick: () -> Unit,
    onFlashLightClick: () -> Unit,
    onQrCodeScanned: (String) -> Unit,
    onQrCodeError: (Exception) -> Unit
) {

    val state by viewModel.collectAsState()

    QrCodeScannerScreen(
        modifier = modifier,
        state = state,
        onBackButtonClick = onBackButtonClick,
        onFlashLightClick = onFlashLightClick,
        onQrCodeScanned = onQrCodeScanned,
        onQrCodeError = onQrCodeError,
        onDismissPairingDialog = viewModel::dismissPairingDialog
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun QrCodeScannerScreen(
    modifier: Modifier = Modifier,
    state: QrCodeScannerViewState,
    onBackButtonClick: () -> Unit,
    onFlashLightClick: () -> Unit,
    onQrCodeScanned: (String) -> Unit,
    onQrCodeError: (Exception) -> Unit,
    onDismissPairingDialog: () -> Unit
) {

    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = true)
        onDispose {}
    }

    val controller = rememberSystemUiController()
    LaunchedEffect(null) {
        controller.setStatusBarColor(Color.Transparent, darkIcons = false)
    }

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    if (!cameraPermissionState.status.isGranted) {
        AlertDialog(
            onDismissRequest = emptyLambda,
            title = {
                Text(text = "Camera permission")
            },
            text = {
                val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                    // If the user has denied the permission but the rationale can be shown,
                    // then gently explain why the app requires this permission
                    "The camera is important for this app. Please grant the permission."
                } else {
                    // If it's the first time the user lands on this feature, or the user
                    // doesn't want to be asked again for this permission, explain that the
                    // permission is required
                    "Camera permission required for this feature to be available. " +
                        "Please grant the permission"
                }

                Text(textToShow)
            },
            confirmButton = {
                TextButton(
                    onClick = { cameraPermissionState.launchPermissionRequest() }
                ) {
                    Text("Grant")
                }
            }
        )
    }

    if (state.pairingCode != null) {
        PairingCodeDialog(
            pairingCode = state.pairingCode,
            onDismissRequest = onDismissPairingDialog
        )
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.background
    ) {
        Box {

            QrCodeScanner(
                modifier = Modifier.fillMaxSize(),
                onQrCodeScanned = onQrCodeScanned,
                onQrCodeError = onQrCodeError
            )

            CameraOverlay(
                modifier = Modifier.fillMaxSize(),
                onBackButtonClick = onBackButtonClick,
                onFlashLightClick = onFlashLightClick
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0000)
@Composable
internal fun QrCodeScannerScreenPreview() {
    QrCodeScannerTheme {
        QrCodeScannerScreen(
            modifier = Modifier.fillMaxSize(),
            state = QrCodeScannerViewState(),
            onBackButtonClick = {},
            onFlashLightClick = {},
            onQrCodeScanned = {},
            onQrCodeError = {},
            onDismissPairingDialog = {}
        )
    }
}