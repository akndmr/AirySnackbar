package com.akndmr.library

import android.view.Gravity
import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar

data class AirySnackbarModel(
    var view: View? = null,
    var padding: SizeAttribute.Padding = SizeAttribute.Padding(0, 0, 0, 0),
    var gravity: Int = Gravity.TOP or Gravity.CENTER_HORIZONTAL,
    var animationMode: Int = BaseTransientBottomBar.ANIMATION_MODE_FADE,
    var anchorView: View? = null
)