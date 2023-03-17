package nl.eduid.wallet.ui.item

import com.airbnb.mvrx.MavericksState

sealed interface WalletAttribute {

    data class InWallet(
        val value: String,
        val issuer: String,
        val deletable: Boolean
    ) : WalletAttribute

    object Missing : WalletAttribute

}

data class WalletItemDetailsViewState(
    val name: String? = null,
    val unverified: List<WalletAttribute> = emptyList(),
    val verified: List<WalletAttribute> = emptyList(),
) : MavericksState {

    companion object {

        fun mock(mode: WalletItemDetailsMockMode) = when (mode) {
            WalletItemDetailsMockMode.FULL_NAME -> mockName()
            WalletItemDetailsMockMode.DATA_OF_BIRTH -> mockBirthDate()
            WalletItemDetailsMockMode.MASTERS_DEGREE -> TODO()
            WalletItemDetailsMockMode.GRADUATION_DATE -> TODO()
        }

        fun mockName() = WalletItemDetailsViewState(
            name = "Full name",
            unverified = listOf(
                WalletAttribute.InWallet(
                    value = "René v. Hamersdonksveer",
                    issuer = "you",
                    deletable = false
                )
            ),
            verified = listOf(
                WalletAttribute.Missing
            )
        )

        fun mockBirthDate() = WalletItemDetailsViewState(
            name = "Date of birth",
            unverified = listOf(
                WalletAttribute.InWallet(
                    value = "February 16, 1993",
                    issuer = "you",
                    deletable = true
                )
            ),
            verified = listOf(
                WalletAttribute.InWallet(
                    value = "February 16, 1993",
                    issuer = "Mijn Overheid",
                    deletable = true
                )
            )
        )

    }

}

enum class WalletItemDetailsMockMode {
    FULL_NAME, DATA_OF_BIRTH, MASTERS_DEGREE, GRADUATION_DATE
}