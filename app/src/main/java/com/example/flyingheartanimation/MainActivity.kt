package com.example.flyingheartanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.flyingheartanimation.ui.theme.FlyingHeartAnimationTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlyingHeartAnimationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Screen()
                }
            }
        }
    }
}

@Composable
fun Screen() {
    val heartCount = remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        repeat(heartCount.value) {
            Heart(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp),
                horizontalPadding = 24,
                bottomMargin = 120
            )
        }

        Button(
            onClick = {
                heartCount.value++
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .wrapContentHeight()
                .wrapContentWidth()
        ) {
            Text(
                text = "Like",
                color = Color.White
            )
        }

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Heart(modifier: Modifier, horizontalPadding: Int, bottomMargin: Int) {
    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp - bottomMargin

    val yRandom = Random.nextInt(0, height / 2)
    val xRandom = Random.nextInt(horizontalPadding, (width - horizontalPadding))

    val state = remember {
        mutableStateOf(HeartState.Show)
    }

    val offsetYAnimation: Dp by animateDpAsState(
        targetValue = when (state.value) {
            HeartState.Show -> height.dp
            else -> yRandom.dp
        },
        animationSpec = tween(1000)
    )

    val offsetXAnimation: Dp by animateDpAsState(
        targetValue = when (state.value) {
            HeartState.Show -> (((width - (horizontalPadding * 2)) / 2) + 8).dp
            else -> xRandom.dp
        },
        animationSpec = tween(1000)
    )

    LaunchedEffect(key1 = state) {
        state.value = when (state.value) {
            HeartState.Show -> HeartState.Hide
            HeartState.Hide -> HeartState.Show
        }
    }

    AnimatedVisibility(
        visible = state.value == HeartState.Show,
        enter = fadeIn(animationSpec = tween(durationMillis = 250)),
        exit = fadeOut(animationSpec = tween(durationMillis = 700))
    ) {
        Canvas(modifier = modifier
            .offset(y = offsetYAnimation, x = offsetXAnimation),
            onDraw = {
                val path = Path().apply {
                    heartPath(Size(120f, 120f))
                }
                drawPath(
                    path = path,
                    color = Color.Red,
                )
            }
        )
    }
}