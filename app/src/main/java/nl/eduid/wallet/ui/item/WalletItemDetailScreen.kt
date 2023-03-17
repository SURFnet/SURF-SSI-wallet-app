package nl.eduid.wallet.ui.item

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.composables.attribute.WalletAttributeCard
import nl.eduid.wallet.shared.presentation.composables.button.PrimaryButton
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeBorder
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeMissingAttributeBorder
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeMissingAttributeOnSurface
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeMissingAttributeSurface
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeOnSurface

@Composable
fun WalletItemDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: WalletItemDetailsViewModel = mavericksViewModel(),
    onDone: () -> Unit
) {

    val state by viewModel.collectAsState()

    WalletItemDetailScreen(
        modifier = modifier,
        name = state.name,
        unverified = state.unverified,
        verified = state.verified,
        onDone = onDone
    )
}

@Composable
fun WalletItemDetailScreen(
    modifier: Modifier = Modifier,
    name: String? = null,
    unverified: List<WalletAttribute> = emptyList(),
    verified: List<WalletAttribute> = emptyList(),
    onDone: () -> Unit,
) {

    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = true)
        onDispose {}
    }

    Column(
        modifier = modifier
            .padding(horizontal = 32.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(52.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_wallet),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(54.dp))

        Text(
            text = "All details of",
            style = MaterialTheme.typography.h5
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = name ?: "",
            style = MaterialTheme.typography.h5,
            color = Color(0xFF008738)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {


            Text(
                modifier = Modifier,
                text = "Unverified",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.ic_question),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        for (item in unverified) {
            when (item) {
                is WalletAttribute.InWallet -> {
                    WalletAttributeInWallet(item)
                }
                WalletAttribute.Missing -> {
                    WalletItemMissing()
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            val drawable =
                AppCompatResources.getDrawable(LocalContext.current, R.drawable.ic_verified_badge)

            Image(
                painter = rememberDrawablePainter(drawable = drawable),
                contentDescription = null,
            )

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier,
                text = "Verified",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.ic_question),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        for (item in verified) {
            when (item) {
                is WalletAttribute.InWallet -> {
                    WalletAttributeInWallet(item)
                }
                WalletAttribute.Missing -> {
                    WalletItemMissing()
                }
            }
        }


        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onDone
        ) {
            Text(text = "Done")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun WalletAttributeInWallet(item: WalletAttribute.InWallet) {
    val context = LocalContext.current
    WalletAttributeCard(
        borderColor = MaterialTheme.colors.walletAttributeBorder,
        topContent = {
            Text(
                text = item.value,
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
                        append(
                            text = item.issuer
                        )
                    }
                },
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.walletAttributeOnSurface
            )
        },
        trailingContent = if (item.deletable) {{
            val painter = painterResource(id = R.drawable.ic_trash)
            Icon(painter = painter, contentDescription = null)
        }} else null
    )
}

@Composable
private fun WalletItemMissing(
    modifier: Modifier = Modifier,
) {
    WalletAttributeCard(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.walletAttributeMissingAttributeSurface,
        borderColor = MaterialTheme.colors.walletAttributeMissingAttributeBorder,
        topContent = {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Not available")
                    }
                },
                style = MaterialTheme.typography.body1
            )
        },
        bottomContent = {
            Text(
                text = "Proceed to add this to your Wallet",
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
fun WalletItemDetailScreenPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            WalletItemDetailScreen(
                modifier = Modifier.fillMaxSize(),
                name = "Full name",
                unverified = listOf(
                    WalletAttribute.InWallet(
                        value = "René v. Hamersdonksveer",
                        issuer = "you",
                        deletable = false
                    )
                ),
                verified = listOf(
                    WalletAttribute.Missing
                ),
                onDone = {}
            )
        }
    }
}