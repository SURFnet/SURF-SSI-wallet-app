package nl.eduid.wallet.bridge

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import irmagobridge.IrmaMobileBridge
import irmagobridge.Irmagobridge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import nl.eduid.wallet.json.irmaJson
import nl.eduid.wallet.model.event.receive.CredentialsEvent
import nl.eduid.wallet.model.event.receive.EnrollmentStatusEvent
import nl.eduid.wallet.model.event.receive.FailureSessionEvent
import nl.eduid.wallet.model.event.receive.IrmaBridgeReceiveEvent
import nl.eduid.wallet.model.event.receive.IrmaConfigurationEvent
import nl.eduid.wallet.model.event.receive.LogsEvent
import nl.eduid.wallet.model.event.receive.PairingRequiredSessionEvent
import nl.eduid.wallet.model.event.receive.RequestIssuancePermissionSessionEvent
import nl.eduid.wallet.model.event.receive.RequestVerificationPermissionSessionEvent
import nl.eduid.wallet.model.event.receive.StatusUpdateSessionEvent
import nl.eduid.wallet.model.event.receive.SuccessSessionEvent
import java.io.IOException
import java.security.GeneralSecurityException
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("LogNotTimber")

@Singleton
class IrmaMobileBridge @Inject constructor() : IrmaMobileBridge {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    val _events = MutableSharedFlow<IrmaBridgeReceiveEvent>()
    val events: SharedFlow<IrmaBridgeReceiveEvent>
        get() = _events

    private var debug = false
    private var nativeError: String? = null

    fun setup(context: Context) {
        try {
            val copier = IrmaConfigurationCopier(context)
            val aesKey = AESKey.getKey(context)
            val pi = context.packageManager.getPackageInfo(
                context.packageName, 0
            )
            Irmagobridge.start(
                this,
                pi.applicationInfo.dataDir,
                copier.destAssetsPath.toString(),
                aesKey
            )
            debug = pi.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: GeneralSecurityException) {
            nativeError = String.format(
                "{\"Exception\":\"%s\",\"Stack\":\"%s\",\"Fatal\":true}",
                e.toString(),
                e.stackTrace
            )
        } catch (e: IOException) {
            nativeError = String.format(
                "{\"Exception\":\"%s\",\"Stack\":\"%s\",\"Fatal\":true}",
                e.toString(),
                e.stackTrace
            )
        } catch (e: PackageManager.NameNotFoundException) {
            nativeError = String.format(
                "{\"Exception\":\"%s\",\"Stack\":\"%s\",\"Fatal\":true}",
                e.toString(),
                e.stackTrace
            )
        }
    }

    private fun log(message: String) {
        Log.d("irmabridge", message)
    }

    private fun logException(message: String, exception: Exception) {
        Log.d("irmabridge", message, exception)
    }

    fun dispatchFromNative(eventName: String, payload: String) {
        log("dispatchFromNative: name=$eventName payload=$payload")
        Irmagobridge.dispatchFromNative(eventName, payload)
    }

    override fun dispatchFromGo(name: String, payload: String) {
        scope.launch {
            log("dispatchFromGo: name=$name payload=$payload")
            try {
                val event = decodeReceiveEventFromString(name, payload)
                _events.emit(event)
            } catch (exception: Exception) {
                logException("dispatchFromGo: could not emit \"$name\" event because unsuccessful parse.", exception)
            }
        }
    }

    private fun decodeReceiveEventFromString(
        name: String,
        payload: String
    ): IrmaBridgeReceiveEvent {
        return when (name) {
            "IrmaConfigurationEvent" -> irmaJson
                .decodeFromString(IrmaConfigurationEvent.serializer(), payload)
            "StatusUpdateSessionEvent" -> irmaJson
                .decodeFromString(StatusUpdateSessionEvent.serializer(), payload)
            "PairingRequiredSessionEvent" -> irmaJson
                .decodeFromString(PairingRequiredSessionEvent.serializer(), payload)
            "RequestIssuancePermissionSessionEvent" -> irmaJson
                .decodeFromString(RequestIssuancePermissionSessionEvent.serializer(), payload)
            "SuccessSessionEvent" -> irmaJson
                .decodeFromString(SuccessSessionEvent.serializer(), payload)
            "RequestVerificationPermissionSessionEvent" -> irmaJson
                .decodeFromString(RequestVerificationPermissionSessionEvent.serializer(), payload)
            "LogsEvent" -> irmaJson
                .decodeFromString(LogsEvent.serializer(), payload)
            "EnrollmentStatusEvent" -> irmaJson
                .decodeFromString(EnrollmentStatusEvent.serializer(), payload)
            "CredentialsEvent" -> irmaJson
                .decodeFromString(CredentialsEvent.serializer(), payload)
            "FailureSessionEvent" -> irmaJson
                .decodeFromString(FailureSessionEvent.serializer(), payload)
            else -> throw IllegalArgumentException("Unknown event type.")
        }
    }

    override fun debugLog(message: String) {
        if (debug) {
            log("debugLog: $message")
        }
    }
}