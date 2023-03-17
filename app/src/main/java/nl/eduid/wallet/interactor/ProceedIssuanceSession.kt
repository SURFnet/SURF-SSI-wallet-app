package nl.eduid.wallet.interactor

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.eduid.wallet.bridge.IrmaMobileBridge
import nl.eduid.wallet.model.event.receive.DisclosureChoices
import nl.eduid.wallet.model.event.receive.RespondPermissionEvent
import javax.inject.Inject

class ProceedIssuanceSession @Inject constructor(
    private val irmaMobileBridge: IrmaMobileBridge,
) {
    operator fun invoke(sessionId: Long) {
        irmaMobileBridge.dispatchFromNative(
            eventName = "RespondPermissionEvent",
            payload = Json.encodeToString(RespondPermissionEvent(sessionId, true, DisclosureChoices(emptyList())))
        )
    }
}