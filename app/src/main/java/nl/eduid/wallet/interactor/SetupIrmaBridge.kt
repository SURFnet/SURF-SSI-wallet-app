package nl.eduid.wallet.interactor

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import irmagobridge.Irmagobridge
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.eduid.wallet.bridge.IrmaMobileBridge
import nl.eduid.wallet.model.event.AppReadyEvent
import nl.eduid.wallet.model.event.send.ClientPreferences
import nl.eduid.wallet.model.event.send.ClientPreferencesEvent
import javax.inject.Inject

class SetupIrmaBridge @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bridge: IrmaMobileBridge
) {

    operator fun invoke() {
        setupBridge()
        enableIrmaDeveloperMode()
    }

    private fun setupBridge() {

        // Initialize the Go binding here by calling a seemingly noop function
        Irmagobridge.prestart();
        bridge.setup(context)

        val appReadyEvent = AppReadyEvent
        bridge.dispatchFromNative(
            "AppReadyEvent",
            Json.encodeToString(appReadyEvent)
        )
    }

    private fun enableIrmaDeveloperMode() {

        val preferencesEvent = ClientPreferencesEvent(
            ClientPreferences(
                developerMode = true
            )
        )

        bridge.dispatchFromNative(
            "ClientPreferencesEvent",
            Json.encodeToString(preferencesEvent)
        )
    }

}