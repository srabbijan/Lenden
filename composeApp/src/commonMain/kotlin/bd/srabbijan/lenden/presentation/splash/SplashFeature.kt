package bd.srabbijan.lenden.presentation.splash

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import bd.srabbijan.lenden.core.BaseViewModel
import bd.srabbijan.lenden.core.UiEffect
import bd.srabbijan.lenden.core.UiEvent
import bd.srabbijan.lenden.core.UiState
import bd.srabbijan.lenden.navigation.ActivityScreen
import bd.srabbijan.lenden.navigation.AppScreens
import bd.srabbijan.lenden.navigation.NavController
import bd.srabbijan.lenden.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lenden.composeapp.generated.resources.Res
import lenden.composeapp.generated.resources.app_logo
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

class SplashViewModel : BaseViewModel<SplashState, SplashEvent, SplashEffect>(SplashState) {

    init {
        viewModelScope.launch {
            delay(2500)
            setEffect { SplashEffect.NavigateToDashboard }
        }
    }

    override fun onEvent(event: SplashEvent) {
        // No events for splash
    }
}

object SplashState : UiState

sealed interface SplashEvent : UiEvent

sealed interface SplashEffect : UiEffect {
    data object NavigateToDashboard : SplashEffect
}

class SplashScreen() : ActivityScreen<AppScreens.Splash>() {

    @Composable
    override fun InitView(
        navKey: AppScreens.Splash,
        navController: NavController
    ) {
        val viewModel : SplashViewModel = koinViewModel()
        LaunchedEffect(Unit) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    SplashEffect.NavigateToDashboard -> {
                        navController.navigate(AppScreens.Dashboard, true)
                    }
                }
            }
        }
        var scale by remember { mutableStateOf(0.7f) }
        var opacity by remember { mutableStateOf(0f) }

        LaunchedEffect(Unit) {
            // Animate scale
            animate(
                initialValue = 0.7f,
                targetValue = 1.0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) { value, _ ->
                scale = value
            }

            // Animate opacity
            delay(300)
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = tween(500)
            ) { value, _ ->
                opacity = value
            }

        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            AppTheme.colorScheme.primary,
                            AppTheme.colorScheme.cardBgColor,
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.sdp)
            ) {
                // Animated Icon
                Box(
                    modifier = Modifier
                        .size(140.sdp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.app_logo),
                        contentDescription = null,
                        modifier = Modifier.size(80.sdp),
                        tint = Color.White
                    )
                }

                // App Name
                Text(
                    text = "লেনদেন",
                    fontSize = 40.ssp,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colorScheme.cardBgColor,
                    modifier = Modifier.graphicsLayer {
                        alpha = opacity
                    }
                )

//                // Tagline
//                Text(
//                    text = "Organize your life",
//                    fontSize = 16.sp,
//                    color = Color.White.copy(alpha = 0.9f),
//                    modifier = Modifier.graphicsLayer {
//                        alpha = opacity
//                    }
//                )
            }
        }
    }
}
