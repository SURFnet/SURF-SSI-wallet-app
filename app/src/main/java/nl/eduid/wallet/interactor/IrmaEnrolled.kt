package nl.eduid.wallet.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import nl.eduid.wallet.bridge.IrmaMobileBridge
import nl.eduid.wallet.model.event.receive.EnrollmentStatusEvent
import nl.eduid.wallet.store.IrmaSchemeEnrollmentState
import nl.eduid.wallet.store.IrmaStore
import javax.inject.Inject

class IrmaEnrolled @Inject constructor(
    private val store: IrmaStore
) {

    operator fun invoke(): Flow<Boolean> {
        return store.enrollment
            .filterIsInstance<IrmaSchemeEnrollmentState.Loaded>()
            .map { it.event.enrolledSchemeManagerIds.contains("pbdf") }
    }
}