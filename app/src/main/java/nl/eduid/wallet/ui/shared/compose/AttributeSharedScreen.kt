package nl.eduid.wallet.ui.shared.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
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
import nl.eduid.wallet.shared.presentation.composables.attribute.EduBadgeCredential
import nl.eduid.wallet.shared.presentation.composables.attribute.WalletAttributeReceivedWithTitle
import nl.eduid.wallet.shared.presentation.composables.button.PrimaryButton
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme
import nl.eduid.wallet.ui.shared.SharedItem

@Composable
fun AttributeSharedScreen(
    modifier: Modifier = Modifier,
    verifierName: String?,
    exchangeId: String?,
    exchangeDataTime: String?,
    sharedAttributes: List<SharedItem>,
    onContinue: () -> Unit,
) {

    val systemUiController = rememberSystemUiController()
    val isThemeLight = MaterialTheme.colors.isLight

    LaunchedEffect(null) {
        systemUiController.setStatusBarColor(Color.Transparent, isThemeLight)
    }

    val horizontalPaddingModifier =
        Modifier
            .padding(horizontal = 36.dp)

    Box(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            Row(
                modifier = horizontalPaddingModifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_wallet),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(54.dp))

            Text(
                modifier = horizontalPaddingModifier,
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Color(0xFF008738))) {
                        append(stringResource(R.string.attribute_shared_header_part_1))
                    }
                    append('\n')
                    append(stringResource(R.string.attribute_shared_header_part_2))
                },
                style = MaterialTheme.typography.h5
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = horizontalPaddingModifier,
                text = buildAnnotatedString {
                    append(stringResource(R.string.attribute_shared_body))
                    append(' ')
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(verifierName ?: "")
                    }
                },
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFECECEC))
            ) {
                Column(horizontalPaddingModifier) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(
                            id = R.string.attribute_exchange_id,
                            exchangeId ?: ""
                        ),
                        style = MaterialTheme.typography.body1,
                        color = Color(0xFF979797),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(
                            id = R.string.attribute_exchange_date,
                            exchangeDataTime ?: ""
                        ),
                        style = MaterialTheme.typography.body1,
                        color = Color(0xFF979797),
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        for (share in sharedAttributes) {
                            when (share) {
                                is SharedItem.EduBadge -> {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        EduBadgeCredential(
                                            title = share.title,
                                            imageUrl = share.imageUrl,
                                            issuer = share.issuer,
                                            issuerLogoUrl = share.issuerLogoUrl,
                                            issueDate = share.issueDate
                                        )
                                    }
                                }
                                is SharedItem.SharedAttribute -> {
                                    WalletAttributeReceivedWithTitle(
                                        title = share.name,
                                        attributeValue = share.value,
                                        issuer = share.issuer
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = horizontalPaddingModifier,
                text = stringResource(R.string.hint_information_not_stored)
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                modifier = horizontalPaddingModifier
                    .fillMaxWidth()
                    .padding(bottom = 60.dp),
                onClick = onContinue
            ) {
                Text(text = stringResource(R.string.button_got_it))
            }
        }
    }

}

@Preview
@Composable
fun AttributeReceivedScreenPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            AttributeSharedScreen(
                modifier = Modifier.fillMaxSize(),
                verifierName = "dominos",
                exchangeId = "145",
                exchangeDataTime = "Sat 22 Oct 2022 - 00:35",
                sharedAttributes = listOf(
                    SharedItem.SharedAttribute(
                        name = "Proof of being a student",
                        value = "Student",
                        issuer = "Universiteit van Amsterdam"
                    )
                )
            ) {}
        }
    }
}