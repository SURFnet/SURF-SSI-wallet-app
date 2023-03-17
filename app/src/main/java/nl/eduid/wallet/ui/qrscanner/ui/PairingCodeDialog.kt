package nl.eduid.wallet.ui.qrscanner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme

@Composable
fun PairingCodeDialog(
    modifier: Modifier = Modifier,
    pairingCode: String,
    onDismissRequest: () -> Unit
) {
    AppTheme {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card {
                Column(Modifier.padding(24.dp)) {
                    Text(
                        text = "Pairing Code",
                        style = MaterialTheme.typography.subtitle1
                    )
                    Row(
                        modifier = modifier
                            .padding(top = 24.dp, bottom = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            24.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (char in pairingCode) {
                            Text(
                                text = char.toString(),
                                style = MaterialTheme.typography.h5,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.66f)
                            )
                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(
                            onClick = onDismissRequest,
                        ) {
                            Text(text = "Close")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PairingCodeDialogPreview() {
    AppTheme {
        PairingCodeDialog(
            pairingCode = "1234",
            onDismissRequest = {}
        )
    }
}