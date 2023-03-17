package nl.eduid.wallet.ui.enrollment.activation

import com.airbnb.mvrx.MavericksState
import nl.eduid.wallet.ui.received.IssuedAttributeState

data class WalletActivationViewState(
    val attributes: IssuedAttributeState = IssuedAttributeState.Loading,
    val error: Boolean = false,
) : MavericksState