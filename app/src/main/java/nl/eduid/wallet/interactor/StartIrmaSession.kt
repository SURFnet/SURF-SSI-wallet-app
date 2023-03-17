package nl.eduid.wallet.interactor

import android.annotation.SuppressLint
import android.content.Context
import dagger.Reusable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.encodeToString
import nl.eduid.wallet.bridge.IrmaMobileBridge
import nl.eduid.wallet.json.irmaJson
import nl.eduid.wallet.model.event.send.NewSessionEvent
import javax.inject.Inject

@Reusable
class StartIrmaSession @Inject constructor(
    val bridge: IrmaMobileBridge,
) {

    operator fun invoke(
        sessionId: Int,
        pointer: ParseSessionPointer.Result.QrCode.NewIrmaSession
    ): Int {

        val encodedEvent = irmaJson.encodeToString(
            NewSessionEvent(
                id = sessionId,
                request = pointer.session
            )
        )

        bridge.dispatchFromNative("NewSessionEvent", encodedEvent)

        return sessionId
    }


}

class SessionIdGenerator @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    @SuppressLint("ApplySharedPref")
    fun incrementAndGetSessionId(): Int {

        val sharedPreferences =
            context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

        val currentSessionId = sharedPreferences.getInt(SESSION_ID_KEY, 0)

        val newSessionId = currentSessionId + 1

        sharedPreferences.edit().putInt(SESSION_ID_KEY, newSessionId).commit()

        return newSessionId
    }

    companion object {
        const val SESSION_ID_KEY = "session_id"
    }
}