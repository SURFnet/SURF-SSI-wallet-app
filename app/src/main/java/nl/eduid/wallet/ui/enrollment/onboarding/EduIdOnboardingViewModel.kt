package nl.eduid.wallet.ui.enrollment.onboarding

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class EduIdOnboardingViewModel @AssistedInject constructor(
    @Assisted initialState: EduIdOnboardingViewState,
) : MavericksViewModel<EduIdOnboardingViewState>(initialState) {

    @AssistedFactory
    interface Factory :
        AssistedViewModelFactory<EduIdOnboardingViewModel, EduIdOnboardingViewState> {
        override fun create(state: EduIdOnboardingViewState): EduIdOnboardingViewModel
    }

    companion object :
        MavericksViewModelFactory<EduIdOnboardingViewModel, EduIdOnboardingViewState> by hiltMavericksViewModelFactory()
}
