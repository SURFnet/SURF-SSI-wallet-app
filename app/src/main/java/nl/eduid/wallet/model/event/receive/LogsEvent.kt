package nl.eduid.wallet.model.event.receive

import kotlinx.serialization.SerialName
import nl.eduid.wallet.model.config.RequestorInfo
import nl.eduid.wallet.model.config.TranslatedValue

@kotlinx.serialization.Serializable
data class LogsEvent(
    @SerialName("LogEntries") val entries: List<LogEntry>
) : IrmaBridgeReceiveEvent

@kotlinx.serialization.Serializable
data class LogEntry(
    @SerialName("ID")
    val id: Long,
    @SerialName("Type")
    val type: LogEntryType,
    @SerialName("Time")
    val time: Long,
    @SerialName("ServerName")
    val serverRequestorInfo: RequestorInfo?, // Due to some legacy log entries, serverName might be null sometimes. This should be fixed in irmago.
    @SerialName("IssuedCredentials")
    val issuedCredentials: List<RawCredential>,
    @SerialName("DisclosedCredentials")
    val disclosedAttributes: List<List<DisclosedAttribute>>,
    @SerialName("RemovedCredentials")
    val removedCredentials: Map<String, Map<String, TranslatedValue>>,
    @SerialName("SignedMessage")
    val signedMessage: SignedMessage?,
)

@kotlinx.serialization.Serializable
enum class LogEntryType {
    @SerialName("disclosing")
    Disclosing,
    @SerialName("signing")
    Signing,
    @SerialName("issuing")
    Issuing,
    @SerialName("removal")
    Removal,
}

@kotlinx.serialization.Serializable
data class DisclosedAttribute(
    @SerialName("rawValue")
    val  rawValue: String?,
    @SerialName("value") // Default value is set by fromJson of TranslatedValue
    val value: TranslatedValue ,
    @SerialName("id")
    val identifier: String ,
    @SerialName("status")
    val status: String ,
    @SerialName("issuancetime")
    val issuanceTime: Int,
)

@kotlinx.serialization.Serializable
data class SignedMessage(
    val message: String
)