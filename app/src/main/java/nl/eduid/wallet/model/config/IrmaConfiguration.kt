package nl.eduid.wallet.model.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IrmaConfiguration(
    @SerialName("SchemeManagers") val schemeManagers: Map<String, SchemeManager>,
    @SerialName("RequestorSchemes") val requestorSchemes: Map<String, RequestorScheme>,
    @SerialName("Requestors") val requestors: Map<String, RequestorInfo>,
    @SerialName("Issuers") val issuers: Map<String, Issuer>,
    @SerialName("CredentialTypes") val credentialTypes: Map<String, CredentialType>,
    @SerialName("AttributeTypes") val attributeTypes: Map<String, AttributeType>,
    @SerialName("IssueWizards") val issueWizards: Map<String, IssueWizard>,
    @SerialName("Path") val path: String,
)