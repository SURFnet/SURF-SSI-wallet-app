package nl.eduid.wallet.model.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestorInfo(
    @SerialName("id") val id: String?,
    @SerialName("name") val name: TranslatedValue,
    @SerialName("industry") val industry: TranslatedValue?,
    @SerialName("logo") val logo: String?,
    @SerialName("logoPath") val logoPath: String?,
    @SerialName("unverified") val unverified: Boolean,
    @SerialName("hostnames") val hostnames: List<String>,
)