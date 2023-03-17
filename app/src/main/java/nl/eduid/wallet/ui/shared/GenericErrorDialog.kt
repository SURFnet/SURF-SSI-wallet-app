package nl.eduid.wallet.ui.shared

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
internal fun GenericErrorDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = "Ok")
            }
        },
        title = {
            Text(text = "Error occurred")
        },
        text = {
            Text(text = "Something went wrong")
        }
    )
}