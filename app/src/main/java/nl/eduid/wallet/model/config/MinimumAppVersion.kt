package nl.eduid.wallet.model.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MinimumAppVersion(
    @SerialName("Android") val android: Int,
    @SerialName("IOS") val ios: Int
)