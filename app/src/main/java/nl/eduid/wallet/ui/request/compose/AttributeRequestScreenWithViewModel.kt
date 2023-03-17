package nl.eduid.wallet.ui.request.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.mvrx.compose.collectAsState
import nl.eduid.wallet.ui.request.AttributeRequest
import nl.eduid.wallet.ui.request.AttributeRequestViewModel
import nl.eduid.wallet.ui.shared.GenericErrorDialog

@Composable
fun AttributeRequestScreenWithViewModel(
    modifier: Modifier = Modifier,
    viewModel: AttributeRequestViewModel,
) {

    val state by viewModel.collectAsState()

    LaunchedEffect(null) {
        viewModel.setup()
    }

    AttributeRequestScreen(
        modifier = modifier,
        state = state,
        onAttributeClick = viewModel::onUnsatisfiedAttributeClicked,
        onContinue = viewModel::disclose
    )

    if (state.error) {
        GenericErrorDialog(viewModel::recoverFromErrorState)
    }
}