package nl.eduid.wallet.json

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
val irmaJson = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    explicitNulls = false
}