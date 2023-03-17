package nl.eduid.wallet.model.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SchemeManager(
    @SerialName("ID") val id: String,
    @SerialName("Name") val name: TranslatedValue,
    @SerialName("URL") val url: String,
    @SerialName("Description") val description: TranslatedValue,
    @SerialName("MinimumAppVersion") val minimumAppVersion: MinimumAppVersion,
    @SerialName("KeyshareServer") val keyshareServer: String,
    @SerialName("KeyshareWebsite") val keyshareWebsite: String,
    @SerialName("KeyshareAttribute")
    @Deprecated("Use keyshareAttributes instead")
    val keyshareAttribute: String,
    @SerialName("Timestamp") val timestamp: Long,
    @SerialName("Demo") val demo: Boolean,
)