package nl.eduid.wallet.ui.splash.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import nl.eduid.wallet.ui.splash.theme.lightColors

@Composable
fun SplashTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = lightColors,
        content = content
    )
}