package com.akndmr.library

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.akndmr.library.extensions.findSuitableParent
import com.google.android.material.snackbar.BaseTransientBottomBar


class AirySnackbar(
    parent: ViewGroup,
    content: AirySnackbarView
) : BaseTransientBottomBar<AirySnackbar>(parent, content, content) {

    private val topMargin = content.resources.getDimensionPixelSize(R.dimen.snackbar_top_margin)
    private val minStatusBarHeight =
        content.resources.getDimensionPixelSize(R.dimen.min_status_bar_height)
    private val topSpace = topMargin

    init {
        getView().apply {
            setBackgroundColor(
                ContextCompat.getColor(view.context, android.R.color.transparent)
            )
            setPadding(NO_PADDING, topSpace, NO_PADDING, NO_PADDING)

            val params = layoutParams as ViewGroup.LayoutParams
            val gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            when (params) {
                is CoordinatorLayout.LayoutParams -> {
                    params.gravity = gravity
                }
                is FrameLayout.LayoutParams -> {
                    params.gravity = gravity
                }
            }
            layoutParams = params
            animationMode = ANIMATION_MODE_FADE
        }
    }

    companion object {

        private const val NO_PADDING = 0

        fun make(
            activity: Activity,
            type: AirySnackbarType,
            attributes: List<AirySnackAttribute>
        ): AirySnackbar? {
            return activity.window?.decorView?.let { root ->
                make(root, type, attributes).apply {
                    getView().setPadding(NO_PADDING, minStatusBarHeight, NO_PADDING, NO_PADDING)
                }
            }
        }

        // For DialogFragments (BottomSheets, etc)
        fun make(
            dialog: Dialog,
            type: AirySnackbarType,
            attributes: List<AirySnackAttribute>
        ): AirySnackbar? {
            return dialog.window?.decorView?.let { root ->
                make(root, type, attributes).apply {
                    getView().setPadding(NO_PADDING, minStatusBarHeight, NO_PADDING, NO_PADDING)
                }
            }
        }

        @SuppressWarnings()
        fun make(
            view: View,
            type: AirySnackbarType,
            attributes: List<AirySnackAttribute>
        ): AirySnackbar {
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "Could not find a parent view for ${AirySnackbar.javaClass.simpleName}."
            )

            val snackBarView = LayoutInflater.from(view.context).inflate(
                R.layout.layout_airy_snackbar,
                parent,
                false
            ) as AirySnackbarView

            with(snackBarView) {
                val params = layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                layoutParams = params
                orientation = LinearLayout.HORIZONTAL

                setSnackBarType(type)

                attributes.forEach { attr ->
                    when (attr) {
                        is TextAttribute.Text -> {
                            setText(attr.text)
                        }
                        is TextAttribute.TextColor -> {
                            setTextColor(
                                textColor = attr.textColor, forceTextColor = attr.forceTextColor
                            )
                        }
                        is IconAttribute.NoIcon -> {
                            setIconVisibility(isVisible = false)
                        }
                        is IconAttribute.Icon -> {
                            setIcon(iconRes = attr.iconRes)
                        }
                        is IconAttribute.IconColor -> {
                            setIconColor(
                                iconTint = attr.iconTint, forceTintColor = attr.forceTintColor
                            )
                        }
                        is SizeAttribute.Padding -> {
                            val leftPadding = attr.left.takeIf {
                                it > 0
                            } ?: paddingLeft
                            val rightPadding = attr.right.takeIf {
                                it > 0
                            } ?: paddingRight
                            val topPadding = attr.top.takeIf {
                                it > 0
                            } ?: paddingTop
                            val bottomPadding = attr.bottom.takeIf {
                                it > 0
                            } ?: paddingBottom

                            setPadding(leftPadding, topPadding, rightPadding, bottomPadding)
                        }
                        is SizeAttribute.Margin -> {
                            val params = (layoutParams as FrameLayout.LayoutParams).apply {
                                leftMargin = attr.left
                                topMargin = attr.top
                                rightMargin = attr.right
                                bottomMargin = attr.bottom
                            }
                            layoutParams = params
                        }
                    }
                }
            }

            return AirySnackbar(
                parent,
                snackBarView
            )
        }
    }
}