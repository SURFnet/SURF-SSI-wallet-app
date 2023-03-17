package nl.eduid.wallet.model.event.receive

import kotlinx.serialization.SerialName

// {"SessionID":42,"Proceed":true,"DisclosureChoices":[]}
@kotlinx.serialization.Serializable
data class RespondPermissionEvent(
    @SerialName("SessionID") val sessionId: Long,
    @SerialName("Proceed") val proceed: Boolean,
    @SerialName("DisclosureChoices") val disclosureChoices: DisclosureChoices,
)

@JvmInline
@kotlinx.serialization.Serializable
value class DisclosureChoices(val value: List<List<AttributeIdentifier>>)

@kotlinx.serialization.Serializable
data class AttributeIdentifier(
    @SerialName("Type") val type: String,
    @SerialName("CredentialHash")
    val credentialHash: String
)
