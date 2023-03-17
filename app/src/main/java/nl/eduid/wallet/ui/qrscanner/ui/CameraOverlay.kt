package nl.eduid.wallet.ui.qrscanner.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nl.eduid.wallet.R

@Composable
internal fun CameraOverlay(
    modifier: Modifier = Modifier,
    onBackButtonClick: () -> Unit,
    onFlashLightClick: () -> Unit
) {
    Column(modifier) {
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(TransparentGray)
                .statusBarsPadding()
        ) {

            IconButton(
                modifier = Modifier
                    .padding(top = 52.dp, start = 24.dp)
                    .align(Alignment.TopStart),
                onClick = onBackButtonClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null
                )
            }

            IconButton(
                modifier = Modifier
                    .padding(top = 52.dp, end = 24.dp)
                    .align(Alignment.TopEnd),
                onClick = onFlashLightClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_flashlight),
                    contentDescription = null
                )
            }

            Text(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 41.dp),
                text = stringResource(id = R.string.qr_code_scanner_heading),
                style = MaterialTheme.typography.h4
            )
        }

        Row(
            Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .weight(1f)
                .height(248.dp)
        ) {

            Box(
                Modifier
                    .fillMaxHeight()
                    .width(45.dp)
                    .background(TransparentGray)
            )

            Box(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                listOf(
                    Alignment.TopStart to 0f,
                    Alignment.TopEnd to 90f,
                    Alignment.BottomEnd to 180f,
                    Alignment.BottomStart to -90f
                ).forEach { (align, rotation) ->
                    Image(
                        modifier = Modifier
                            .align(align)
                            .rotate(rotation),
                        painter = painterResource(id = R.drawable.ic_qr_corner),
                        contentDescription = null
                    )
                }
            }

            Box(
                Modifier
                    .fillMaxHeight()
                    .width(45.dp)
                    .background(TransparentGray)
            )

        }

        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(TransparentGray)
                .navigationBarsPadding()
        )
    }
}