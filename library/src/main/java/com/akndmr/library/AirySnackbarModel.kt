package com.akndmr.library

import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar

data class AirySnackbarModel(
    var view: View? = null,
    var snackbarLayoutAttribute: MutableList<AirySnackbarLayoutAttribute> = mutableListOf(),
    var anchorView: View? = null,
    var duration: Int = BaseTransientBottomBar.LENGTH_SHORT
)