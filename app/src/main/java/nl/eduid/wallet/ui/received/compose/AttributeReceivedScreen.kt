package nl.eduid.wallet.ui.received.compose

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.datetime.LocalDate
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.composables.attribute.EduBadgeCredential
import nl.eduid.wallet.shared.presentation.composables.attribute.WalletAttributeReceivedWithTitle
import nl.eduid.wallet.shared.presentation.composables.button.PrimaryButton
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme
import nl.eduid.wallet.ui.received.AttributeReceivedViewModel
import nl.eduid.wallet.ui.received.IssuanceItem
import nl.eduid.wallet.ui.received.IssuedAttributeState
import nl.eduid.wallet.ui.shared.GenericErrorDialog
import java.time.Month

@Composable
fun AttributeReceivedScreen(
    modifier: Modifier = Modifier,
    viewModel: AttributeReceivedViewModel = mavericksViewModel(),
) {

    val state by viewModel.collectAsState()

    LaunchedEffect(null) {
        viewModel.setup()
    }

    AttributeReceivedScreen(
        modifier = modifier,
        heading = buildAnnotatedString {
            withStyle(SpanStyle(color = Color(0xFF008939))) {
                append("Your data")
            }
            append('\n')
            append("was retrieved successfully")

        },
        description = "The following information has been added to your Wallet and can now be shared.",
        attributes = state.attributes,
        onContinue = viewModel::onContinue
    )

    if (state.error) {
        GenericErrorDialog(viewModel::recoverFromErrorState)
    }
}

@Composable
fun AttributeReceivedScreen(
    modifier: Modifier = Modifier,
    heading: AnnotatedString,
    description: String,
    attributes: IssuedAttributeState,
    onContinue: () -> Unit,
) {

    val systemUiController = rememberSystemUiController()
    val isThemeLight = MaterialTheme.colors.isLight

    LaunchedEffect(null) {
        systemUiController.setStatusBarColor(Color.Transparent, isThemeLight)
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 38.dp)
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_wallet),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(54.dp))

        Text(
            text = heading,
            style = MaterialTheme.typography.h5,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.body1
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            val attributeModifier = Modifier.fillMaxWidth()

            when (attributes) {
                IssuedAttributeState.Loading -> {}
                is IssuedAttributeState.Loaded -> {
                    for (item in attributes.list) {
                        when (item) {
                            is IssuanceItem.EduBadge -> {
                                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                    EduBadgeCredential(
                                        title = item.title,
                                        imageUrl = item.imageUrl,
                                        issuer = item.issuer,
                                        issuerLogoUrl = item.issuerLogoUrl,
                                        issueDate = item.issueDate
                                    )
                                }
                            }
                            is IssuanceItem.IssuedAttribute -> {
                                WalletAttributeReceivedWithTitle(
                                    modifier = attributeModifier,
                                    title = item.name,
                                    attributeValue = item.value,
                                    issuer = item.issuer
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onContinue
        ) {
            Text(text = stringResource(R.string.button_continue))
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

}

@Preview
@Composable
fun AttributeReceivedScreenPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            AttributeReceivedScreen(
                modifier = Modifier.fillMaxSize(),
                heading = buildAnnotatedString { append("Title") },
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                attributes = IssuedAttributeState.Loaded(
                    listOf(
                        IssuanceItem.IssuedAttribute(
                            name = "Email Address",
                            value = "test@test.com",
                            issuer = "Test Inc."
                        )
                    )
                ),
                onContinue = {}
            )
        }
    }
}

@Preview
@Composable
fun AttributeReceivedEduBadgeScreenPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            AttributeReceivedScreen(
                modifier = Modifier.fillMaxSize(),
                heading = buildAnnotatedString { append("Title") },
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                attributes = IssuedAttributeState.Loaded(
                    listOf(
                        IssuanceItem.EduBadge(
                            title = "Advanced Food Physics - Reology and fracture of soft solids",
                            imageUrl = "",
                            issuer = "Wagenigen University & Research - Professional Education",
                            issuerLogoUrl = "",
                            issueDate = LocalDate(2022, Month.NOVEMBER, 21)
                        )
                    )
                ),
                onContinue = {}
            )
        }
    }
}
