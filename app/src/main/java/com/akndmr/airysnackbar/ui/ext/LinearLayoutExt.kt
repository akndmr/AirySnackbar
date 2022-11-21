package com.akndmr.airysnackbar.ui.ext

import android.graphics.Outline
import android.graphics.Path
import android.graphics.RectF
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.akndmr.library.AirySnackbarView
import com.akndmr.library.R
import com.akndmr.library.Type
import kotlin.math.min

fun LinearLayout.backgroundColor(type: Type) {
    when (type) {
        Type.Success -> R.color.greeny
        Type.Info -> R.color.ocean
        Type.Warning -> R.color.carrot
        Type.Error -> R.color.bloody
        is Type.Custom -> R.color.mustardy
        Type.Default -> R.color.midnight
    }.also { color ->
        setBackgroundColor(
            ContextCompat.getColor(
                this.context,
                color
            )
        )
    }
}

fun LinearLayout.setRoundPercent(roundPercent: Float) {
    var path: Path? = null
    var rect: RectF? = null
    var viewOutlineProvider: ViewOutlineProvider? = null

    if (roundPercent != AirySnackbarView.NOT_ROUNDED) {
        path = Path()
        rect = RectF()
        viewOutlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val radius: Float = min(width, height) * roundPercent / 2
                outline.setRoundRect(0, 0, width, height, radius)
            }
        }
        outlineProvider = viewOutlineProvider
        clipToOutline = true

        val radius: Float = min(width, height) * roundPercent / 2

        rect.set(0f, 0f, width.toFloat(), height.toFloat())
        path.apply {
            reset()
            addRoundRect(rect ?: return, radius, radius, Path.Direction.CW)
        }
    } else {
        clipToOutline = false
    }

    invalidateOutline()
}