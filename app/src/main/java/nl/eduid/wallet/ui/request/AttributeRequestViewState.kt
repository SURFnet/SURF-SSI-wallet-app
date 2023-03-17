package nl.eduid.wallet.ui.request

import com.airbnb.mvrx.MavericksState

data class AttributeRequestViewState(
    val verifierName: String? = null,
    val requestedAttributes: List<AttributeRequest> = emptyList(),
    val loading: Boolean = false,
    val error: Boolean = false,
) : MavericksState

data class AttributeRequest(
    val id: String,
    val name: String,
    val inWallet: Boolean,
    val disjunctionIndex: Int
)