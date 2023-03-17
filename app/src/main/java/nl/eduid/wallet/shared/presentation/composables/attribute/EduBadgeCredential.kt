package nl.eduid.wallet.shared.presentation.composables.attribute

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.datetime.LocalDate
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme
import java.time.Month

@Composable
fun EduBadgeCredential(
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: String,
    issuer: String,
    issuerLogoUrl: String,
    issueDate: LocalDate
) {
    Card(
        modifier = modifier
            .widthIn(min = 246.dp)
            .heightIn(min = 338.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFFF4F6F8)
    ) {
        Column(
            modifier = Modifier
                .padding(2.dp)
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
        ) {

            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .weight(0.5f)
            ) {

                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.TopCenter),
                    text = formatDate(issueDate),
                )

                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth(),
                    model = imageUrl,
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(8.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 1.1.em
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {

                    AsyncImage(
                        modifier = Modifier.background(MaterialTheme.colors.surface).size(32.dp),
                        model = issuerLogoUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {

                        val style = TextStyle.Default.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = "Issued by",
                            style = style,
                            color = Color(0xFF6E2B7F)
                        )

                        Text(
                            text = issuer,
                            style = style
                        )

                        Text(
                            text = "($issuer)",
                            fontSize = 10.sp,
                            lineHeight = 1.1.em
                        )
                    }
                }
            }
        }
    }
}

private fun formatDate(date: LocalDate): String {

    val month = when (date.month) {
        Month.JANUARY -> "Jan"
        Month.FEBRUARY -> "Feb"
        Month.MARCH -> "Mar"
        Month.APRIL -> "Apr"
        Month.MAY -> "May"
        Month.JUNE -> "Jun"
        Month.JULY -> "Jul"
        Month.AUGUST -> "Aug"
        Month.SEPTEMBER -> "Sep"
        Month.OCTOBER -> "Oct"
        Month.NOVEMBER -> "Nov"
        Month.DECEMBER -> "Dec"
    }

    val dayOfMonth = date.dayOfMonth
    val year = date.year

    return "$month $dayOfMonth, $year"
}

@Preview
@Composable
fun EduBadgeCredentialPreview() {
    AppTheme {
        EduBadgeCredential(
            title = "Advanced Food Physics - Reology and fracture of soft solids",
            imageUrl = "",
            issuer = "Wagenigen University & Research - Professional Education",
            issuerLogoUrl = "",
            issueDate = LocalDate(2022, Month.NOVEMBER, 21)
        )
    }
}