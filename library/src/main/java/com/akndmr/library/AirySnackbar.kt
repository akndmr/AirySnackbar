package com.akndmr.library

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.akndmr.library.extensions.findSuitableParent
import com.google.android.material.snackbar.BaseTransientBottomBar

class AirySnackbar(
    parent: ViewGroup,
    private val content: AirySnackbarView,
    model: AirySnackbarModel
) : BaseTransientBottomBar<AirySnackbar>(parent, content, content) {

    private val defaultTopAndBottomMargin: Int
        get() = content.context.resources.getDimensionPixelSize(
            R.dimen.snackbar_top_and_bottom_margin
        )

    private val defaultVerticalPadding =
        context.resources.getDimensionPixelSize(R.dimen.padding_small)

    init {
        getView().apply {
            setBackgroundColor(
                ContextCompat.getColor(view.context, android.R.color.transparent)
            )

            (layoutParams as ViewGroup.LayoutParams).apply {
                when (this) {
                    is CoordinatorLayout.LayoutParams -> gravity = model.gravity
                    is FrameLayout.LayoutParams -> gravity = model.gravity
                }
            }.also { layoutParams = it }

            animationMode = model.animationMode

            with(model) {
                view?.let { view ->
                    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
                        val statusBar = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        val navigationBar =
                            insets.getInsets(WindowInsetsCompat.Type.navigationBars())

                        updateMargins(statusBar.top, navigationBar.bottom)

                        ViewCompat.onApplyWindowInsets(v, insets)
                    }
                }

                padding.apply {
                    val leftPadding = left.takeIf {
                        it > 0
                    } ?: paddingLeft
                    val rightPadding = right.takeIf {
                        it > 0
                    } ?: paddingRight
                    val topPadding = top.takeIf {
                        it > 0
                    } ?: defaultVerticalPadding
                    val bottomPadding = bottom.takeIf {
                        it > 0
                    } ?: defaultVerticalPadding

                    setPadding(leftPadding, topPadding, rightPadding, bottomPadding)
                }
            }
        }
    }

    private fun updateMargins(top: Int, bottom: Int) {
        getView().apply {
            if (layoutParams !is MarginLayoutParams) return

            (layoutParams as MarginLayoutParams).apply {
                bottomMargin = bottom + defaultTopAndBottomMargin
                topMargin = top + defaultTopAndBottomMargin
            }.also { layoutParams = it }
        }
    }

    companion object {

        @SuppressWarnings()
        fun make(
            source: AirySnackbarSource,
            type: AirySnackbarType,
            attributes: List<AirySnackbarAttribute>
        ): AirySnackbar {

            val view = when (source) {
                is AirySnackbarSource.ActivitySource -> {
                    source.activity.window?.decorView
                }
                is AirySnackbarSource.DialogSource -> {
                    source.dialog.window?.decorView
                }
                is AirySnackbarSource.ViewSource -> {
                    source.view
                }
            } ?: throw IllegalArgumentException(
                "${AirySnackbar::class.java.simpleName} source view is null"
            )

            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "Could not find a parent view for ${AirySnackbar::class.java.simpleName}."
            )

            val snackBarView = LayoutInflater.from(view.context).inflate(
                R.layout.layout_airy_snackbar,
                parent,
                false
            ) as AirySnackbarView


            val airySnackbarModel: AirySnackbarModel by lazy { AirySnackbarModel() }

            with(snackBarView) {
                val params = layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL

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
                            airySnackbarModel.padding = attr
                        }
                        is RadiusAttribute.Radius -> {
                            snackBarView.setRoundPercent(attr.radius)
                        }
                        is SizeAttribute.Margin -> {
                            (layoutParams as FrameLayout.LayoutParams).apply {
                                leftMargin = attr.left
                                topMargin = attr.top
                                rightMargin = attr.right
                                bottomMargin = attr.bottom
                            }.also { layoutParams = it }
                        }
                        is GravityAttribute.Top -> {
                            airySnackbarModel.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                        }
                        is GravityAttribute.Bottom -> {
                            airySnackbarModel.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                        }
                        is AnimationAttribute.FadeInOut -> {
                            airySnackbarModel.animationMode = ANIMATION_MODE_FADE
                        }
                        is AnimationAttribute.SlideInOut -> {
                            airySnackbarModel.animationMode = ANIMATION_MODE_SLIDE
                        }
                    }
                }
            }

            return AirySnackbar(
                parent,
                snackBarView,
                airySnackbarModel.apply { this.view = view }
            )
        }
    }
}