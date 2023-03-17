package nl.eduid.wallet.model.event.send

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class LoadLogsEvent(
    @SerialName("Before") val before: Int?,
    @SerialName("Max") val max: Int
)