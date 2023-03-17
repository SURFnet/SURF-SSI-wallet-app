package nl.eduid.wallet.model.config

import kotlinx.serialization.Serializable

@Serializable
data class RequestorScheme(
    val id: String,
    val demo: Boolean,
)