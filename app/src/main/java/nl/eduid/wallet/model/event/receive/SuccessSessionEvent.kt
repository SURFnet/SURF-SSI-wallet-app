package nl.eduid.wallet.model.event.receive

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuccessSessionEvent(
    @SerialName("SessionID") val sessionId: Long
) : IrmaBridgeReceiveEvent
