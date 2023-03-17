package nl.eduid.wallet.android.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import dagger.hilt.android.AndroidEntryPoint
import irmagobridge.Irmagobridge
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import nl.eduid.wallet.R
import nl.eduid.wallet.bridge.IrmaMobileBridge
import nl.eduid.wallet.interactor.CancelIrmaSession
import nl.eduid.wallet.interactor.EduIdEnrolled
import nl.eduid.wallet.interactor.ParseSessionPointer
import nl.eduid.wallet.interactor.SessionIdGenerator
import nl.eduid.wallet.interactor.StartIrmaSession
import nl.eduid.wallet.model.event.receive.CredentialsEvent
import nl.eduid.wallet.model.event.receive.EnrollmentStatusEvent
import nl.eduid.wallet.model.event.receive.IrmaConfigurationEvent
import nl.eduid.wallet.model.event.receive.IrmaSessionEvent
import nl.eduid.wallet.model.event.receive.IssuedCredential
import nl.eduid.wallet.model.event.receive.RequestIssuancePermissionSessionEvent
import nl.eduid.wallet.model.event.receive.RequestVerificationPermissionSessionEvent
import nl.eduid.wallet.shared.presentation.navigation.NavCommand
import nl.eduid.wallet.shared.presentation.navigation.NavCommands
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph.DISJUNCTION_INDEX_KEY
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph.MODE_KEY
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph.SESSION_ID_KEY
import nl.eduid.wallet.shared.presentation.navigation.NavigationGraph.Screen
import nl.eduid.wallet.shared.presentation.navigation.navigateToAttributeRequest
import nl.eduid.wallet.shared.presentation.navigation.navigateToAttributesReceived
import nl.eduid.wallet.shared.presentation.navigation.navigateToWalletActivation
import nl.eduid.wallet.store.CredentialsState
import nl.eduid.wallet.store.IrmaSchemeEnrollmentState
import nl.eduid.wallet.store.IrmaStore
import nl.eduid.wallet.ui.activity.YourActivityFragment
import nl.eduid.wallet.ui.add.AttributeImportFragment
import nl.eduid.wallet.ui.enrollment.activation.WalletActivationFragment
import nl.eduid.wallet.ui.enrollment.onboarding.EduIdOnboardingFragment
import nl.eduid.wallet.ui.home.HomeFragment
import nl.eduid.wallet.ui.item.WalletItemDetailFragment
import nl.eduid.wallet.ui.item.WalletItemDetailsMockMode
import nl.eduid.wallet.ui.qrscanner.QrCodeScannerFragment
import nl.eduid.wallet.ui.received.AttributeReceivedFragment
import nl.eduid.wallet.ui.request.AttributeRequestFragment
import nl.eduid.wallet.ui.shared.AttributeSharedFragment
import nl.eduid.wallet.ui.splash.SplashFragment
import nl.eduid.wallet.ui.wallet.YourDataFragment
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity(R.layout.activity_main) {

    @Inject
    internal lateinit var irmaMobileBridge: IrmaMobileBridge

    @Inject
    internal lateinit var irmaStore: IrmaStore

    @Inject
    internal lateinit var parseSessionPointer: ParseSessionPointer

    @Inject
    internal lateinit var startIrmaSession: StartIrmaSession

    @Inject
    internal lateinit var sessionIdGenerator: SessionIdGenerator

    @Inject
    internal lateinit var eduIdEnrolled: EduIdEnrolled

    @Inject
    internal lateinit var cancelIrmaSession: CancelIrmaSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        collectSessionEvents()

        lifecycleScope.launch {
            irmaMobileBridge.events
                .collect {
                    when (it) {
                        is IrmaConfigurationEvent -> {
                            irmaStore.irmaConfiguration.value = it.config
                        }
                    }
                }
        }

        irmaMobileBridge.events
            .filterIsInstance<CredentialsEvent>()
            .onEach { event ->
                irmaStore.credentials.value = CredentialsState.Loaded(event.credentials)
            }
            .launchIn(lifecycleScope)

        irmaMobileBridge.events
            .filterIsInstance<EnrollmentStatusEvent>()
            .onEach { event ->
                irmaStore.enrollment.value = IrmaSchemeEnrollmentState.Loaded(event)
            }
            .launchIn(lifecycleScope)

        val navController =
            (supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment)
                .navController

        navController.graph = navController.createGraph(
            route = NavigationGraph.GRAPH_ROUTE,
            startDestination = Screen.Splash.route
        ) {
            fragment<SplashFragment>(Screen.Splash.route)
            fragment<HomeFragment>(Screen.Home.route)
            fragment<EduIdOnboardingFragment>(Screen.Enrollment.route)
            fragment<WalletActivationFragment>("wallet-activation?$SESSION_ID_KEY={$SESSION_ID_KEY}") {
                argument(SESSION_ID_KEY) {
                    type = NavType.LongType
                }
            }
            fragment<QrCodeScannerFragment>(Screen.QrScanner.route)
            fragment<YourDataFragment>(Screen.YourData.route)
            fragment<WalletItemDetailFragment>("wallet-item-details?$MODE_KEY={$MODE_KEY}") {
                argument(MODE_KEY) {
                    type = NavType.EnumType(WalletItemDetailsMockMode::class.java)
                }
            }
            fragment<YourActivityFragment>(Screen.YourActivity.route)
            fragment<AttributeReceivedFragment>("attributes-received?$SESSION_ID_KEY={$SESSION_ID_KEY}") {
                argument(SESSION_ID_KEY) {
                    type = NavType.LongType
                }
            }
            fragment<AttributeSharedFragment>("attributes-shared?$SESSION_ID_KEY={$SESSION_ID_KEY}") {
                argument(SESSION_ID_KEY) {
                    type = NavType.LongType
                }
            }
            fragment<AttributeRequestFragment>("attributes-request?$SESSION_ID_KEY={$SESSION_ID_KEY}") {
                argument(SESSION_ID_KEY) {
                    type = NavType.LongType
                }
            }
            fragment<AttributeImportFragment>("attributes-import?$SESSION_ID_KEY={$SESSION_ID_KEY}&$DISJUNCTION_INDEX_KEY={$DISJUNCTION_INDEX_KEY}") {
                argument(SESSION_ID_KEY) {
                    type = NavType.LongType
                }
                argument(DISJUNCTION_INDEX_KEY) {
                    type = NavType.IntType
                }
            }
        }

        // Make layout content draw underneath the status bar and navigation bar. Use
        // Accompanist Insets (Compose) or Insetter (Views) to offset your layout when needed.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch {
            NavCommands.commands
                .collect { command ->
                    command(navController)
                }
        }
    }

    override fun onDestroy() {
        Irmagobridge.stop()
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {

        val data: String = try {
            val uri = intent?.dataString
            Uri.decode(uri)
        } catch (ex: Exception) {
            Log.e("error", "failed to decode intent data: $ex")
            return
        }

        when (val result = parseSessionPointer(data)) {
            is ParseSessionPointer.Result.QrCode.NewIrmaSession -> {
                val newSessionId = sessionIdGenerator.incrementAndGetSessionId()
                startIrmaSession(newSessionId, result)
            }
            is ParseSessionPointer.Result.Error -> Log.e("error", result.toString())
        }
    }

    private fun collectSessionEvents() {
        sessionEvents()
            .map(::toNavigationCommand)
            .filterNotNull()
            .onEach { command -> NavCommands.commands.emit(command) }
            .launchIn(lifecycleScope)
    }

    private fun sessionEvents() = irmaMobileBridge
        .events
        .filterIsInstance<IrmaSessionEvent>()

    private suspend fun toNavigationCommand(
        event: IrmaSessionEvent
    ): NavCommand? {

        val enrolled = eduIdEnrolled().first()

        return when {
            !enrolled && event.isEnrollmentSessionEvent() -> {
                irmaStore.addOrUpdateSession(event)
                ({ navigateToWalletActivation(event.sessionId) })
            }
            !enrolled -> {
                cancelIrmaSession.cancel(event.sessionId)
                null
            }
            else -> {
                if (irmaStore.doesSessionExist(event)) {
                    irmaStore.addOrUpdateSession(event)
                    null
                } else {
                    irmaStore.addOrUpdateSession(event)
                    when (event) {
                        is RequestIssuancePermissionSessionEvent ->
                            ({ navigateToAttributesReceived(event.sessionId) })
                        is RequestVerificationPermissionSessionEvent ->
                            ({ navigateToAttributeRequest(event.sessionId) })
                    }
                }
            }
        }
    }

    private fun IrmaSessionEvent.isEnrollmentSessionEvent(): Boolean {
        return this is RequestIssuancePermissionSessionEvent
            && (credentials.findSurfnetCredential() != null || credentials.findEduIdCredential() != null)
    }

    private fun List<IssuedCredential>.findSurfnetCredential() =
        find { it.schemeManagerId == "irma-demo" && it.issuerId == "pbdf" && it.id.value == "surfnet-2" }

    private fun List<IssuedCredential>.findEduIdCredential() =
        find { it.schemeManagerId == "irma-demo" && it.issuerId == "surf" && it.id.value == "eduid" }

}

