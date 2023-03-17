package nl.eduid.wallet.ui.request

import androidx.navigation.NavController
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.eduid.wallet.bridge.IrmaMobileBridge
import nl.eduid.wallet.interactor.DidSessionSuccess
import nl.eduid.wallet.interactor.ProceedDisclosureSession
import nl.eduid.wallet.model.config.AttributeType
import nl.eduid.wallet.model.config.IrmaConfiguration
import nl.eduid.wallet.model.event.receive.AttributeIdentifier
import nl.eduid.wallet.model.event.receive.DisclosureCandidate
import nl.eduid.wallet.model.event.receive.DisclosureChoices
import nl.eduid.wallet.model.event.receive.FailureSessionEvent
import nl.eduid.wallet.model.event.receive.RequestIssuancePermissionSessionEvent
import nl.eduid.wallet.model.event.receive.RequestVerificationPermissionSessionEvent
import nl.eduid.wallet.shared.presentation.navigation.NavCommands
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph
import nl.eduid.wallet.shared.presentation.navigation.navigateToAttributeImport
import nl.eduid.wallet.shared.presentation.navigation.navigateToAttributeRequest
import nl.eduid.wallet.shared.presentation.navigation.navigateToAttributesReceived
import nl.eduid.wallet.shared.presentation.navigation.navigateToAttributesShared
import nl.eduid.wallet.shared.presentation.navigation.navigateToHome
import nl.eduid.wallet.store.IrmaStore
import nl.eduid.wallet.ui.received.englishLanguageCode

