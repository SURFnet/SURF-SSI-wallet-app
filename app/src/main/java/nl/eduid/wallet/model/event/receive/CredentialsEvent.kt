package nl.eduid.wallet.model.event.receive

import kotlinx.serialization.SerialName
import nl.eduid.wallet.model.config.TranslatedValue

@kotlinx.serialization.Serializable
data class CredentialsEvent(
    @SerialName("Credentials")
    val credentials: List<RawCredential>,
) : IrmaBridgeReceiveEvent


@kotlinx.serialization.Serializable
data class RawCredential(
    @SerialName("ID")
    val id: String,
    @SerialName("IssuerID")
    val issuerId: String,
    @SerialName("SchemeManagerID")
    val schemeManagerId: String,
    @SerialName("SignedOn")
    val signedOn: Int,
    @SerialName("Expires")
    val expires: Int,
    @SerialName("Attributes")
    val attributes: Map<String, TranslatedValue?>,
    @SerialName("Hash")
    val hash: String,
    @SerialName("Revoked")
    val revoked: Boolean,
    @SerialName("RevocationSupported")
    val revocationSupported: Boolean,
)
