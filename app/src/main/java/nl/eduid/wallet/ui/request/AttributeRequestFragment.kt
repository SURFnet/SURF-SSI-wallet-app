package nl.eduid.wallet.ui.request

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
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.compose.ComposeAppView
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph.Screen
import nl.eduid.wallet.ui.request.compose.AttributeRequestScreenWithViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AttributeRequestFragment : Fragment(), MavericksView {

    @Inject
    internal lateinit var viewModelFactory: AttributeRequestViewModel.Factory

    private val viewModel by fragmentViewModel(AttributeRequestViewModel::class)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeAppView(
        context = requireActivity(),
        navController = findNavController(),
    ) {
        Surface(color = MaterialTheme.colors.background) {
            AttributeRequestScreenWithViewModel(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
                viewModel = viewModel,
            )
        }
    }

    override fun invalidate() {}

}
