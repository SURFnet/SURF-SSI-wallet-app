package nl.eduid.wallet.shared.presentation.compose.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import nl.eduid.wallet.shared.presentation.compose.theme.color.AppLightColors

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = AppLightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
