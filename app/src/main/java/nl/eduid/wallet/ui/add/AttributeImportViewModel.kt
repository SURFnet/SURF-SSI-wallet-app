package nl.eduid.wallet.ui.add

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import nl.eduid.wallet.model.config.AttributeType
import nl.eduid.wallet.model.config.IrmaConfiguration
import nl.eduid.wallet.model.config.Issuer
import nl.eduid.wallet.model.event.receive.DisclosureCandidate
import nl.eduid.wallet.model.event.receive.RequestVerificationPermissionSessionEvent
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph
import nl.eduid.wallet.store.IrmaStore
import nl.eduid.wallet.ui.received.englishLanguageCode

class AttributeImportViewModel @AssistedInject constructor(
    @Assisted initialState: AttributeImportViewState,
    @Assisted sessionId: Long,
    @Assisted private val disjunctionIndex: Int,
    irmaStore: IrmaStore
) : MavericksViewModel<AttributeImportViewState>(initialState) {

    private val irmaConfiguration = irmaStore.irmaConfiguration
        .filterNotNull()

    private val session = irmaStore
        .sessions
        .map { it.filterIsInstance<RequestVerificationPermissionSessionEvent>() }
        .map { sessions -> sessions.firstOrNull { it.sessionId == sessionId } }
        .filterNotNull()

    private val disclosureCandidates = session
        .map { it.disclosuresCandidates[disjunctionIndex] }

    fun setup() {

        viewModelScope.launch {
            session
                .map { it.disclosuresLabels?.get(disjunctionIndex)?.copy?.get(englishLanguageCode) }
                .flatMapLatest { label -> label?.let { flowOf(it) } ?: flowOf("Credential") }
                .collectLatest { setState { copy(attributeType = it) } }
        }

        viewModelScope.launch {
            combine(
                disclosureCandidates,
                irmaConfiguration,
                ::combineToAttributeIssuers
            ).collectLatest { issuers ->
                setState {
                    copy(
                        credentialIssuers = issuers
                    )
                }
            }
        }
    }

    private fun firstCredentialName() =
        combine(disclosureCandidates, irmaConfiguration) { candidates, configuration ->

            val candidate = candidates.first().first()

            val credentialId = candidate.type
                .split('.')
                .take(3)
                .joinToString(separator = ".")

            configuration.credentialTypes[credentialId]
                ?.name
                ?.copy
                ?.get(englishLanguageCode)
                ?: credentialId
        }

    private fun combineToAttributeIssuers(
        candidates: List<List<DisclosureCandidate>>,
        config: IrmaConfiguration
    ): List<CredentialIssuer> {

        val issuers = candidates
            .map { it.first() }
            .map { candidate ->

                // 1. Get issue URL
                val credentialId = candidate.type
                    .split('.')
                    .take(3)
                    .joinToString(separator = ".")

                // 2. Get the issue URL for credential using the config
                val issueUrl = config.credentialTypes[credentialId]
                    ?.issueUrl
                    ?.copy
                    ?.get(englishLanguageCode)
                    ?: throw IllegalStateException("Credential must have an IssueURL")

                // 3. Get issuer name
                val issuerName = issuerName(config.issuers, candidate)

                CredentialIssuer(
                    credentialId = credentialId,
                    issuerName = issuerName,
                    issueUrl = issueUrl
                )
            }

        return issuers
    }

    private fun issuerName(
        issuers: Map<String, Issuer>,
        candidate: DisclosureCandidate
    ): String {
        return issuers[issuerId(candidate)]?.name?.copy?.get(englishLanguageCode)
            ?: throw IllegalStateException("Candidate must have a issuer name")
    }

    private fun issuerId(candidate: DisclosureCandidate): String {
        return candidate.type
                    .split('.')
                    .take(2)
                    .joinToString(separator = ".")
    }

    @AssistedFactory
    interface Factory {
        fun create(
            state: AttributeImportViewState,
            sessionId: Long,
            disjunctionIndex: Int
        ): AttributeImportViewModel
    }

    companion object :
        MavericksViewModelFactory<AttributeImportViewModel, AttributeImportViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: AttributeImportViewState
        ): AttributeImportViewModel {

            val fragment = (viewModelContext as FragmentViewModelContext)
                .fragment<AttributeImportFragment>()

            val sessionId = fragment.arguments
                ?.getLong(NavigationGraph.SESSION_ID_KEY)
                ?: throw IllegalStateException("IRMA session ID must be provided.")

            val disjunctionIndex = fragment.arguments
                ?.getInt(NavigationGraph.DISJUNCTION_INDEX_KEY)
                ?: throw IllegalStateException("Disjunction index must be provided.")

            return fragment.viewModelFactory.create(state, sessionId, disjunctionIndex)
        }
        }
}
