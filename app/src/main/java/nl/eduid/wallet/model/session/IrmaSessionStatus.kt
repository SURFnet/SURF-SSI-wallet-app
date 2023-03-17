package nl.eduid.wallet.model.session

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class IrmaSessionStatus {
    @SerialName("communicating") COMMUNICATING,
    @SerialName("connected") CONNECTED,
}