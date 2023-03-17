package nl.eduid.wallet.ui.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nl.eduid.wallet.shared.presentation.compose.ComposeAppView
import nl.eduid.wallet.shared.presentation.navigation.NavCommands
import javax.inject.Inject

@AndroidEntryPoint
class WalletItemDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: WalletItemDetailsViewModel.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeAppView(
        context = requireActivity(),
        navController = findNavController(),
    ) {
        Surface(color = MaterialTheme.colors.background) {
            WalletItemDetailScreen(
                modifier = Modifier
                    .systemBarsPadding()
                    .fillMaxSize(),
                onDone = ::navigateBackwards
            )
        }
    }

    private fun navigateBackwards() {
        lifecycleScope.launch {
            NavCommands.commands.emit { popBackStack() }
        }
    }

}