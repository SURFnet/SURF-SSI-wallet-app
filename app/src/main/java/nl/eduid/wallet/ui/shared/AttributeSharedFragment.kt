package nl.eduid.wallet.ui.shared

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
import nl.eduid.wallet.ui.shared.compose.AttributeSharedScreenWithViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AttributeSharedFragment : Fragment(), MavericksView {

    @Inject
    internal lateinit var viewModelFactory: AttributeSharedViewModel.Factory

    private val viewModel by fragmentViewModel(AttributeSharedViewModel::class)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeAppView(
        context = requireActivity(),
        navController = findNavController(),
    ) {
        Surface(color = MaterialTheme.colors.background) {
            AttributeSharedScreenWithViewModel(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
                viewModel = viewModel,
                onContinue = {
                    findNavController().navigate(Screen.Home.route) {

                        popUpTo(NavigationGraph.GRAPH_ROUTE) {
                            inclusive = true
                        }

                        anim {
                            enter = R.anim.slide_in_right
                            exit = R.anim.slide_out_left
                            popEnter = R.anim.slide_in_left
                            popExit = R.anim.slide_out_right
                        }
                    }
                }
            )
        }
    }

    override fun invalidate() {}

}
