package nl.eduid.wallet.inject

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap
import nl.eduid.wallet.android.main.MainViewModel
import nl.eduid.wallet.ui.activity.YourActivityFragment
import nl.eduid.wallet.ui.activity.YourActivityViewModel
import nl.eduid.wallet.ui.home.HomeViewModel
import nl.eduid.wallet.ui.qrscanner.QrCodeScannerViewModel
import nl.eduid.wallet.ui.splash.SplashViewModel
import nl.eduid.wallet.ui.wallet.YourDataViewModel

@Module
@InstallIn(MavericksViewModelComponent::class)
interface ViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun homeViewModelFactory(factory: HomeViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun mainViewModelFactory(factory: MainViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(QrCodeScannerViewModel::class)
    fun qrCodeScannerViewModelFactory(factory: QrCodeScannerViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(YourDataViewModel::class)
    fun yourDataViewModelFactory(factory: YourDataViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(YourActivityViewModel::class)
    fun yourActivityViewModelFactory(factory: YourActivityViewModel.Factory): AssistedViewModelFactory<*, *>
}
