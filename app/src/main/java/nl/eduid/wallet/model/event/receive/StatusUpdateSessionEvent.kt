package nl.eduid.wallet.model.event.receive

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.eduid.wallet.model.session.IrmaSessionStatus
import nl.eduid.wallet.model.session.IrmaSessionType

@Serializable
data class StatusUpdateSessionEvent(
    @SerialName("SessionID") val sessionId: Int,
    @SerialName("Action") val action: IrmaSessionType,
    @SerialName("Status") val status: IrmaSessionStatus
) : IrmaBridgeReceiveEvent
