package nl.eduid.wallet.model.event.receive

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class FailureSessionEvent(
    @SerialName("SessionID") val sessionId: Long
) : IrmaBridgeReceiveEvent