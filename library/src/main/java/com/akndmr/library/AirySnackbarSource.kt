package com.akndmr.library

import android.app.Activity
import android.app.Dialog
import android.view.View

sealed class AirySnackbarSource {
    data class ActivitySource(val activity: Activity) : AirySnackbarSource()
    data class DialogSource(val dialog: Dialog) : AirySnackbarSource()
    data class ViewSource(val view: View) : AirySnackbarSource()
}