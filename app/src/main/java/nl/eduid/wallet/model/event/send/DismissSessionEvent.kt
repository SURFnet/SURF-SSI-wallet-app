package nl.eduid.wallet.model.event.send

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class DismissSessionEvent(
    @SerialName("sessionID") val sessionId: Long
)
