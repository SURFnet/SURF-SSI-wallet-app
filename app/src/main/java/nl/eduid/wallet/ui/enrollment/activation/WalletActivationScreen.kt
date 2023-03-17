package nl.eduid.wallet.ui.enrollment.activation

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme
import nl.eduid.wallet.ui.received.IssuanceItem
import nl.eduid.wallet.ui.received.IssuedAttributeState
import nl.eduid.wallet.ui.received.compose.AttributeReceivedScreen
import nl.eduid.wallet.ui.shared.GenericErrorDialog

@Composable
fun WalletActivationScreen(
    modifier: Modifier = Modifier,
    viewModel: WalletActivationViewModel = mavericksViewModel(),
) {

    val state by viewModel.collectAsState()

    LaunchedEffect(null) {
        viewModel.setup()
    }

    WalletActivationScreen(
        modifier = modifier,
        attributes = state.attributes,
        onContinue = viewModel::enroll
    )

    if (state.error) {
        GenericErrorDialog(viewModel::recoverFromErrorState)
    }
}

@Composable
fun WalletActivationScreen(
    modifier: Modifier = Modifier,
    attributes: IssuedAttributeState,
    onContinue: () -> Unit
) {
    AttributeReceivedScreen(
        modifier = modifier,
        heading = buildAnnotatedString {
            withStyle(SpanStyle(color = Color(0xFF008939))) {
                append(stringResource(R.string.wallet_activated_heading_part_1))
            }
            append('\n')
            append(stringResource(R.string.wallet_activated_heading_part_2))
        },
        description = stringResource(R.string.wallet_activated_body),
        attributes = attributes,
        onContinue = onContinue
    )
}

@Preview
@Composable
fun WalletActivationScreenPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            WalletActivationScreen(
                attributes = IssuedAttributeState.Loaded(
                    listOf(
                        IssuanceItem.IssuedAttribute(
                            name = "Your full name",
                            value = "René v. Hamersdonk",
                            issuer = "Universiteit van Amsterdam"
                        ),
                        IssuanceItem.IssuedAttribute(
                            name = "Proof of being a student",
                            value = "\uD83E\uDDD1\u200D\uD83C\uDF93 Student",
                            issuer = "Universiteit van Amsterdam"
                        ),
                        IssuanceItem.IssuedAttribute(
                            name = "Your institution",
                            value = "\u200D\uD83C\uDFDB️ Universiteit van Amsterdam",
                            issuer = "Universiteit van Amsterdam"
                        )
                    )
                ),
                onContinue = {}
            )
        }
    }
}