package nl.eduid.wallet.ui.home.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import nl.eduid.wallet.shared.presentation.compose.theme.color.onPrimaryButtonBackground
import nl.eduid.wallet.shared.presentation.compose.theme.color.primaryButtonBackground

@Composable
internal fun HomeButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
    contents: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {

        IconButton(
            modifier = Modifier
                .background(
                    MaterialTheme.colors.primaryButtonBackground,
                    RoundedCornerShape(6.dp)
                ),
            onClick = onClick
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.onPrimaryButtonBackground) {
                contents()
            }
        }

        Text(
            title,
            style = MaterialTheme.typography.body1,
            color = Color.White
        )
    }
}