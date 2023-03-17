package nl.eduid.wallet.model.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IssueWizard(
    @SerialName("id") val id: String,
    @SerialName("title") val title: TranslatedValue,
    @SerialName("logo") val logo: String?,
    @SerialName("logoPath") val logoPath: String?,
    @SerialName("color") val color: String?,
    @SerialName("textColor") val textColor: String?,
    @SerialName("issues") val issues: String?,
    @SerialName("allowOtherRequestors") val allowOtherRequestors: Boolean,
    @SerialName("info") val info: TranslatedValue,
    @SerialName("faq") val faq: List<IssueWizardQA>? = emptyList(),
    @SerialName("intro") val intro: TranslatedValue?,
    @SerialName("successHeader") val successHeader: TranslatedValue,
    @SerialName("successText") val successText: TranslatedValue,
    @SerialName("expandDependencies") val expandDependencies: Boolean?,
)

@Serializable
data class IssueWizardQA(
    @SerialName("question") val question: TranslatedValue,
    @SerialName("answer") val answer: TranslatedValue,
)
