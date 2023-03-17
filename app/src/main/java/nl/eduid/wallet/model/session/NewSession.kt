package nl.eduid.wallet.model.session

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewIrmaSessionPointer(
    @SerialName("u") val url: String,
    @SerialName("irmaqr") val type: IrmaSessionType
)



