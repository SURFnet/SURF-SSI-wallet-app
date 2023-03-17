package nl.eduid.wallet.ui.activity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.composables.attribute.EduBadgeCredential
import nl.eduid.wallet.shared.presentation.composables.attribute.WalletAttributeReceivedWithTitle
import nl.eduid.wallet.shared.presentation.composables.button.PrimaryButton
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme

private val emptyLambda = {}

@Composable
fun YourActivityScreen(
    modifier: Modifier = Modifier,
    viewModel: YourActivityViewModel = mavericksViewModel(),
    onBackClick: () -> Unit,
) {

    val state by viewModel.collectAsState()

    LaunchedEffect(null) {
        viewModel.setup()
    }

    YourActivityScreen(
        modifier = modifier,
        entries = state.logs,
        expandedExchange = state.expandedExchange,
        onBackClick = onBackClick,
        onExchangeClick = viewModel::onExchangeClick
    )
}

private fun Modifier.contentPadding(): Modifier {
    return padding(horizontal = 32.dp)
}

@Composable
fun YourActivityScreen(
    modifier: Modifier = Modifier,
    entries: List<LogItem>,
    expandedExchange: String?,
    onBackClick: () -> Unit,
    onExchangeClick: (LogItem) -> Unit
) {

    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = true)
        onDispose {}
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(52.dp))

        Row(
            modifier = Modifier
                .contentPadding()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
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
            modifier = Modifier.contentPadding(),
            text = "Your Activity",
            style = MaterialTheme.typography.h5,
            color = Color(0xFF008738)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            for (entry in entries) {
                Column(
                    modifier = Modifier
                        .background(Color(0xFFECECEC))
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { onExchangeClick(entry) }
                            .padding(start = 32.dp)
                            .fillMaxWidth()
                            .heightIn(min = 82.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = entry.verifierName,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "on ${entry.formattedExchangeDateTime}",
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF979797)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))

                        val rotation by animateFloatAsState(targetValue = if (entry.exchangeId == expandedExchange) -90f else 90f)

                        Icon(
                            modifier = Modifier.rotate(rotation),
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(28.dp))
                    }
                    AnimatedVisibility(visible = entry.exchangeId == expandedExchange) {
                        Divider()
                        Column(
                            modifier = Modifier.contentPadding(),
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(
                                    id = R.string.attribute_exchange_id,
                                    entry.exchangeId ?: ""
                                ),
                                style = MaterialTheme.typography.body1,
                                color = Color(0xFF979797),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                                for (share in entry.sharedAttributes) {
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
                            PrimaryButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = emptyLambda
                            ) {
                                Text(text = "Request removal of this information")
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview
@Composable
fun YourActivityScreenPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            YourActivityScreen(
                modifier = Modifier.fillMaxSize(),
                entries = listOf(
                    LogItem(
                        verifierName = "Dominos",
                        exchangeId = "144",
                        formattedExchangeDateTime = "Sat 22 Oct 2022 - 14:35",
                        sharedAttributes = emptyList()
                    ),
                    LogItem(
                        verifierName = "Surf Spot",
                        exchangeId = "143",
                        formattedExchangeDateTime = "Sat 22 Oct 2022 - 14:35",
                        sharedAttributes = listOf(
                            SharedItem.SharedAttribute(
                                name = "Full name",
                                value = "Jonathan Jostar",
                                issuer = "Mijn Overheid"
                            ),
                            SharedItem.SharedAttribute(
                                name = "Date of birth",
                                value = "February 22, 1994",
                                issuer = "Mijn Overheid"
                            )
                        )
                    )
                ),
                expandedExchange = "143",
                onBackClick = {},
                onExchangeClick = {}
            )
        }
    }
}
