package nl.eduid.wallet.ui.home.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import nl.eduid.wallet.R
import nl.eduid.wallet.ui.home.compose.HomeButton

@Composable
internal fun HomeButtons(
    modifier: Modifier = Modifier,
    onScanQrClick: () -> Unit,
    onYourDateClick: () -> Unit,
    onActivityClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(Color(0xFF0062B0))
            .statusBarsPadding()
            .height(237.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {

        val buttonModifier = Modifier.padding(bottom = 64.dp)

        HomeButton(
            modifier = buttonModifier,
            title = "Scan QR",
            onClick = onScanQrClick
        ) {
            Icon(
                modifier = Modifier.padding(13.dp),
                painter = painterResource(id = R.drawable.ic_qr_code_scan),
                contentDescription = null,
            )
        }
        HomeButton(
            modifier = buttonModifier,
            title = "Your data",
            onClick = onYourDateClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_money_wallet),
                contentDescription = null,
            )
        }
        HomeButton(
            modifier = buttonModifier,
            title = "Activity",
            onClick = onActivityClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_time_clock_circle),
                contentDescription = null,
            )
        }
    }
}