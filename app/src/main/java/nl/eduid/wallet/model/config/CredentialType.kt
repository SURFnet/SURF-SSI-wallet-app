package nl.eduid.wallet.model.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CredentialType(
    @SerialName("ID")
    val id: String,
    @SerialName("Name")
    val name: TranslatedValue,
    @SerialName("IssuerID")
    val issuerId: String,
    @SerialName("SchemeManagerID")
    val schemeManagerId: String,
    @SerialName("IsSingleton")
    val isSingleton: Boolean,
    @SerialName("Description")
    val description: TranslatedValue,
    @SerialName("IssueURL")
    val issueUrl: TranslatedValue?,
    @SerialName("IsULIssueURL")
    val isULIssueUrl: Boolean,
    @SerialName("DisallowDelete")
    val disallowDelete: Boolean?,
    @SerialName("ForegroundColor")
    val foregroundColor: Color?,
    @SerialName("BackgroundGradientStart")
    val backgroundGradientStart: Color?,
    @SerialName("BackgroundGradientEnd")
    val backgroundGradientEnd: Color?,
    @SerialName("IsInCredentialStore")
    val isInCredentialStore: Boolean?,
    @SerialName("Category")
    val category: TranslatedValue?,
    @SerialName("FAQIntro")
    val faqIntro: TranslatedValue?,
    @SerialName("FAQPurpose")
    val faqPurpose: TranslatedValue?,
    @SerialName("FAQContent")
    val faqContent: TranslatedValue?,
    @SerialName("FAQHowto")
    val faqHowto: TranslatedValue?,
    @SerialName("FAQSummary")
    val faqSummary: TranslatedValue?,
    @SerialName("Logo")
    val logo: String?,
)
