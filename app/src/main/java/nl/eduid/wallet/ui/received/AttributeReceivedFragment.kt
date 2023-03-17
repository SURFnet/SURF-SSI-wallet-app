package nl.eduid.wallet.ui.received

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
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.compose.ComposeAppView
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph.Screen
import nl.eduid.wallet.ui.received.compose.AttributeReceivedScreen
import javax.inject.Inject

@AndroidEntryPoint
class AttributeReceivedFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: AttributeReceivedViewModel.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeAppView(
        context = requireActivity(),
        navController = findNavController(),
    ) {
        Surface(color = MaterialTheme.colors.background) {
            AttributeReceivedScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
            )
        }
    }
}
