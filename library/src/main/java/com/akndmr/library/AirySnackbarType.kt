package com.akndmr.library

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes


sealed interface AirySnackbarType

sealed class Type : AirySnackbarType {
    object Success : Type()
    object Error : Type()
    object Info : Type()
    object Warning : Type()
    object Default : Type()
    data class Custom(@ColorRes val bgColor: Int) : Type()
}