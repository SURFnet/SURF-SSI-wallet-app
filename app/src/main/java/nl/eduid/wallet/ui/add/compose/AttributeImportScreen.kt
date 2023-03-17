package nl.eduid.wallet.ui.add.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.composables.attribute.WalletAttributeCard
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeImportBorder
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeImportContentColor
import nl.eduid.wallet.ui.add.CredentialIssuer

@Composable
fun AttributeImportScreen(
    modifier: Modifier = Modifier,
    attributeType: String?,
    credentialIssuers: List<CredentialIssuer>,
    onBackClick: () -> Unit,
    onAttributeImportClick: (CredentialIssuer) -> Unit,
) {

    val systemUiController = rememberSystemUiController()
    val isThemeLight = MaterialTheme.colors.isLight

    LaunchedEffect(null) {
        systemUiController.setStatusBarColor(Color.Transparent, isThemeLight)
    }

    Column(
        modifier = modifier
            .padding(horizontal = 36.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painterResource(id = R.drawable.ic_back),
                    contentDescription = "back button",
                    tint = Color(0xFF0062B0)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_wallet),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(54.dp))

        Text(
            text = "Add",
            style = MaterialTheme.typography.h5
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = attributeType ?: "",
            style = MaterialTheme.typography.h5,
            color = Color(0xFF008738)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "The following source(s) can provide your Wallet with this information:",
            style = MaterialTheme.typography.body1
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            for (provider in credentialIssuers) {

                val onClick: () -> Unit = remember(provider) {
                    { onAttributeImportClick(provider) }
                }

                WalletAttributeCard(
                    modifier = Modifier
                        .clickable(
                            onClick = onClick
                        ),
                    borderColor = MaterialTheme.colors.walletAttributeImportBorder,
                    contentColor = MaterialTheme.colors.walletAttributeImportContentColor,
                    topContent = {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(provider.issuerName)
                                }
                            },
                            style = MaterialTheme.typography.body1
                        )
                    },
                    bottomContent = {
                        Text(
                            text = "Please logon via ${provider.issuerName}",
                            style = MaterialTheme.typography.caption,
                        )
                    },
                    trailingContent = {
                        val painter = painterResource(id = R.drawable.ic_arrow_right)
                        Icon(painter = painter, contentDescription = null)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AttributeImportScreenPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            AttributeImportScreen(
                modifier = Modifier.fillMaxSize(),
                attributeType = "Proof of being a student",
                credentialIssuers = listOf(
                    CredentialIssuer(
                        credentialId = "1",
                        issuerName = "eduID",
                        issueUrl = ""
                    ),
                    CredentialIssuer(
                        credentialId = "2",
                        issuerName = "Studielink",
                        issueUrl = ""
                    ),
                ),
                onBackClick = {},
                onAttributeImportClick = {},
            )
        }
    }
}