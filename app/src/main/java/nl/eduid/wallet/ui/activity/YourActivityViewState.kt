package nl.eduid.wallet.ui.activity

import com.airbnb.mvrx.MavericksState
import kotlinx.datetime.LocalDate

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

data class LogItem(
    val verifierName: String,
    val exchangeId: String,
    val formattedExchangeDateTime: String,
    val sharedAttributes: List<SharedItem>,
)

data class YourActivityViewState(
    val logs: List<LogItem> = emptyList(),
    val expandedExchange: String? = null
): MavericksState