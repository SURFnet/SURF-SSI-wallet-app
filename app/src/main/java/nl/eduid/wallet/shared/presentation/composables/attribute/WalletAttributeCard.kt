package nl.eduid.wallet.shared.presentation.composables.attribute

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeBorder
import nl.eduid.wallet.shared.presentation.compose.theme.color.walletAttributeOnSurface

@Composable
fun WalletAttributeCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.surface,
    borderColor: Color,
    contentColor: Color = MaterialTheme.colors.onSurface,
    topContent: @Composable ColumnScope.() -> Unit,
    bottomContent: @Composable ColumnScope.() -> Unit,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .heightIn(min = 71.dp),
        shape = RoundedCornerShape(6.dp),
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(Modifier.weight(1f)) {
                topContent()
                Spacer(modifier = Modifier.height(4.dp))
                bottomContent()
            }

            if (trailingContent != null) {
                Spacer(modifier = Modifier.width(10.dp))
                trailingContent()
            }
        }
    }
}

@Preview
@Composable
fun WalletAttributeCardPreview() {
    AppTheme {
        WalletAttributeCard(
            borderColor = MaterialTheme.colors.walletAttributeBorder,
            topContent = {
                Text(
                    text = "René v. Hamersdonk",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
            },
            bottomContent = {
                Text(
                    text = buildAnnotatedString {
                        append("Provided by ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Universiteit van Amsterdam")
                        }
                    },
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.walletAttributeOnSurface
                )
            })
    }
}