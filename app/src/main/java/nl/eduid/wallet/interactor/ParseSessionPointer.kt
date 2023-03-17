package nl.eduid.wallet.interactor

import dagger.Reusable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import nl.eduid.wallet.model.session.NewIrmaSessionPointer
import nl.eduid.wallet.model.session.IrmaSessionType
import javax.inject.Inject

@Reusable
class ParseSessionPointer @Inject constructor() {

    operator fun invoke(value: String): Result {

        if (value.isEmpty()) {
            return Result.Error.Empty
        }

        val match = matchIrmaQrCode(value)
            ?: return Result.Error.InvalidContents

        return decodeIrmaSession(match.value)
    }

    private fun matchIrmaQrCode(value: String): MatchResult? {

        val expressions = listOf(
            Regex("(?<=^irma://qr/json/).*"),
            Regex("(?<=^cardemu://qr/json/).*"),
            Regex("(?<=^intent://qr/json/).*(?=#)"),
            Regex("(?<=^https://irma.app/-/session#).*"),
            Regex("(?<=^https://irma.app/-pilot/session#).*"),
            Regex(".*", setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL))
        )

        for (expression in expressions) {
            val match = expression.find(value)
            if (match != null) {
                return match
            }
        }

        return null
    }

    private fun decodeIrmaSession(value: String): Result {
        return try {

            val qr = Json.decodeFromString<IrmaQrCode>(value)

            val sessionType = when (qr.irmaQr) {
                "issuing" -> IrmaSessionType.ISSUING
                "disclosing" -> IrmaSessionType.DISCLOSING
                else -> return Result.Error.Parse
            }

            val session = NewIrmaSessionPointer(url = qr.u, type = sessionType)
            Result.QrCode.NewIrmaSession(session)
        } catch (exception: Exception) {
            Result.Error.Parse
        }
    }

    sealed interface Result {

        sealed interface QrCode : Result {
            data class NewIrmaSession(val session: NewIrmaSessionPointer) : QrCode
        }

        sealed interface Error : Result {
            object Empty : Error
            object InvalidContents : Error
            object Parse : Error
        }
    }

    @Serializable
    private data class IrmaQrCode(val u: String, @SerialName("irmaqr") val irmaQr: String)

}