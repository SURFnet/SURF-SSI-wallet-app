package nl.eduid.wallet.ui.item

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph

class WalletItemDetailsViewModel @AssistedInject constructor(
    @Assisted mode: WalletItemDetailsMockMode
) : MavericksViewModel<WalletItemDetailsViewState>(WalletItemDetailsViewState.mock(mode)) {

    @AssistedFactory
    interface Factory {
        fun create(
            mode: WalletItemDetailsMockMode
        ): WalletItemDetailsViewModel
    }

    companion object :
        MavericksViewModelFactory<WalletItemDetailsViewModel, WalletItemDetailsViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: WalletItemDetailsViewState
        ): WalletItemDetailsViewModel {

            val fragment = (viewModelContext as FragmentViewModelContext)
                .fragment<WalletItemDetailFragment>()

            val mode = fragment.arguments?.get("mode") as? WalletItemDetailsMockMode
                ?: throw IllegalStateException("Mock mode must be provided.")

            return fragment.viewModelFactory.create(mode)
        }
    }

}