package com.akndmr.airysnackbar.ui.ext

import android.content.Context
import android.util.TypedValue

fun Context.getDimensionDip(value: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value, this.resources.displayMetrics
    )
}

fun Context.getDimensionSp(value: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, value, this.resources.displayMetrics
    )
}