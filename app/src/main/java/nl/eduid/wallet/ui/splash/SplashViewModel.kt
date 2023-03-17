package nl.eduid.wallet.ui.splash

import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import nl.eduid.wallet.interactor.EduIdEnrolled
import nl.eduid.wallet.interactor.IrmaEnroll
import nl.eduid.wallet.interactor.IrmaEnrolled
import nl.eduid.wallet.interactor.ParseSessionPointer
import nl.eduid.wallet.interactor.SessionIdGenerator
import nl.eduid.wallet.interactor.SetupIrmaBridge
import nl.eduid.wallet.interactor.StartIrmaSession
import nl.eduid.wallet.shared.presentation.navigation.NavCommands
import nl.eduid.wallet.shared.presentation.navigation.navigateToEnrollment
import nl.eduid.wallet.shared.presentation.navigation.navigateToHome

class SplashViewModel @AssistedInject constructor(
    @Assisted initialState: SplashViewState,
    @Assisted private val uri: Uri?,
    private val setupIrmaBridge: SetupIrmaBridge,
    private val irmaEnroll: IrmaEnroll,
    private val irmaEnrolled: IrmaEnrolled,
    private val parseSessionPointer: ParseSessionPointer,
    private val sessionIdGenerator: SessionIdGenerator,
    private val startIrmaSession: StartIrmaSession,
    private val eduIdEnrolled: EduIdEnrolled,
) : MavericksViewModel<SplashViewState>(initialState) {

    fun setup() {
        viewModelScope.launch {

            setupIrmaBridge()

            val delay = async { delay(750) }
            val irmaEnrolled = async { irmaEnrolled().first() }
            val eduIdEnrolled = async { eduIdEnrolled().first() }

            if (!irmaEnrolled.await()) {
                irmaEnroll()
            }

            delay.await()

            when {
                !eduIdEnrolled.await() -> enrollment()
                uri != null -> handleUri(uri)
                else -> home()
            }
        }
    }

    private fun enrollment() {
        viewModelScope.launch {
            NavCommands.commands.emit { navigateToEnrollment() }
        }
    }

    private fun handleUri(uri: Uri) {
        when (val result = parseUri(uri)) {
            is ParseSessionPointer.Result.QrCode.NewIrmaSession -> {
                // Side note: we assume the MainActivity handles the navigation by
                // subscribing to the relevant IRMA events
                val newSessionId = sessionIdGenerator.incrementAndGetSessionId()
                startIrmaSession(newSessionId, result)
            }
            is ParseSessionPointer.Result.Error -> home()
        }
    }

    private fun home() {
        viewModelScope.launch {
            NavCommands.commands.emit(NavController::navigateToHome)
        }
    }

    private fun parseUri(uri: Uri): ParseSessionPointer.Result {

        val data: String = try {
            Uri.decode(uri.toString())
        } catch (ex: Exception) {
            Log.e("error", "failed to decode uri: $ex")
            return ParseSessionPointer.Result.Error.Parse
        }

        return parseSessionPointer(data)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            state: SplashViewState,
            uri: Uri?
        ): SplashViewModel
    }

    companion object :
        MavericksViewModelFactory<SplashViewModel, SplashViewState> {

        override fun create(
            viewModelContext: ViewModelContext,
            state: SplashViewState
        ): SplashViewModel {

            val fragment = (viewModelContext as FragmentViewModelContext)
                .fragment<SplashFragment>()

            return fragment.viewModelFactory.create(state, fragment.activity?.intent?.data)
        }
    }
}
