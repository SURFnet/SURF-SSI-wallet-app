package nl.eduid.wallet.interactor

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.eduid.wallet.bridge.IrmaMobileBridge
import nl.eduid.wallet.model.event.EnrollEvent
import javax.inject.Inject

class IrmaEnroll @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bridge: IrmaMobileBridge
) {

    operator fun invoke() {

        val event = EnrollEvent(
            email = null,
            pin = "12345",
            language = "en",
            schemeId = "pbdf"
        )

        bridge.dispatchFromNative(
            eventName = "EnrollEvent",
            payload = Json.encodeToString(event)
        )
    }

}