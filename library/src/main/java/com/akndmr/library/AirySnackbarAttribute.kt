package com.akndmr.library

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes


sealed interface AirySnackAttribute

sealed class IconAttribute : AirySnackAttribute {
    data class Icon(@DrawableRes val iconRes: Int) : IconAttribute()
    data class IconColor(@ColorRes val iconTint: Int, val forceTintColor: Boolean = false) :
        IconAttribute()

    object NoIcon : IconAttribute()
}

sealed class TextAttribute : AirySnackAttribute {
    data class Text(val text: String) : TextAttribute()
    data class TextColor(
        @ColorRes val textColor: Int,
        val forceTextColor: Boolean = false
    ) : TextAttribute()
}