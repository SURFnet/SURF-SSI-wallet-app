package nl.eduid.wallet.model.session

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class IrmaSessionType {
    @SerialName("issuing") ISSUING,
    @SerialName("disclosing") DISCLOSING
}