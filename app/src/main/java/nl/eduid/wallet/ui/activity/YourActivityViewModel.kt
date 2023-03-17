package nl.eduid.wallet.ui.activity

import android.util.Log
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.eduid.wallet.bridge.IrmaMobileBridge
import nl.eduid.wallet.model.config.AttributeType
import nl.eduid.wallet.model.config.IrmaConfiguration
import nl.eduid.wallet.model.config.Issuer
import nl.eduid.wallet.model.event.receive.DisclosedAttribute
import nl.eduid.wallet.model.event.receive.LogEntry
import nl.eduid.wallet.model.event.receive.LogEntryType
import nl.eduid.wallet.model.event.receive.LogsEvent
import nl.eduid.wallet.model.event.send.LoadLogsEvent
import nl.eduid.wallet.store.IrmaStore
import nl.eduid.wallet.ui.received.englishLanguageCode

class YourActivityViewModel @AssistedInject constructor(
    @Assisted initialState: YourActivityViewState,
    irmaStore: IrmaStore,
    private val bridge: IrmaMobileBridge,
) : MavericksViewModel<YourActivityViewState>(initialState) {

    private val configuration = irmaStore.irmaConfiguration
        .filterNotNull()

    private val logEntries = bridge
        .events
        .filterIsInstance<LogsEvent>()
        .map { list -> list.entries.filter { it.type == LogEntryType.Disclosing } }

    fun setup() {

        viewModelScope.launch {
            combine(configuration, logEntries) { config, entries ->
                entries
                    .map {
                        LogItem(
                            verifierName = it.serverRequestorInfo?.name?.copy?.get(
                                englishLanguageCode
                            ) ?: "unknown",
                            exchangeId = it.id.toString(),
                            formattedExchangeDateTime = logEntryTime(it),
                            sharedAttributes = sharedItems(it, config)
                        )
                    }
            }.collectLatest {
                setState { copy(logs = it) }
            }
        }

        loadLogEvents()
    }

    private fun logEntryTime(entry: LogEntry): String {

        val dateTime = Instant.fromEpochSeconds(entry.time)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val dayOfWeek = dateTime.dayOfWeek.toString().take(3)
            .lowercase()
            .replaceFirstChar { it.uppercase() }

        val month = dateTime.month.toString().take(3)
            .lowercase()
            .replaceFirstChar { it.uppercase() }

        // "Sat 22 Oct 2022 - 00:35"
        return "$dayOfWeek ${dateTime.dayOfMonth} $month ${dateTime.year} " +
            "- ${dateTime.hour}:${dateTime.minute}"
    }

    private fun loadLogEvents() {
        viewModelScope.launch {
            bridge.dispatchFromNative(
                "LoadLogsEvent",
                Json.encodeToString(LoadLogsEvent(null, 999))
            )
        }
    }

    private fun sharedItems(
        entry: LogEntry,
        config: IrmaConfiguration
    ): List<SharedItem> = entry.disclosedAttributes
        .map { attributes ->

            val badgeTitle = attributes
                .find { it.identifier == "irma-demo.surf.edubadge.badge_title" }
                ?.value
                ?.copy
                ?.get(englishLanguageCode)

            val badgeImageUrl = attributes
                .find { it.identifier == "irma-demo.surf.edubadge.badge_logo_url" }
                ?.value
                ?.copy
                ?.get(englishLanguageCode)

            val badgeIssuer = attributes
                .find { it.identifier == "irma-demo.surf.edubadge.badge_issuer" }
                ?.value
                ?.copy
                ?.get(englishLanguageCode)

            val issuerLogoUrl = attributes
                .find { it.identifier == "irma-demo.surf.edubadge.badge_issuer_logo_url" }
                ?.value
                ?.copy
                ?.get(englishLanguageCode)

            val issueDate = attributes
                .find { it.identifier == "irma-demo.surf.edubadge.badge_issued_on" }
                ?.value
                ?.copy
                ?.get(englishLanguageCode)
                ?.let {
                    try {
                        Instant.parse(it)
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date
                    } catch (ex: Exception) {
                        LocalDate(1999, 1, 1)
                    }
                }

            if (badgeTitle != null && badgeImageUrl != null && badgeIssuer != null && issuerLogoUrl != null && issueDate != null) {
                listOfNotNull(
                    attributes.find { it.identifier == "irma-demo.surf.edubadge.learner_id" }?.let { attribute ->
                        SharedItem.SharedAttribute(
                            name = attributeName(attribute.identifier, config.attributeTypes),
                            value = attribute.value.copy.get(englishLanguageCode) ?: "null",
                            issuer = issuerName(attribute, config.issuers)
                        )
                    },
                    SharedItem.EduBadge(
                        title = badgeTitle,
                        imageUrl = badgeImageUrl,
                        issuer = badgeIssuer,
                        issuerLogoUrl = issuerLogoUrl,
                        issueDate = issueDate
                    )
                )
            } else {
                attributes.map { attribute ->
                    SharedItem.SharedAttribute(
                        name = attributeName(attribute.identifier, config.attributeTypes),
                        value = attribute.value.copy.get(englishLanguageCode) ?: "null",
                        issuer = issuerName(attribute, config.issuers)
                    )
                }
            }
        }.flatten()

    private fun issuerName(
        attribute: DisclosedAttribute,
        issuers: Map<String, Issuer>
    ): String {
        return issuers[issuerId(attribute)]?.name?.copy?.get(englishLanguageCode)
            ?: "null"
    }

    private fun issuerId(attribute: DisclosedAttribute): String {
        val (schemeManagerId, issuerId) = attribute.identifier.split('.')
        return "$schemeManagerId.$issuerId"
    }

    private fun attributeName(
        attributeType: String,
        attributeTypes: Map<String, AttributeType>
    ): String =
        attributeTypes[attributeType]?.name?.copy?.get(englishLanguageCode)
            ?: attributeType

    fun onExchangeClick(item: LogItem) {
        setState {
            if (item.exchangeId == expandedExchange) {
                copy(expandedExchange = null)
            } else {
                copy(expandedExchange = item.exchangeId)
            }
        }
    }

    @AssistedFactory
    interface Factory :
        AssistedViewModelFactory<YourActivityViewModel, YourActivityViewState> {
        override fun create(state: YourActivityViewState): YourActivityViewModel
    }

    companion object :
        MavericksViewModelFactory<YourActivityViewModel, YourActivityViewState> by hiltMavericksViewModelFactory()

}