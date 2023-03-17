package nl.eduid.wallet.model.event.send

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.eduid.wallet.model.session.NewIrmaSessionPointer

@Serializable
data class NewSessionEvent(
    @SerialName("SessionID") val id: Int,
    @SerialName("Request") val request: NewIrmaSessionPointer
)
