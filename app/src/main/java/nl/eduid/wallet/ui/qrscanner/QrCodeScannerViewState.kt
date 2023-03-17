package nl.eduid.wallet.ui.qrscanner

import com.airbnb.mvrx.MavericksState

data class QrCodeScannerViewState(
    val loading: Boolean? = null,
    val pairingCode: String? = null,
) : MavericksState
