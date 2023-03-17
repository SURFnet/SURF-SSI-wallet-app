package nl.eduid.wallet.ui.qrscanner.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun QrCodeScannerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = darkColors(
            primary = Color.White,
            onBackground = Color.White
        ),
        content = content
    )
}