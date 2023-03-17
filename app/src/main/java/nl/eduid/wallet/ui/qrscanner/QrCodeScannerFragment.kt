package nl.eduid.wallet.ui.qrscanner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.MavericksView
import dagger.hilt.android.AndroidEntryPoint
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.compose.ComposeAppView
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph.Screen
import nl.eduid.wallet.ui.qrscanner.ui.QrCodeScannerScreen
import nl.eduid.wallet.ui.qrscanner.ui.QrCodeScannerTheme
import com.airbnb.mvrx.fragmentViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import nl.eduid.wallet.bridge.IrmaMobileBridge
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class QrCodeScannerFragment : Fragment(), MavericksView {

    @Inject
    internal lateinit var viewModelFactory: QrCodeScannerViewModel.Factory

    private val viewModel by fragmentViewModel(QrCodeScannerViewModel::class)

    @Inject
    lateinit var bridge: IrmaMobileBridge

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeAppView(
        context = requireActivity(),
        navController = findNavController(),
    ) {
        QrCodeScannerTheme {
            QrCodeScannerScreen(
                modifier = Modifier.fillMaxSize(),
                onBackButtonClick = {
                    findNavController().popBackStack()
                },
                onFlashLightClick = {},
                onQrCodeScanned = viewModel::parseQrCode,
                onQrCodeError = {},
            )
        }
    }

    override fun invalidate() { }

}
