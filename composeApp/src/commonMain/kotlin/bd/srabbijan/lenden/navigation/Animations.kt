package bd.srabbijan.lenden.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

@OptIn(ExperimentalAnimationApi::class)
object Animations {

    const val DURATION = 450

    val enterTransition = slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(DURATION)
    )

    val exitTransition = slideOutHorizontally(
        targetOffsetX = { fullWidth -> -fullWidth / 4 },
        animationSpec = tween(DURATION)
    )

    val popEnterTransition = slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth / 4 },
        animationSpec = tween(DURATION)
    )

    val popExitTransition = slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(DURATION)
    )

    val transitionSpec = enterTransition togetherWith exitTransition

    val popTransitionSpec = popEnterTransition togetherWith popExitTransition


    val forwardTransition: ContentTransform =
        ContentTransform(
            enterTransition,
            exitTransition,
            sizeTransform = SizeTransform(clip = false)
        )

    val backTransition: ContentTransform =
        ContentTransform(
            popEnterTransition,
            popExitTransition,
            sizeTransform = SizeTransform(clip = false)
        )

}