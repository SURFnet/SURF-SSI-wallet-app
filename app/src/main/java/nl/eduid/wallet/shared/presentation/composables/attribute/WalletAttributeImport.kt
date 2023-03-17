package nl.eduid.wallet.shared.presentation.composables.attribute

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeImportBorder
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeImportContentColor

@Composable
fun WalletAttributeImport(
    modifier: Modifier = Modifier,
    attributeType: AnnotatedString,
    onClick: () -> Unit
) {
    WalletAttributeCard(
        modifier = modifier
            .clickable(onClick = onClick),
        borderColor = MaterialTheme.colors.walletAttributeImportBorder,
        contentColor = MaterialTheme.colors.walletAttributeImportContentColor,
        topContent = {
            Text(
                text = attributeType,
                style = MaterialTheme.typography.body1
            )
        },
        bottomContent = {
            Text(
                text = stringResource(
                    id = R.string.attribute_not_in_wallet
                ),
                style = MaterialTheme.typography.caption,
            )
        },
        trailingContent = {
            val painter = painterResource(id = R.drawable.ic_arrow_right)
            Icon(painter = painter, contentDescription = null)
        }
    )
}

@Preview
@Composable
fun WalletAttributeImportPreview() {
    AppTheme {
        WalletAttributeImport(
            attributeType = buildAnnotatedString {
                append("Proof of ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("being a student")
                }
            },
            onClick = {}
        )
    }
}
