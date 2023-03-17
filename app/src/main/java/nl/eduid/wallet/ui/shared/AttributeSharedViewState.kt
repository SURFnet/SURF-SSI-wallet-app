package nl.eduid.wallet.ui.shared

import com.airbnb.mvrx.MavericksState
import kotlinx.datetime.LocalDate
import nl.eduid.wallet.ui.received.IssuanceItem

data class AttributeSharedViewState(
    val verifierName: String? = null,
    val exchangeId: String? = null,
    val formattedExchangeDateTime: String? = null,
    val sharedAttributes: List<SharedItem> = emptyList(),
) : MavericksState

sealed interface SharedItem {

    data class SharedAttribute(
        val name: String,
        val value: String,
        val issuer: String
    ) : SharedItem

    data class EduBadge(
        val title: String,
        val imageUrl: String,
        val issuer: String,
        val issuerLogoUrl: String,
        val issueDate: LocalDate
    ) : SharedItem

}