package nl.eduid.wallet.model.config

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class LanguageCode(val value: String)


@JvmInline
@Serializable
value class TranslatedValue(val copy: Map<LanguageCode, String>)
