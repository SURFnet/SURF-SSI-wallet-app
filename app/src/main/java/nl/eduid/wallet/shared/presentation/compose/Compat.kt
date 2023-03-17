package nl.eduid.wallet.shared.presentation.compose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme

@Suppress("FunctionName")
fun Fragment.ComposeAppView(
    context: Context,
    navController: NavController,
    content: @Composable () -> Unit
) = ComposeView(context).apply {

    // Dispose the Composition when viewLifecycleOwner is destroyed
    setViewCompositionStrategy(
        ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
    )

    setContent {
        AppTheme {
            CompositionLocalProvider(
                LocalNavController provides navController,
            ) { content() }
        }
    }
}
