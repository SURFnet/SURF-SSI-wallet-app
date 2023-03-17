package nl.eduid.wallet.ui.wallet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.composables.button.PrimaryButton
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeRequestSatisfiedBorder

private val emptyLambda = {}

@Composable
fun YourDataScreen(
    modifier: Modifier = Modifier,
    viewModel: YourDataViewModel = mavericksViewModel(),
    onBackClick: () -> Unit
) {

    val state by viewModel.collectAsState()

    LaunchedEffect(key1 = null) {
        viewModel.setup()
    }

    YourDataScreen(
        modifier = modifier,
        sections = state.sections,
        onBackClick = onBackClick,
        onWalletItemClick = viewModel::onWalletItemClick
    )
}

@Composable
fun YourDataScreen(
    modifier: Modifier = Modifier,
    sections: List<WalletSection>,
    onBackClick: () -> Unit,
    onWalletItemClick: (WalletItem) -> Unit
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
            text = "Your wallet contains",
            style = MaterialTheme.typography.h5
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "the following information",
            style = MaterialTheme.typography.h5,
            color = Color(0xFF008738)
        )

        Spacer(modifier = Modifier.height(20.dp))

        for (section in sections) {
            Column {
                Text(
                    modifier = Modifier,
                    text = section.name,
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    for (item in section.items) {
                        WalletItem(
                            modifier = Modifier.fillMaxWidth(),
                            item = item,
                            onClick = onWalletItemClick
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
        
        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = emptyLambda
        ) {
            Text(text = "Add information to your wallet")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun WalletItem(
    modifier: Modifier = Modifier,
    item: WalletItem,
    onClick: (WalletItem) -> Unit
) {
    Card(
        modifier = modifier
            .heightIn(min = 71.dp)
            .clickable { onClick(item) },
        shape = RoundedCornerShape(6.dp),
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface,
        border = BorderStroke(1.dp, MaterialTheme.colors.walletAttributeRequestSatisfiedBorder)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            val painter = painterResource(id = R.drawable.ic_arrow_right)
            Icon(
                painter = painter,
                contentDescription = null,
                tint = Color(0xFF0062B0)
            )
        }
    }
}

@Preview
@Composable
fun WalletScreenPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            YourDataScreen(
                modifier = Modifier.fillMaxSize(),
                sections = listOf(
                    WalletSection(
                        name = "About you",
                        items = listOf(
                            WalletItem(
                                name = "Your full name"
                            ),
                            WalletItem(
                                name = "Your date of birth"
                            )
                        )
                    ),
                    WalletSection(
                        name = "About your studies",
                        items = listOf(
                            WalletItem(
                                name = "Proof of masters degree"
                            ),
                            WalletItem(
                                name = "Your graduation date"
                            )
                        )
                    )
                ),
                onBackClick = {},
                onWalletItemClick = {}
            )
        }
    }
}