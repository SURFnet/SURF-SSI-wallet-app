package nl.eduid.wallet.ui.shared

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
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
import nl.eduid.wallet.model.event.receive.LogsEvent
import nl.eduid.wallet.model.event.send.LoadLogsEvent
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph
import nl.eduid.wallet.store.IrmaStore
import nl.eduid.wallet.ui.received.englishLanguageCode

class AttributeSharedViewModel @AssistedInject constructor(
    irmaStore: IrmaStore,
    @Assisted initialState: AttributeSharedViewState,
    @Assisted private val sessionId: Long,
    private val bridge: IrmaMobileBridge,
) : MavericksViewModel<AttributeSharedViewState>(initialState) {

    private val configuration = irmaStore.irmaConfiguration
        .filterNotNull()

    private val logEntries = bridge
        .events
        .filterIsInstance<LogsEvent>()
        .map { it.entries }

    @Suppress("UNREACHABLE_CODE")
    fun setup() {
        loadLogEntryForSessionId(sessionId)
        loadLogEvents()
    }

    private fun loadLogEntryForSessionId(sessionId: Long) {
        viewModelScope.launch {
            combine(configuration, logEntries) { config, entries ->
                config to entries.first()
            }.collectLatest { (config, entry) ->
                setState {
                    copy(
                        verifierName = verifierName(entry),
                        exchangeId = sessionId.toString(),
                        formattedExchangeDateTime = logEntryTime(entry),
                        sharedAttributes = sharedItems(entry, config)
                    )
                }
            }
        }
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
                Json.encodeToString(LoadLogsEvent(null, 1))
            )
        }
    }

    private fun verifierName(entry: LogEntry) =
        entry.serverRequestorInfo?.name?.copy?.get(englishLanguageCode)
            ?: "unknown"

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

    @AssistedFactory
    interface Factory {
        fun create(
            state: AttributeSharedViewState,
            sessionId: Long
        ): AttributeSharedViewModel
    }

    companion object :
        MavericksViewModelFactory<AttributeSharedViewModel, AttributeSharedViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: AttributeSharedViewState
        ): AttributeSharedViewModel {

            val fragment = (viewModelContext as FragmentViewModelContext)
                .fragment<AttributeSharedFragment>()

            val arguments = fragment.arguments
            requireNotNull(arguments)
            require(arguments.containsKey(NavigationGraph.SESSION_ID_KEY))
            val sessionId = arguments
                .getLong(NavigationGraph.SESSION_ID_KEY, -1)

            return fragment.viewModelFactory.create(state, sessionId)
        }
    }
}
