package nl.eduid.wallet.model.event.receive

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.eduid.wallet.model.config.IrmaConfiguration

@Serializable
data class IrmaConfigurationEvent(
    @SerialName("IrmaConfiguration")
    val config: IrmaConfiguration
) : IrmaBridgeReceiveEvent