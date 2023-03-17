package nl.eduid.wallet.ui.wallet

import com.airbnb.mvrx.MavericksState

data class WalletSection(
    val name: String,
    val items: List<WalletItem>
)

data class WalletItem(
    val name: String
)

data class YourDataViewState(
    val sections: List<WalletSection> = emptyList()
) : MavericksState
