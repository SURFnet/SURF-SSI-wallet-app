package nl.eduid.wallet.interactor

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onEach
import nl.eduid.wallet.bridge.IrmaMobileBridge
import nl.eduid.wallet.model.event.receive.PairingRequiredSessionEvent
import timber.log.Timber
import javax.inject.Inject

@Reusable
class GetPairingCodeEvents @Inject constructor(
    private val bridge: IrmaMobileBridge,
) {

    operator fun invoke(): Flow<PairingRequiredSessionEvent> =
        bridge.events.filterIsInstance()

}