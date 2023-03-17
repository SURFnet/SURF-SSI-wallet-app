package nl.eduid.wallet.ui.add

import com.airbnb.mvrx.MavericksState

data class AttributeImportViewState(
    val attributeType: String? = null,
    val credentialIssuers: List<CredentialIssuer> = emptyList()
) : MavericksState

data class CredentialIssuer(
    val credentialId: String,
    val issuerName: String,
    val issueUrl: String
)
