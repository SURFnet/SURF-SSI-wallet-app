package nl.eduid.wallet.shared.presentation.navigation

import nl.eduid.wallet.ui.item.WalletItemDetailsMockMode

object NavigationGraph {

    const val GRAPH_ROUTE = "/root"
    const val EVENT_KEY = "event"
    const val SESSION_ID_KEY = "id"
    const val DISJUNCTION_INDEX_KEY = "disjunction_index"
    const val MODE_KEY = "mode"

    sealed class Screen(val route: String) {
        object Splash : Screen("splash")
        object Home : Screen("home")
        object Enrollment : Screen("enrollment")
        data class WalletActivation(val sessionId: Long) : Screen("wallet-activation?$SESSION_ID_KEY=$sessionId")
        object QrScanner : Screen("qrscanner")
        object YourData : Screen("your-data")
        object YourActivity : Screen("your-activity")
        data class WalletItemDetails(val mode: WalletItemDetailsMockMode) : Screen("wallet-item-details?$MODE_KEY=$mode")
        data class AttributesReceived(val sessionId: Long) : Screen("attributes-received?$SESSION_ID_KEY=$sessionId")
        data class AttributesShared(val sessionId: Long) : Screen("attributes-shared?$SESSION_ID_KEY=$sessionId")
        data class AttributesRequest(val sessionId: Long) : Screen("attributes-request?$SESSION_ID_KEY=$sessionId")
        data class AttributeImport(val sessionId: Long, val index: Int) : Screen("attributes-import?$SESSION_ID_KEY=$sessionId&$DISJUNCTION_INDEX_KEY=$index")
        object Settings : Screen("settings")
    }
}
