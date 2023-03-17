package nl.eduid.wallet.model.event.receive

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.eduid.wallet.model.config.RequestorInfo
import nl.eduid.wallet.model.config.TranslatedValue

sealed interface IrmaSessionEvent {
    val sessionId: Long
}

@Serializable
data class RequestIssuancePermissionSessionEvent(
    @SerialName("SessionID") override val sessionId: Long,
    @SerialName("ServerName") val serverName: ServerName,
    @SerialName("IssuedCredentials") val credentials: List<IssuedCredential>,
) : IrmaBridgeReceiveEvent, IrmaSessionEvent

@Serializable
data class ServerName(
    val id: String,
    val scheme: String,
    val name: TranslatedValue,
)

@JvmInline
@Serializable
value class CredentialId(val value: String)

@Serializable
data class IssuedCredential(
    @SerialName("ID") val id: CredentialId,
    @SerialName("IssuerID") val issuerId: String,
    @SerialName("SchemeManagerID") val schemeManagerId: String,
    @SerialName("Attributes") val attributes: IssuedAttributes
)

@JvmInline
@Serializable
value class AttributeId(val value: String)

@JvmInline
@Serializable
value class IssuedAttributes(val value: Map<AttributeId, TranslatedValue?>)

@Serializable
data class RequestVerificationPermissionSessionEvent(
    @SerialName("SessionID")
    override val sessionId: Long,
    @SerialName("ServerName")
    val serverName: RequestorInfo,
    @SerialName("Satisfiable")
    val satisfiable: Boolean,
    @SerialName("DisclosuresLabels")
    val disclosuresLabels: Map<Int, TranslatedValue>?,
    @SerialName("Disclosures")
    val disclosures: List<List<List<String>>>,
    @SerialName("DisclosuresCandidates")
    val disclosuresCandidates: List<List<List<DisclosureCandidate>>>,
    @SerialName("IsSignatureSession")
    val isSignatureSession: Boolean,
    @SerialName("SignedMessage")
    val signedMessage: String?,
) : IrmaBridgeReceiveEvent, IrmaSessionEvent

@Serializable
data class DisclosureCandidate(
    @SerialName("Type")
    val type: String,
    @SerialName("CredentialHash")
    val credentialHash: String?,
    @SerialName("Value") // Default value is set by fromJson of TranslatedValue
    val value: TranslatedValue?,
    @SerialName("Expired")
    val expired: Boolean,
    @SerialName("Revoked")
    val revoked: Boolean,
    @SerialName("NotRevokable")
    val notRevokable: Boolean,
)
