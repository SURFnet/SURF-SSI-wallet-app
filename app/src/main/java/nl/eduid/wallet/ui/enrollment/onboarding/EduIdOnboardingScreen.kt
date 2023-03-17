package nl.eduid.wallet.ui.enrollment.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.composables.button.PrimaryButton
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme

@Composable
internal fun EduIdOnboardingScreen(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit
) {

    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = true)
        onDispose {}
    }

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .padding(horizontal = 38.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_wallet),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(54.dp))

            Text(
                text = stringResource(id = R.string.eduonboarding_heading),
                style = MaterialTheme.typography.h5,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(id = R.string.eduonboarding_body),
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(Modifier.padding(start = 16.dp)) {
                listOf(
                    stringResource(id = R.string.eduonboarding_body_bullet_1),
                    stringResource(id = R.string.eduonboarding_body_bullet_2),
                    stringResource(id = R.string.eduonboarding_body_bullet_3),
                ).forEach { text ->
                    Row {
                        Text(text = "•", style = MaterialTheme.typography.body1)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = text, style = MaterialTheme.typography.body1)
                    }
                }
            }
        }

        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFEF8D3))
            ) {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = stringResource(id = R.string.eduonboarding_hint_activate_wallet),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onButtonClick
            ) {
                Text(text = "Connect your eduID")
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF)
@Composable
internal fun EduIdOnboardingScreenPreview() {
    AppTheme {
        EduIdOnboardingScreen(
            modifier = Modifier.fillMaxSize(),
            onButtonClick = {}
        )
    }
}