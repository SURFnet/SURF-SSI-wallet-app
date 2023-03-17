package nl.eduid.wallet.model.event.receive

import android.content.ReceiverCallNotAllowedException
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class EnrollmentStatusEvent(
    @SerialName("EnrolledSchemeManagerIds")
    val enrolledSchemeManagerIds: List<String>,
    @SerialName("UnenrolledSchemeManagerIds")
    val unenrolledSchemeManagerIds: List<String>,
): IrmaBridgeReceiveEvent