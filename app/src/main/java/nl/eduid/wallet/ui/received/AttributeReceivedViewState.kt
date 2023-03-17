package nl.eduid.wallet.ui.received

import com.airbnb.mvrx.MavericksState
import kotlinx.datetime.LocalDate

sealed interface IssuanceItem {

    data class IssuedAttribute(
        val name: String,
        val value: String,
        val issuer: String,
    ) : IssuanceItem

    data class EduBadge(
        val title: String,
        val imageUrl: String,
        val issuer: String,
        val issuerLogoUrl: String,
        val issueDate: LocalDate
    ) : IssuanceItem
}


sealed interface IssuedAttributeState {
    object Loading : IssuedAttributeState
    @JvmInline value class Loaded(val list: List<IssuanceItem>) : IssuedAttributeState
}

data class AttributeReceivedViewState(
    val attributes: IssuedAttributeState = IssuedAttributeState.Loading,
    val error: Boolean = false,
) : MavericksState
