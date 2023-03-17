package nl.eduid.wallet.ui.qrscanner.scanner

import androidx.annotation.OptIn
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.lang.Exception

typealias QrCodeListener = (String) -> Unit
typealias QrCodeErrorListener = (Exception) -> Unit

class QrCodeAnalyzer(
    private val onSuccess: QrCodeListener,
    private val onError: QrCodeErrorListener
) : ImageAnalysis.Analyzer {

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()

    private val scanner = BarcodeScanning.getClient(options)

    @OptIn(markerClass = [androidx.camera.core.ExperimentalGetImage::class])
    override fun analyze(proxy: ImageProxy) {

        val inputImage = proxy.image
            ?.let { InputImage.fromMediaImage(it, proxy.imageInfo.rotationDegrees) }

        if (inputImage != null) {
            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        onSuccess(barcode.rawValue ?: "")
                    }
                }
                .addOnFailureListener { onError(it) }
                .addOnCompleteListener { proxy.close() }
        }
    }
}
