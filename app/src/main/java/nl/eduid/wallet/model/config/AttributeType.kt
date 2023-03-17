package nl.eduid.wallet.model.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttributeType(
    @SerialName("ID") val id: String,
    @SerialName("Optional") val optional: String?,
    @SerialName("Name") val name: TranslatedValue?,
    @SerialName("Description") val description: TranslatedValue?,
    @SerialName("Index") val index: Int,
    @SerialName("DisplayIndex") val displayIndex: Int?,
    @SerialName("DisplayHint") val displayHint: String?,
    @SerialName("CredentialTypeID") val credentialTypeId: String,
    @SerialName("IssuerID") val issuerId: String,
    @SerialName("SchemeManagerID") val schemeManagerId: String,
)
