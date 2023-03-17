package nl.eduid.wallet.model.event.receive

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PairingRequiredSessionEvent(
    @SerialName("SessionID") val sessionId: Int,
    @SerialName("PairingCode") val pairingCode: String
) : IrmaBridgeReceiveEvent
