package nl.eduid.wallet.ui.received

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
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import nl.eduid.wallet.bridge.IrmaMobileBridge
import nl.eduid.wallet.interactor.DidSessionSuccess
import nl.eduid.wallet.interactor.ProceedIssuanceSession
import nl.eduid.wallet.model.config.AttributeType
import nl.eduid.wallet.model.config.IrmaConfiguration
import nl.eduid.wallet.model.config.Issuer
import nl.eduid.wallet.model.config.LanguageCode
import nl.eduid.wallet.model.config.TranslatedValue
import nl.eduid.wallet.model.event.receive.AttributeId
import nl.eduid.wallet.model.event.receive.CredentialId
import nl.eduid.wallet.model.event.receive.FailureSessionEvent
import nl.eduid.wallet.model.event.receive.IssuedCredential
import nl.eduid.wallet.model.event.receive.RequestIssuancePermissionSessionEvent
import nl.eduid.wallet.model.event.receive.RequestVerificationPermissionSessionEvent
import nl.eduid.wallet.shared.presentation.navigation.NavCommands
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph
import nl.eduid.wallet.shared.presentation.navigation.navigateToAttributeRequest
import nl.eduid.wallet.shared.presentation.navigation.navigateToAttributesReceived
import nl.eduid.wallet.shared.presentation.navigation.navigateToHome
import nl.eduid.wallet.store.IrmaStore

val englishLanguageCode = LanguageCode("en")

