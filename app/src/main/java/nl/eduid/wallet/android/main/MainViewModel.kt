package nl.eduid.wallet.android.main

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class MainViewModel @AssistedInject constructor(
    @Assisted initialState: MainViewState,
) : MavericksViewModel<MainViewState>(initialState) {

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<MainViewModel, MainViewState> {
        override fun create(state: MainViewState): MainViewModel
    }

    companion object :
        MavericksViewModelFactory<MainViewModel, MainViewState> by hiltMavericksViewModelFactory()
}
