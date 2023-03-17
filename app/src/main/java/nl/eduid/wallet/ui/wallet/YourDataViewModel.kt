package nl.eduid.wallet.ui.wallet

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import nl.eduid.wallet.shared.presentation.navigation.NavCommands
import nl.eduid.wallet.shared.presentation.navigation.navigateToWalletActivation
import nl.eduid.wallet.shared.presentation.navigation.navigateToWalletItemDetails
import nl.eduid.wallet.ui.item.WalletItemDetailsMockMode

class YourDataViewModel @AssistedInject constructor(
    @Assisted initialState: YourDataViewState
): MavericksViewModel<YourDataViewState>(initialState) {

    fun setup() {
        setState {
            copy(
                sections = listOf(
                    WalletSection(
                        name = "About you",
                        items = listOf(
                            WalletItem(
                                name = "Your full name"
                            ),
                            WalletItem(
                                name = "Your date of birth"
                            )
                        )
                    ),
                    WalletSection(
                        name = "About your studies",
                        items = listOf(
                            WalletItem(
                                name = "Proof of masters degree"
                            ),
                            WalletItem(
                                name = "Your graduation date"
                            )
                        )
                    )
                )
            )
        }
    }

    fun onWalletItemClick(item: WalletItem) {

        val mode = when (item.name) {
            "Your full name" -> WalletItemDetailsMockMode.FULL_NAME
            "Your date of birth" -> WalletItemDetailsMockMode.DATA_OF_BIRTH
            else -> return
        }

        viewModelScope.launch {
            NavCommands.commands.emit { navigateToWalletItemDetails(mode) }
        }
    }

    @AssistedFactory
    interface Factory :
        AssistedViewModelFactory<YourDataViewModel, YourDataViewState> {
        override fun create(state: YourDataViewState): YourDataViewModel
    }

    companion object :
        MavericksViewModelFactory<YourDataViewModel, YourDataViewState> by hiltMavericksViewModelFactory()

}