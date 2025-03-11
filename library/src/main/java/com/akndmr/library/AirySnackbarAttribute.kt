package com.akndmr.library

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.google.android.material.snackbar.BaseTransientBottomBar

sealed interface AirySnackbarAttribute

sealed interface AirySnackbarLayoutAttribute

sealed class IconAttribute : AirySnackbarAttribute {
    data class Icon(@DrawableRes val iconRes: Int) : IconAttribute()
    data class IconColor(
        @ColorRes val iconTint: Int, val forceTintColor: Boolean = false
    ) : IconAttribute()

    object NoIcon : IconAttribute()
}

sealed class TextAttribute : AirySnackbarAttribute {
    data class Text(val text: String) : TextAttribute()
    data class TextColor(
        @ColorRes val textColor: Int,
        val forceTextColor: Boolean = false
    ) : TextAttribute()

    data class TextSize(val size: Float) : TextAttribute()
}

sealed class RadiusAttribute : AirySnackbarAttribute {
    data class Radius(val radius: Float) : RadiusAttribute()
}

sealed class GravityAttribute : AirySnackbarAttribute, AirySnackbarLayoutAttribute {
    object Top : GravityAttribute()
    object Bottom : GravityAttribute()
}

sealed class AnimationAttribute : AirySnackbarAttribute, AirySnackbarLayoutAttribute {
    object SlideInOut : AnimationAttribute()
    object FadeInOut : AnimationAttribute()
}

sealed class SizeAttribute : AirySnackbarAttribute, AirySnackbarLayoutAttribute {
    data class Margin(
        val left: Int = 0,
        val top: Int = 0,
        val right: Int = 0,
        val bottom: Int = 0,
        val unit: SizeUnit = SizeUnit.DP
    ) : SizeAttribute()

    data class Padding(
        val left: Int = 0,
        val top: Int = 0,
        val right: Int = 0,
        val bottom: Int = 0,
        val unit: SizeUnit = SizeUnit.DP
    ) : SizeAttribute()
}
sealed class SoundAttribute : AirySnackbarAttribute {
    data class Volume(val volumeLevel: VolumeLevel) : SoundAttribute()
    data class Custom(
        val soundResId: Int,
        val volumeLevel: VolumeLevel = VolumeLevel.of(0.15f)
    ) : SoundAttribute()
    object UseDefault : SoundAttribute()
}

sealed class SwipeAttribute : AirySnackbarAttribute, AirySnackbarLayoutAttribute {
    data class Enable(
        val dismissThreshold: Float = SwipeDismissTouchListener.DEFAULT_DISMISS_THRESHOLD,
        val animationDuration: Long = SwipeDismissTouchListener.DEFAULT_ANIMATION_DURATION,
        val minAlpha: Float = SwipeConfig.DEFAULT_MIN_ALPHA,
        val maxAlpha: Float = SwipeConfig.DEFAULT_MAX_ALPHA,
        val alphaProgressFactor: Float = SwipeConfig.DEFAULT_ALPHA_PROGRESS_FACTOR,
        val swipeDirection: SwipeConfig.SwipeDirection = SwipeConfig.SwipeDirection.BOTH
    ) : SwipeAttribute()

    object Disable : SwipeAttribute()
}

sealed class DurationAttribute : AirySnackbarAttribute {
    object Indefinite : DurationAttribute()
    object Short : DurationAttribute()
    object Long : DurationAttribute()
    data class Custom(val durationMs: Int) : DurationAttribute()

    fun toDuration(): Int = when (this) {
        is Indefinite -> BaseTransientBottomBar.LENGTH_INDEFINITE
        is Short -> BaseTransientBottomBar.LENGTH_SHORT
        is Long -> BaseTransientBottomBar.LENGTH_LONG
        is Custom -> durationMs
    }
}

enum class SizeUnit {
    DP,
    PX
}