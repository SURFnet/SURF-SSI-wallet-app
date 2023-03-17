package nl.eduid.wallet.model.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Issuer(
    @SerialName("ID") val id: String,
    @SerialName("Name") val name: TranslatedValue,
    @SerialName("SchemeManagerID") val schemeManagerId: String,
    @SerialName("ContactAddress") val contactAddress: String?,
    @SerialName("ContactEMail") val contactEmail: String?,
)
