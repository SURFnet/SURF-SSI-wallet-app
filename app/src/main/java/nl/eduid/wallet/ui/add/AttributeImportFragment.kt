package nl.eduid.wallet.ui.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.compose.ComposeAppView
import nl.eduid.wallet.shared.presentation.navigation.NavCommands
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph.Screen
import nl.eduid.wallet.ui.add.compose.AttributeImportScreenWithViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AttributeImportFragment : Fragment() {

    @Inject
    internal lateinit var viewModelFactory: AttributeImportViewModel.Factory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeAppView(
        context = requireActivity(),
        navController = findNavController(),
    ) {
        Surface(color = MaterialTheme.colors.background) {
            AttributeImportScreenWithViewModel(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
                onBackClick = ::navigateBack,
                onAttributeImportClick = ::openIssueUrl
            )
        }
    }

    private fun navigateBack() {
        lifecycleScope.launch {
            NavCommands.commands.emit(NavController::popBackStack)
        }
    }

    private fun openIssueUrl(issuer: CredentialIssuer) {

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = (Uri.parse(issuer.issueUrl))
        }

        startActivity(intent)
    }

//    private fun navigateToAttributesReceivedScreen() {
//        findNavController().navigate(Screen.AttributesReceived.route) {
//
//            popUpTo(NavigationGraph.GRAPH_ROUTE) {
//                inclusive = true
//            }
//
//            anim {
//                enter = R.anim.slide_in_right
//                exit = R.anim.slide_out_left
//                popEnter = R.anim.slide_in_left
//                popExit = R.anim.slide_out_right
//            }
//        }
//    }
}
