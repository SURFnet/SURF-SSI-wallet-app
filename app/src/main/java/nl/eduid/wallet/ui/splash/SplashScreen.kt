package nl.eduid.wallet.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.compose.theme.nunitoFamily
import nl.eduid.wallet.ui.splash.theme.SplashTheme

@Composable
internal fun SplashScreen(
    modifier: Modifier = Modifier,
) {

    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
        onDispose {}
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.background
    ) {
        Box(Modifier.systemBarsPadding()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(50.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_splash_logo),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = stringResource(id = R.string.splash_motto),
                    fontFamily = nunitoFamily,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 1.5.em
                )
            }

            Image(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                painter = painterResource(id = R.drawable.ic_surf_logo),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashTheme {
        SplashScreen(Modifier.fillMaxSize())
    }
}