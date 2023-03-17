package nl.eduid.wallet.ui.qrscanner

import android.util.Log
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nl.eduid.wallet.bridge.IrmaMobileBridge
import nl.eduid.wallet.interactor.GetPairingCodeEvents
import nl.eduid.wallet.interactor.ParseSessionPointer
import nl.eduid.wallet.interactor.ParseSessionPointer.Result
import nl.eduid.wallet.interactor.SessionIdGenerator
import nl.eduid.wallet.interactor.StartIrmaSession
import nl.eduid.wallet.model.event.receive.StatusUpdateSessionEvent
import nl.eduid.wallet.model.session.IrmaSessionStatus
import nl.eduid.wallet.store.IrmaStore

class QrCodeScannerViewModel @AssistedInject constructor(
    @Assisted initialState: QrCodeScannerViewState,
    private val parseSessionPointer: ParseSessionPointer,
    private val startIrmaSession: StartIrmaSession,
    private val sessionIdGenerator: SessionIdGenerator,
    private val getPairingCodeEvents: GetPairingCodeEvents,
    private val irmaMobileBridge: IrmaMobileBridge,
    private val irmaStore: IrmaStore,
) : MavericksViewModel<QrCodeScannerViewState>(initialState) {

    private var qrCode: String? = null
    private val mutex = Mutex()

    init {
        getPairingCodeEvents()
            .onEach { setState { copy(pairingCode = it.pairingCode) } }
            .launchIn(viewModelScope)
    }

    fun parseQrCode(value: String) {

        if (value == qrCode) return

        viewModelScope.launch {
            mutex.withLock {
                qrCode = value
                when (val result = parseSessionPointer(value)) {
                    is Result.QrCode.NewIrmaSession -> onValidQrCode(result)
                    is Result.Error -> onInvalidQrCode(result)
                }
            }
        }
    }

    private fun CoroutineScope.onValidQrCode(code: Result.QrCode) = launch {
        Log.i(QrCodeScannerViewModel::class.simpleName, "QR code: $code.")
        when (code) {
            is Result.QrCode.NewIrmaSession -> {

                val newSessionId = sessionIdGenerator.incrementAndGetSessionId()
                val newSessionStatusUpdate = async {

                    irmaMobileBridge.events
                        .filterIsInstance<StatusUpdateSessionEvent>()
                        .filter { newSessionId == it.sessionId && it.status == IrmaSessionStatus.CONNECTED }
                        .first()

                    setState { copy(pairingCode = null) }
                }

                startIrmaSession(newSessionId, code)
                newSessionStatusUpdate.await()
            }
        }
    }

    private fun onInvalidQrCode(error: Result.Error) {
        Log.w(QrCodeScannerViewModel::class.simpleName, "Invalid QR code: $error.")
        qrCode = null
    }

    fun dismissPairingDialog() {
        setState { copy(pairingCode = null) }
    }

    @AssistedFactory
    interface Factory :
        AssistedViewModelFactory<QrCodeScannerViewModel, QrCodeScannerViewState> {
        override fun create(state: QrCodeScannerViewState): QrCodeScannerViewModel
    }

    companion object :
        MavericksViewModelFactory<QrCodeScannerViewModel, QrCodeScannerViewState> by hiltMavericksViewModelFactory()
}
