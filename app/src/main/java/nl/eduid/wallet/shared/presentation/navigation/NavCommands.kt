package nl.eduid.wallet.shared.presentation.navigation

import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableSharedFlow

typealias NavCommand = NavController.() -> Unit

object NavCommands {
    val commands = MutableSharedFlow<NavCommand>()
}