package nl.eduid.wallet.shared.presentation.composables.attribute

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeBorder
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeOnSurface

@Composable
fun WalletAttributeTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.subtitle1
    )
}

@Composable
fun WalletAttributeReceived(
    modifier: Modifier = Modifier,
    attributeValue: String,
    issuer: String
) {

    val context = LocalContext.current

    WalletAttributeCard(
        modifier = modifier,
        borderColor = MaterialTheme.colors.walletAttributeBorder,
        topContent = {
            Text(
                text = attributeValue,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
        },
        bottomContent = {
            Text(
                text = buildAnnotatedString {
                    append(context.getString(R.string.provided_by))
                    append(" ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(issuer)
                    }
                },
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.walletAttributeOnSurface
            )
        })
}

@Composable
fun WalletAttributeReceivedWithTitle(
    modifier: Modifier = Modifier,
    title: String,
    attributeValue: String,
    issuer: String
) {
    Column(modifier = modifier) {
        WalletAttributeTitle(title = title)
        Spacer(modifier = Modifier.height(4.dp))
        WalletAttributeReceived(issuer = issuer, attributeValue = attributeValue)
    }

}

@Preview
@Composable
fun WalletAttributeReceivedPreview() {
    AppTheme {
        WalletAttributeReceived(
            attributeValue = "\uD83E\uDDD1\u200D\uD83C\uDF93 Student",
            issuer = "Universiteit van Amsterdam"
        )
    }
}
