package nl.eduid.wallet.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import nl.eduid.wallet.shared.presentation.compose.ComposeAppView
import nl.eduid.wallet.ui.add.AttributeImportViewModel
import nl.eduid.wallet.ui.splash.theme.SplashTheme
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment(), MavericksView {

    private val viewModel: SplashViewModel by fragmentViewModel()

    @Inject
    internal lateinit var viewModelFactory: SplashViewModel.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeAppView(
        context = requireActivity(),
        navController = findNavController(),
    ) {
        SplashTheme {
            SplashScreen(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setup()
    }

    override fun invalidate() {}
}
