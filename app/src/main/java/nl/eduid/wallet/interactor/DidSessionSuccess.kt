package nl.eduid.wallet.interactor

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import nl.eduid.wallet.bridge.IrmaMobileBridge
import nl.eduid.wallet.model.event.receive.SuccessSessionEvent
import javax.inject.Inject

class DidSessionSuccess @Inject constructor(
    private val bridge: IrmaMobileBridge
) {

    operator fun invoke(sessionId: Long) =
        bridge.events
            .filterIsInstance<SuccessSessionEvent>()
            .filter { it.sessionId == sessionId }

}