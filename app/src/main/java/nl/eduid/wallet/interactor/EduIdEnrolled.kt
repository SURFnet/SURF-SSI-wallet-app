package nl.eduid.wallet.interactor

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import nl.eduid.wallet.model.event.receive.RawCredential
import nl.eduid.wallet.store.CredentialsState
import nl.eduid.wallet.store.IrmaStore
import javax.inject.Inject

class EduIdEnrolled @Inject constructor(
    private val store: IrmaStore
) {

    operator fun invoke(): Flow<Boolean> =
        store.credentials
            .filterIsInstance<CredentialsState.Loaded>()
            .map { state -> state.credentials.find { it.isEnrollmentCredential() } }
            .map { it != null }

    private fun RawCredential.isEnrollmentCredential() =
        (schemeManagerId == "irma-demo" && issuerId == "pbdf" && id == "surfnet-2") ||
            (schemeManagerId == "irma-demo" && issuerId == "surf" && id == "eduid")

}