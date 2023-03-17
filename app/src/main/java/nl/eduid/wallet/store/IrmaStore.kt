package nl.eduid.wallet.store

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import nl.eduid.wallet.model.config.IrmaConfiguration
import nl.eduid.wallet.model.event.receive.EnrollmentStatusEvent
import nl.eduid.wallet.model.event.receive.IrmaSessionEvent
import nl.eduid.wallet.model.event.receive.RawCredential
import nl.eduid.wallet.model.event.receive.RequestIssuancePermissionSessionEvent
import nl.eduid.wallet.model.event.receive.RequestVerificationPermissionSessionEvent
import javax.inject.Inject
import javax.inject.Singleton

sealed interface CredentialsState {
    object Loading : CredentialsState
    @JvmInline value class Loaded(val credentials: List<RawCredential>) : CredentialsState
}

sealed interface IrmaSchemeEnrollmentState {
    object Loading : IrmaSchemeEnrollmentState
    @JvmInline value class Loaded(val event: EnrollmentStatusEvent) : IrmaSchemeEnrollmentState
}

@Singleton
class IrmaStore @Inject constructor() {

    val irmaConfiguration = MutableStateFlow<IrmaConfiguration?>(null)

    private val _sessions = MutableStateFlow<List<IrmaSessionEvent>>(emptyList())
    val sessions: StateFlow<List<IrmaSessionEvent>>
        get() = _sessions

    val enrollment = MutableStateFlow<IrmaSchemeEnrollmentState>(IrmaSchemeEnrollmentState.Loading)
    val credentials = MutableStateFlow<CredentialsState>(CredentialsState.Loading)

    fun addOrUpdateSession(event: IrmaSessionEvent) {
        _sessions.value.let { sessions ->
            if (doesSessionExist(sessions, event)) {
                val copy = sessions.toMutableList()
                copy[sessionIndex(sessions, event)] = event
                _sessions.value = copy.toList()
                logUpdatedSessionList(event)
            } else {
                _sessions.value = _sessions.value.plus(event)
                logAddedSessionList(event)
            }
        }
    }

    private fun logAddedSessionList(event: IrmaSessionEvent): Int {
        return Log.d("store", "Added ${formatLogSession(event)} to the session list. ${formatNewSessionList()}")
    }

    private fun formatLogSession(event: IrmaSessionEvent): String {
        val type = when (event) {
            is RequestIssuancePermissionSessionEvent -> "issuance"
            is RequestVerificationPermissionSessionEvent -> "disclosure"
        }
        return "$type session (ID=${event.sessionId})"
    }

    private fun logUpdatedSessionList(event: IrmaSessionEvent) =
        Log.d(
            "store",
            "Updated ${formatLogSession(event)} in the session list. ${formatNewSessionList()}"
        )

    private fun doesSessionExist(
        sessions: List<IrmaSessionEvent>,
        event: IrmaSessionEvent
    ): Boolean = sessionIndex(sessions, event) >= 0

    fun doesSessionExist(
        event: IrmaSessionEvent
    ): Boolean = doesSessionExist(sessions.value, event)

    private fun sessionIndex(
        sessions: List<IrmaSessionEvent>,
        event: IrmaSessionEvent
    ) = sessions.indexOfFirst { event.sessionId == it.sessionId }

    fun removeSession(id: Long) {
        _sessions.value.let { sessions ->
            val event = sessions.firstOrNull { id == it.sessionId }
            if (event != null) {
                _sessions.value = _sessions.value.minus(event)
                Log.d("store", "Removed ${formatLogSession(event)} from the session list. ${formatNewSessionList()}")
            } else {
                Log.w("store", "Failed to remove session with ID $id from the session list.")
            }
        }
    }

    private fun formatNewSessionList(): String {

        val sessions = if (_sessions.value.isEmpty()) {
            "[empty list]"
        } else {
            _sessions.value.joinToString { formatLogSession(it) }
        }

        return "New session list: $sessions."
    }

}