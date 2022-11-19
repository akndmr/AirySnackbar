package com.akndmr.library

import android.view.View

data class AirySnackbarModel(
    var view: View? = null,
    var snackbarLayoutAttribute: MutableList<AirySnackbarLayoutAttribute> = mutableListOf(),
    var anchorView: View? = null
)