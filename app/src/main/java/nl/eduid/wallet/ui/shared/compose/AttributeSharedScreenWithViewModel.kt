package nl.eduid.wallet.ui.shared.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.mvrx.compose.collectAsState
import nl.eduid.wallet.ui.shared.AttributeSharedViewModel

@Composable
fun AttributeSharedScreenWithViewModel(
    modifier: Modifier = Modifier,
    viewModel: AttributeSharedViewModel,
    onContinue: () -> Unit,
) {

    val state by viewModel.collectAsState()

    LaunchedEffect(null) {
        viewModel.setup()
    }

    AttributeSharedScreen(
        modifier = modifier,
        verifierName = state.verifierName,
        exchangeId = state.exchangeId,
        exchangeDataTime = state.formattedExchangeDateTime,
        sharedAttributes = state.sharedAttributes,
        onContinue = onContinue
    )
}