package nl.eduid.wallet.shared.presentation.navigation

import androidx.navigation.NavController
import nl.eduid.wallet.R
import nl.eduid.wallet.ui.item.WalletItemDetailsMockMode

fun NavController.navigateToAttributesReceived(sessionId: Long) {
    navigate(NavigationGraph.Screen.AttributesReceived(sessionId).route) {

        popUpTo(NavigationGraph.GRAPH_ROUTE) {
            inclusive = true
        }

        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }
}

internal fun NavController.navigateToAttributeRequest(sessionId: Long) {
    navigate(NavigationGraph.Screen.AttributesRequest(sessionId).route) {

        popUpTo(NavigationGraph.GRAPH_ROUTE) {
            inclusive = true
        }

        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }
}

internal fun NavController.navigateToAttributesShared(sessionId: Long) {
    navigate(NavigationGraph.Screen.AttributesShared(sessionId).route) {

        popUpTo(NavigationGraph.GRAPH_ROUTE) {
            inclusive = true
        }

        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }
}

internal fun NavController.navigateToAttributeImport(sessionId: Long, disjunctionIndex: Int) {
    navigate(NavigationGraph.Screen.AttributeImport(sessionId, disjunctionIndex).route) {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }
}

internal fun NavController.navigateToHome() {
    navigate(NavigationGraph.Screen.Home.route) {

        popUpTo(NavigationGraph.GRAPH_ROUTE) {
            inclusive = true
        }

        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }
}

internal fun NavController.navigateToEnrollment() {
    navigate(NavigationGraph.Screen.Enrollment.route) {
        popUpTo(NavigationGraph.GRAPH_ROUTE) {
            inclusive = true
        }

        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }
}

internal fun NavController.navigateToWalletActivation(sessionId: Long) {
    navigate(NavigationGraph.Screen.WalletActivation(sessionId).route) {
        popUpTo(NavigationGraph.GRAPH_ROUTE) {
            inclusive = true
        }

        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }
}

internal fun NavController.navigateToWalletItemDetails(mode: WalletItemDetailsMockMode) {
    navigate(NavigationGraph.Screen.WalletItemDetails(mode).route) {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }
}
