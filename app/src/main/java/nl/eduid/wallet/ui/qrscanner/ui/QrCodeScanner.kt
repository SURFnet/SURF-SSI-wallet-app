package nl.eduid.wallet.ui.qrscanner.ui

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.guava.await
import nl.eduid.wallet.ui.qrscanner.scanner.QrCodeAnalyzer
import java.util.concurrent.Executors

@Composable
internal fun QrCodeScanner(
    modifier: Modifier = Modifier,
    onQrCodeScanned: (String) -> Unit,
    onQrCodeError: (Exception) -> Unit
) {

    val executor = remember {
        Executors.newSingleThreadExecutor()
    }

    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { analysis ->
                analysis.setAnalyzer(
                    executor,
                    QrCodeAnalyzer(
                        onSuccess = { code -> onQrCodeScanned(code) },
                        onError = { error -> onQrCodeError(error) }
                    )
                )
            }
    }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraProvider by produceState<ProcessCameraProvider?>(initialValue = null) {
        value = ProcessCameraProvider.getInstance(context).await()
    }

    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

    val preview = remember { Preview.Builder().build() }

    val camera = remember(cameraProvider) {
        cameraProvider?.let {
            it.unbindAll()
            it.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                imageAnalysis,
                preview,
            )
        }
    }

    LaunchedEffect(camera) {
        camera?.let {
//                if (it.cameraInfo.hasFlashUnit()) {
//                    it.cameraControl.enableTorch(enableTorch).await()
//                }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraProvider?.unbindAll()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            PreviewView(it).apply {
                preview.setSurfaceProvider(surfaceProvider)
            }
        }
    )
}