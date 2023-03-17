package nl.eduid.wallet.model.event.send

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientPreferences(
    @SerialName("DeveloperMode") val developerMode: Boolean
)

@Serializable
data class ClientPreferencesEvent(
    @SerialName("Preferences") val preferences: ClientPreferences
)