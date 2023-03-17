package nl.eduid.wallet.model.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EnrollEvent(
    @SerialName("Email")
    val email: String?,
    @SerialName("Pin")
    val pin: String,
    @SerialName("Language")
    val language: String,
    @SerialName("SchemeID")
    val schemeId: String,
)