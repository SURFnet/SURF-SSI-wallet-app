package nl.eduid.wallet.shared.presentation.composables.attribute

import androidx.compose.foundation.Image
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
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeMissingAttributeBorder
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeMissingAttributeOnSurface
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeMissingAttributeSurface
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeOnSurface
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeRequestSatisfiedBorder

@Composable
fun WalletAttributeRequestSatisfied(
    modifier: Modifier = Modifier,
    attributeType: String,
) {
    WalletAttributeRequestSatisfied(
        modifier = modifier,
        attributeType = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(attributeType)
            }
        }
    )
}

@Composable
fun WalletAttributeRequestSatisfied(
    modifier: Modifier = Modifier,
    attributeType: AnnotatedString,
) {
    WalletAttributeCard(
        modifier = modifier,
        borderColor = MaterialTheme.colors.walletAttributeRequestSatisfiedBorder,
        topContent = {
            Text(
                text = attributeType,
                style = MaterialTheme.typography.body1
            )
        },
        bottomContent = {
            Text(
                text = stringResource(
                    id = R.string.attribute_in_wallet
                ),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.walletAttributeOnSurface
            )
        },
        trailingContent = {
            val painter = painterResource(id = R.drawable.ic_shield)
            Image(painter = painter, contentDescription = null)
        }
    )
}

@Composable
fun WalletAttributeRequest(
    modifier: Modifier = Modifier,
    attributeType: AnnotatedString,
    onClick: () -> Unit
) {
    WalletAttributeCard(
        modifier = modifier
            .clickable(onClick = onClick),
        backgroundColor = MaterialTheme.colors.walletAttributeMissingAttributeSurface,
        borderColor = MaterialTheme.colors.walletAttributeMissingAttributeBorder,
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
                color = MaterialTheme.colors.walletAttributeMissingAttributeOnSurface
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
fun WalletAttributeRequestSatisfiedPreview() {
    AppTheme {
        WalletAttributeRequestSatisfied(
            attributeType = buildAnnotatedString {
                append("Your ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("full name")
                }
            },
        )
    }
}

@Preview
@Composable
fun WalletAttributeRequestPreview() {
    AppTheme {
        WalletAttributeRequest(
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
