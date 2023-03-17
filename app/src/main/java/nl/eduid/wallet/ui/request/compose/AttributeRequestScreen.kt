package nl.eduid.wallet.ui.request.compose

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.composables.attribute.WalletAttributeRequest
import nl.eduid.wallet.shared.presentation.composables.attribute.WalletAttributeRequestSatisfied
import nl.eduid.wallet.shared.presentation.composables.button.PrimaryButton
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme
import nl.eduid.wallet.shared.presentation.compose.theme.nunitoFamily
import nl.eduid.wallet.ui.request.AttributeRequest
import nl.eduid.wallet.ui.request.AttributeRequestViewState
import nl.eduid.wallet.ui.shared.GenericErrorDialog

@Composable
fun AttributeRequestScreen(
    modifier: Modifier = Modifier,
    state: AttributeRequestViewState,
    onAttributeClick: (AttributeRequest) -> Unit,
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

        if (state.verifierName != null) {
            Column(
                modifier = horizontalPaddingModifier
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
                    text = "Sharing request",
                    style = MaterialTheme.typography.h4
                )

                Text(
                    text = buildAnnotatedString {

                        withStyle(SpanStyle(color = Color(0xFF008738))) {
                            append(state.verifierName)
                        }

                        append(" would like to receive")
                    },
                    fontFamily = nunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

                    for (request in state.requestedAttributes) {
                        if (request.inWallet) {
                            WalletAttributeRequestSatisfied(
                                attributeType = request.name,
                            )
                        } else {

                            val onClick: () -> Unit = remember(request) {
                                { onAttributeClick(request) }
                            }

                            WalletAttributeRequest(
                                attributeType = buildAnnotatedString {
                                    append(request.name)
                                },
                                onClick = onClick
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = buildAnnotatedString {
                        val boldStyle = SpanStyle(fontWeight = FontWeight.Bold)

                        withStyle(boldStyle) {
                            append("Tip")
                        }

                        append(": You can click on the cards to see exactly ")

                        withStyle(boldStyle) {
                            append("what information, provided by which source")
                        }

                        append(" will be shared.")
                    }
                )

            }
        }

        PrimaryButton(
            modifier = horizontalPaddingModifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp),
            onClick = onContinue,
            enabled = !state.loading && state.requestedAttributes.all { it.inWallet }
        ) {
            Text(text = stringResource(R.string.button_share_attributes))
        }
    }
}

@Preview
@Composable
fun AttributeReceivedScreenPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            AttributeRequestScreen(
                modifier = Modifier.fillMaxSize(),
                state = AttributeRequestViewState(
                    verifierName = "Dominos",
                    requestedAttributes = listOf(
                        AttributeRequest(
                            id = "irma-demo.pbdf.surfnet-2.fullname",
                            name = "Your full name",
                            inWallet = true,
                            disjunctionIndex = 0
                        ),
                        AttributeRequest(
                            id = "irma-demo.pbdf.surfnet-2.email",
                            name = "Your email address",
                            inWallet = true,
                            disjunctionIndex = 0
                        ),
                        AttributeRequest(
                            id = "irma-demo.pbdf.surfnet-2.type",
                            name = "Proof of being a student",
                            inWallet = false,
                            disjunctionIndex = 1
                        )
                    ),
                ),
                onAttributeClick = {},
                onContinue = {}
            )
        }
    }
}