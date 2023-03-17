package nl.eduid.wallet.ui.enrollment.activation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import nl.eduid.wallet.shared.presentation.compose.ComposeAppView
import nl.eduid.wallet.ui.enrollment.activation.WalletActivationScreen
import javax.inject.Inject

@AndroidEntryPoint
class WalletActivationFragment : Fragment() {

    @Inject
    internal lateinit var viewModelFactory: WalletActivationViewModel.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeAppView(
        context = requireActivity(),
        navController = findNavController(),
    ) {
        Surface(color = MaterialTheme.colors.background) {
            WalletActivationScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
            )
        }
    }

}