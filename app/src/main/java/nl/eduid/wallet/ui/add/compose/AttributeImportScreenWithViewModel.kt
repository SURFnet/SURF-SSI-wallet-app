package nl.eduid.wallet.ui.add.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import nl.eduid.wallet.ui.add.AttributeImportViewModel
import nl.eduid.wallet.ui.add.CredentialIssuer

@Composable
fun AttributeImportScreenWithViewModel(
    modifier: Modifier = Modifier,
    viewModel: AttributeImportViewModel = mavericksViewModel(),
    onAttributeImportClick: (CredentialIssuer) -> Unit,
    onBackClick: () -> Unit
) {

    val state by viewModel.collectAsState()

    LaunchedEffect(null) {
        viewModel.setup()
    }

    AttributeImportScreen(
        modifier = modifier,
        attributeType = state.attributeType,
        credentialIssuers = state.credentialIssuers,
        onBackClick = onBackClick,
        onAttributeImportClick = onAttributeImportClick
    )
}