class AttributeReceivedViewModel @AssistedInject constructor(
    @Assisted initialState: AttributeReceivedViewState,
    @Assisted private val sessionId: Long,
    private val irmaMobileBridge: IrmaMobileBridge,
    private val irmaStore: IrmaStore,
    private val didSessionSuccess: DidSessionSuccess,
    private val proceedIssuanceSession: ProceedIssuanceSession
) : MavericksViewModel<AttributeReceivedViewState>(initialState) {

    private val configuration = irmaStore.irmaConfiguration
        .filterNotNull()

    private val request: StateFlow<RequestIssuancePermissionSessionEvent?> =
        irmaStore
            .sessions
            .map { it.filterIsInstance<RequestIssuancePermissionSessionEvent>() }
            .map { sessions -> sessions.firstOrNull() { it.sessionId == sessionId } }
            .filterNotNull()
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val credentials = request
        .filterNotNull()
        .map { it.credentials }

    private val sessionFailure =
        irmaMobileBridge.events
            .filterIsInstance<FailureSessionEvent>()
            .filter { it.sessionId == sessionId }

    fun setup() {
        loadIssuedCredentials()
        collectSessionFailures()
        awaitSessionSuccess()
    }

    private fun loadIssuedCredentials() {
        viewModelScope.launch {
            combine(configuration, credentials, ::combineToViewState)
                .collectLatest {
                    setState { copy(attributes = IssuedAttributeState.Loaded(it)) }
                }
        }
    }

    private fun collectSessionFailures() = viewModelScope.launch {
        sessionFailure.collectLatest {
            setState { copy(error = true) }
        }
    }

    private fun awaitSessionSuccess() {
        viewModelScope.launch {
            didSessionSuccess(sessionId).first()
            irmaStore.removeSession(sessionId)
            navigateToNextScreen()
        }
    }

    private suspend fun navigateToNextScreen() {
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

    private fun combineToViewState(
        config: IrmaConfiguration,
        credentials: List<IssuedCredential>,
    ) = credentials
        .flatMap { credential ->
            if (isEduBadge(credential)) {

                val attributes = credential.attributes.value
                val title = attributes[AttributeId("irma-demo.surf.edubadge.badge_title")]?.copy?.get(englishLanguageCode)
                val imageUrl =
                    attributes[AttributeId("irma-demo.surf.edubadge.badge_logo_url")]?.copy?.get(englishLanguageCode)
                val issuer = attributes[AttributeId("irma-demo.surf.edubadge.badge_issuer")]?.copy?.get(englishLanguageCode)
                val issuerLogoUrl = attributes[AttributeId("irma-demo.surf.edubadge.badge_issuer_logo_url")]?.copy?.get(englishLanguageCode)
                val issueDate = attributes[AttributeId("irma-demo.surf.edubadge.badge_issued_on")]
                    ?.copy
                    ?.get(englishLanguageCode)
                    ?.let {
                        try {
                            Instant.parse(it)
                                .toLocalDateTime(TimeZone.currentSystemDefault()).date
                        } catch(ex: Exception) {
                            LocalDate(1999, 1, 1)
                        }
                    }

                if (title != null && imageUrl != null && issuer != null && issuerLogoUrl != null && issueDate != null) {
                    listOf(
                        IssuanceItem.EduBadge(
                            title = title,
                            imageUrl = imageUrl,
                            issuer = issuer,
                            issuerLogoUrl = issuerLogoUrl,
                            issueDate = issueDate
                        )
                    )
                } else {
                    // Fallback
                    toIssuedAttributes(
                        credential,
                        config.attributeTypes,
                        config.issuers,
                    )
                }
            } else {
                toIssuedAttributes(
                    credential,
                    config.attributeTypes,
                    config.issuers,
                )
            }
        }

    private fun isEduBadge(credential: IssuedCredential) =
        credential.schemeManagerId == "irma-demo" && credential.issuerId == "surf" && credential.id == CredentialId(
            "edubadge"
        )

    private fun toIssuedAttributes(
        credential: IssuedCredential,
        attributeTypes: Map<String, AttributeType>,
        issuers: Map<String, Issuer>,
    ): List<IssuanceItem> = credential.attributes.value
        .map { attribute ->
            credential.toIssuedAttributes(attribute, attributeTypes, issuers)
        }

    private fun IssuedCredential.toIssuedAttributes(
        attribute: Map.Entry<AttributeId, TranslatedValue?>,
        attributeTypes: Map<String, AttributeType>,
        issuers: Map<String, Issuer>
    ) = IssuanceItem.IssuedAttribute(
        name = attributeName(attributeTypes, attribute),
        value = attributeValue(attribute),
        issuer = issuerName(issuers, this)
    )

    private fun attributeValue(attribute: Map.Entry<AttributeId, TranslatedValue?>): String {
        return attribute.value?.copy?.get(englishLanguageCode)
            ?: attribute.value?.copy?.values?.firstOrNull()
            ?: "null"
    }

    private fun attributeName(
        attributeTypes: Map<String, AttributeType>,
        attribute: Map.Entry<AttributeId, TranslatedValue?>
    ): String {
        return attributeTypes[attribute.key.value]?.name?.copy?.get(englishLanguageCode)
            ?: attribute.key.value
    }

    private fun issuerName(
        issuers: Map<String, Issuer>,
        credential: IssuedCredential
    ): String {
        return issuers[issuerId(credential)]?.name?.copy?.get(englishLanguageCode)
            ?: credential.issuerId
    }

    private fun issuerId(credential: IssuedCredential) =
        "${credential.schemeManagerId}.${credential.issuerId}"

    fun recoverFromErrorState() {
        viewModelScope.launch {
            irmaStore.removeSession(sessionId)
            navigateToNextScreen()
        }
    }

    fun onContinue() {
        proceedIssuanceSession(sessionId)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            state: AttributeReceivedViewState,
            sessionId: Long
        ): AttributeReceivedViewModel
    }

    companion object :
        MavericksViewModelFactory<AttributeReceivedViewModel, AttributeReceivedViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: AttributeReceivedViewState
        ): AttributeReceivedViewModel {

            val fragment = (viewModelContext as FragmentViewModelContext)
                .fragment<AttributeReceivedFragment>()

            val sessionId = fragment.arguments
                ?.getLong(NavigationGraph.SESSION_ID_KEY)
                ?: throw IllegalStateException("IRMA session ID must be provided.")

            return fragment.viewModelFactory.create(state, sessionId)
        }
    }
}