class AttributeRequestViewModel @AssistedInject constructor(
    private val irmaStore: IrmaStore,
    @Assisted initialState: AttributeRequestViewState,
    @Assisted private val sessionId: Long,
    private val irmaMobileBridge: IrmaMobileBridge,
    private val proceedDisclosureSession: ProceedDisclosureSession,
    private val didSessionSuccess: DidSessionSuccess,
) : MavericksViewModel<AttributeRequestViewState>(initialState) {

    private val configuration = irmaStore.irmaConfiguration
        .filterNotNull()

    private val request: StateFlow<RequestVerificationPermissionSessionEvent?> =
        irmaStore
            .sessions
            .map { it.filterIsInstance<RequestVerificationPermissionSessionEvent>() }
            .map { sessions -> sessions.firstOrNull { it.sessionId == sessionId } }
            .filterNotNull()
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val sessionFailure =
        irmaMobileBridge.events
            .filterIsInstance<FailureSessionEvent>()
            .filter { it.sessionId == sessionId }

    fun setup() {
        awaitSessionSuccess()
        collectSessionFailures()
        loadRequestAttribute()
    }

    private fun awaitSessionSuccess() {
        viewModelScope.launch {
            didSessionSuccess(sessionId).first()
            irmaStore.removeSession(sessionId)
            navigateToNextScreen()
        }
    }

    private suspend fun navigateToNextScreen() {
        NavCommands.commands.emit {
            navigateToAttributesShared(sessionId)
        }
    }

    private fun collectSessionFailures() = viewModelScope.launch {
        sessionFailure.collectLatest {
            setState { copy(error = true) }
        }
    }

    private fun loadRequestAttribute() {
        viewModelScope.launch {
            combine(request.filterNotNull(), configuration) { request, configuration ->
                request.serverName() to request.attributeRequests(configuration)
            }.collectLatest { (serverName, attributes) ->
                setState {
                    copy(
                        verifierName = serverName,
                        requestedAttributes = attributes
                    )
                }
            }
        }
    }

    private fun RequestVerificationPermissionSessionEvent.attributeRequests(configuration: IrmaConfiguration): List<AttributeRequest> {

        val requests = disclosures
            .mapIndexed { disIndex, disjunctions ->

                val label: String? = disclosuresLabels?.get(disIndex)?.copy?.get(englishLanguageCode)

                when {
                    label != null -> {

                        val inWallet = disclosuresCandidates[disIndex]
                            .any { it.firstOrNull { it.isInWallet } != null }

                        // Create one AttributeRequest for a disjunction using the label
                        AttributeRequest(
                            id = "$disIndex+${disjunctions.flatMap { conjunction -> conjunction.map { it } }.joinToString(",") { it }}",
                            name = label,
                            inWallet = inWallet,
                            disjunctionIndex = disIndex,
                        ).let { listOf(it) }
                    }
                    disjunctions.size > 1 -> {

                        val inWallet = disclosuresCandidates[disIndex]
                            .any { it.firstOrNull { it.isInWallet } != null }

                        // Create one AttributeRequest for a disjunction using the first candidate's display name
                        AttributeRequest(
                            id = "$disIndex+${disjunctions.flatMap { conjunction -> conjunction.map { it } }.joinToString(",") { it }}",
                            name = attributeName(disjunctions.first().first(), configuration.attributeTypes),
                            inWallet = inWallet,
                            disjunctionIndex = disIndex,
                        ).let { listOf(it) }
                    }
                    else -> {

                        // Show all individual attributes from the conjunction
                        disjunctions.flatMapIndexed { conIndex, conjunctions ->
                            conjunctions.map { attributeType ->

                                val inWallet = disclosuresCandidates
                                    .get(disIndex)
                                    .get(conIndex)
                                    .firstOrNull { it.isInWallet }
                                    ?.let { true }
                                    ?: false

                                AttributeRequest(
                                id = "$disIndex+${attributeType}",
                                name = attributeName(attributeType, configuration.attributeTypes),
                                inWallet = inWallet,
                                disjunctionIndex = disIndex,
                            )
                            }
                        }
                    }
                }
            }
            .flatten()

        return requests
    }

    private val DisclosureCandidate.isInWallet: Boolean
        get() = (credentialHash != null && credentialHash.isNotEmpty())
            && !revoked && !expired

    private fun attributeName(
        attributeType: String,
        attributeTypes: Map<String, AttributeType>
    ): String {
        return attributeTypes[attributeType]?.name?.copy?.get(englishLanguageCode)
            ?: attributeType
    }

    private fun RequestVerificationPermissionSessionEvent.serverName(): String {
        return serverName.name.copy[englishLanguageCode]
            ?: serverName.hostnames.first()
    }

    fun onUnsatisfiedAttributeClicked(request: AttributeRequest) {
        viewModelScope.launch {
            NavCommands.commands.emit {
                navigateToAttributeImport(sessionId, request.disjunctionIndex)
            }
        }
    }

    fun disclose() {
        withState { state ->

            val canProceed = state.requestedAttributes.isNotEmpty() &&
                state.requestedAttributes.all { it.inWallet }

            viewModelScope.launch {
                if (canProceed) {

                    val event = request.filterNotNull().first()
                    val choices = event.disclosuresCandidates
                        .map {
                            it.first { choices -> choices.firstOrNull { candidate -> candidate.credentialHash != "" } != null }
                        }
                        .map { candidates ->
                            candidates.map { AttributeIdentifier(it.type, it.credentialHash!!) }
                        }

                    proceedDisclosureSession(event.sessionId, DisclosureChoices(choices))
                }
            }
        }
    }

    fun recoverFromErrorState() {
        viewModelScope.launch {
            irmaStore.removeSession(sessionId)

            val sessions = irmaStore.sessions.value
            if (sessions.isNotEmpty()) {
                when (val session = sessions.first()) {
                    is RequestIssuancePermissionSessionEvent -> {
                        NavCommands.commands.emit { navigateToAttributesReceived(session.sessionId) }
                    }
                    is RequestVerificationPermissionSessionEvent -> {
                        NavCommands.commands.emit { navigateToAttributeRequest(session.sessionId) }
                    }
                }
            } else {
                NavCommands.commands.emit(NavController::navigateToHome)
            }
        }
    }


    @AssistedFactory
    interface Factory {
        fun create(
            state: AttributeRequestViewState,
            sessionId: Long
        ): AttributeRequestViewModel
    }

    companion object :
        MavericksViewModelFactory<AttributeRequestViewModel, AttributeRequestViewState> {

        override fun create(
            viewModelContext: ViewModelContext,
            state: AttributeRequestViewState
        ): AttributeRequestViewModel {

            val fragment = (viewModelContext as FragmentViewModelContext)
                .fragment<AttributeRequestFragment>()

            val sessionId = fragment.arguments
                ?.getLong(NavigationGraph.SESSION_ID_KEY)
                ?: throw IllegalStateException("IRMA session ID must be provided.")

            return fragment.viewModelFactory.create(state, sessionId)
        }
    }
}
