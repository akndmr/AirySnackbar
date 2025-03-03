package com.akndmr.library

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
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

    private val defaultVerticalPadding: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.padding_small)

    private var airyGravity: Int = Gravity.TOP or Gravity.CENTER_HORIZONTAL

    init {
        var margins = SizeAttribute.Margin()

        getView().apply {
            setBackgroundColor(
                ContextCompat.getColor(view.context, android.R.color.transparent)
            )

            with(model) {
                snackbarLayoutAttribute.onEach { attr ->
                    when (attr) {
                        is GravityAttribute.Top -> {
                            airyGravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                        }
                        is GravityAttribute.Bottom -> {
                            airyGravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                        }
                        is AnimationAttribute.FadeInOut -> {
                            animationMode = ANIMATION_MODE_FADE
                        }
                        is AnimationAttribute.SlideInOut -> {
                            animationMode = ANIMATION_MODE_SLIDE
                        }
                        is SizeAttribute.Padding -> {
                            setContentPaddings(attr)
                        }
                        is SizeAttribute.Margin -> {
                            margins = attr
                        }
                    }
                }

                view?.let { view ->
                    applyWindowInsets(view, margins)
                }
            }

            setGravity()
        }
    }

    private fun applyWindowInsets(view: View, margins: SizeAttribute.Margin) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val navigationBar =
                insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            val factor = if (margins.unit == SizeUnit.DP) {
                content.context.resources.displayMetrics.density
            } else {
                1f
            }

            updateBaseMargins(
                left = margins.left.times(factor).toInt(),
                top = statusBar.top + margins.top.times(factor).toInt(),
                right = margins.right.times(factor).toInt(),
                bottom = navigationBar.bottom + margins.bottom.times(factor).toInt()
            )

            ViewCompat.onApplyWindowInsets(v, insets)
        }
    }

    private fun updateBaseMargins(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        getView().apply {
            if (layoutParams !is MarginLayoutParams) return@apply

            (layoutParams as MarginLayoutParams).apply {
                bottomMargin = bottom + defaultTopAndBottomMargin
                topMargin = top + defaultTopAndBottomMargin
                leftMargin = left
                rightMargin = right
            }.also { layoutParams = it }
        }
    }

    private fun setGravity() {
        getView().apply {
            (layoutParams as ViewGroup.LayoutParams).apply {
                when (this) {
                    is CoordinatorLayout.LayoutParams -> gravity = airyGravity
                    is FrameLayout.LayoutParams -> gravity = airyGravity
                }
            }.also { layoutParams = it }
        }
    }

    private fun setContentPaddings(padding: SizeAttribute.Padding) {
        val factor = if (padding.unit == SizeUnit.DP) {
            content.context.resources.displayMetrics.density
        } else {
            1f
        }

        with(content) {
            val leftPadding = padding.left.times(factor).takeIf {
                it > 0f
            } ?: paddingLeft
            val rightPadding = padding.right.times(factor).takeIf {
                it > 0f
            } ?: paddingRight
            val topPadding = padding.top.times(factor).takeIf {
                it > 0f
            } ?: defaultVerticalPadding
            val bottomPadding = padding.bottom.times(factor).takeIf {
                it > 0f
            } ?: defaultVerticalPadding

            content.setPadding(
                leftPadding.toInt(),
                topPadding.toInt(),
                rightPadding.toInt(),
                bottomPadding.toInt()
            )
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

            (parent.layoutParams as? LinearLayout.LayoutParams)?.let { params ->
                params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            }

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
                        is TextAttribute.TextSize -> {
                            setTextSize(size = attr.size)
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
                        is RadiusAttribute.Radius -> {
                            snackBarView.setRoundPercent(attr.radius)
                        }
                        is AirySnackbarLayoutAttribute -> {
                            airySnackbarModel.snackbarLayoutAttribute.add(attr)

                            if (attr is SizeAttribute.Margin && source is AirySnackbarSource.ViewSource) {
                                (layoutParams as FrameLayout.LayoutParams).apply {
                                    leftMargin = attr.left
                                    topMargin = attr.top
                                    rightMargin = attr.right
                                    bottomMargin = attr.bottom
                                }.also { params ->
                                    layoutParams = params
                                }
                            }
                        }
                        is SoundAttribute.Custom -> playSound(attr.soundResId)
                        is SoundAttribute.UseDefault -> playSound()
                        is SoundAttribute.NoSound -> { /* Don't play any sound */ }
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