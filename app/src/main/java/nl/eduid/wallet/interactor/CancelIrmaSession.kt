package nl.eduid.wallet.interactor

import dagger.Reusable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.eduid.wallet.bridge.IrmaMobileBridge
import nl.eduid.wallet.model.event.send.DismissSessionEvent
import javax.inject.Inject

@Reusable
class CancelIrmaSession @Inject constructor(
    private val irmaMobileBridge: IrmaMobileBridge
) {

    fun cancel(sessionId: Long) {
        val payload = DismissSessionEvent(sessionId).let { Json.encodeToString(it) }
        irmaMobileBridge.dispatchFromNative("DismissSessionEvent", payload)
    }

}