package nl.eduid.wallet.shared.presentation.composables.button

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme
import nl.eduid.wallet.shared.presentation.compose.theme.color.onPrimaryButtonBackground
import nl.eduid.wallet.shared.presentation.compose.theme.color.onPrimaryButtonBackgroundDisabled
import nl.eduid.wallet.shared.presentation.compose.theme.color.primaryButtonBackground
import nl.eduid.wallet.shared.presentation.compose.theme.color.primaryButtonBackgroundDisabled

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable (RowScope.() -> Unit)
) {
    Button(
        modifier = modifier
            .heightIn(min = 48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primaryButtonBackground,
            contentColor = MaterialTheme.colors.onPrimaryButtonBackground,
            disabledBackgroundColor = MaterialTheme.colors.primaryButtonBackgroundDisabled,
            disabledContentColor = MaterialTheme.colors.onPrimaryButtonBackgroundDisabled
        ),
        onClick = onClick
    ) {
        content()
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    AppTheme {
        PrimaryButton(
            modifier = Modifier.width(300.dp),
            onClick = {}
        ) {
            Text(
                text = "Connect your eduID"
            )
        }
    }
}

@Preview
@Composable
fun PrimaryButtonDisabledPreview() {
    AppTheme {
        PrimaryButton(
            modifier = Modifier.width(300.dp),
            enabled = false,
            onClick = {}
        ) {
            Text(
                text = "Connect your eduID"
            )
        }
    }
}
