package nl.eduid.wallet.ui.home.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import nl.eduid.wallet.R
import nl.eduid.wallet.shared.presentation.compose.theme.AppTheme

@Composable
fun HomeScreen(
    modifier: Modifier,
    onScanQrClick: () -> Unit,
    onYourDateClick: () -> Unit,
    onActivityClick: () -> Unit
) {

    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController) {
        systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = true)
        onDispose {}
    }

    ConstraintLayout(modifier = modifier) {

        val (logo, relax, leafs, lady, buttons) = createRefs()

        Image(
            modifier = Modifier
                .width(148.dp)
                .constrainAs(logo) {
                    top.linkTo(parent.top, 58.dp)
                    centerHorizontallyTo(parent)
                },
            painter = painterResource(id = R.drawable.ic_splash_logo),
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .constrainAs(relax) {
                    bottom.linkTo(leafs.top, 40.dp)
                    centerHorizontallyTo(parent)
                },
            text = buildAnnotatedString {

                withStyle(SpanStyle(color = Color(0xFF008738))) {
                    append("Relax")
                }

                append('\n')

                append("all is well")
            },
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
        )

        Image(
            modifier = Modifier
                .constrainAs(leafs) {
                    bottom.linkTo(buttons.top)
                    centerHorizontallyTo(parent)
                },
            painter = painterResource(id = R.drawable.meditation_bg),
            contentDescription = null
        )

        HomeButtons(
            modifier = Modifier
                .constrainAs(buttons) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                },
            onScanQrClick,
            onYourDateClick,
            onActivityClick
        )

        Image(
            modifier = Modifier.constrainAs(lady) {
                bottom.linkTo(buttons.top, margin = (-28).dp)
                centerHorizontallyTo(parent)
            },
            painter = painterResource(id = R.drawable.meditation_fg),
            contentDescription = null
        )

    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            HomeScreen(
                modifier = Modifier.fillMaxSize(),
                onScanQrClick = {},
                onYourDateClick = {}
            ) {}
        }
    }
}