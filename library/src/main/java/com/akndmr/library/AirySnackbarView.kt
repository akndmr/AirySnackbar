package com.akndmr.library

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Outline
import android.graphics.Path
import android.graphics.RectF
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import com.akndmr.library.databinding.ItemAirySnackbarBinding
import com.google.android.material.snackbar.ContentViewCallback
import kotlin.math.min

class AirySnackbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), ContentViewCallback {


    private var binding: ItemAirySnackbarBinding

    private var snackBarType: AirySnackbarType = Type.Default

    private var roundPercent = FULL_ROUNDED

    private var forceIconTint = false
    private var forceTextColor = false
    private var path: Path? = null
    private var rect: RectF? = null
    private var viewOutlineProvider: ViewOutlineProvider? = null
    private var mediaPlayer: MediaPlayer? = null

    init {
        binding = ItemAirySnackbarBinding.inflate(LayoutInflater.from(context), this, true)

        if (background == null) {
            setBackgroundResource(R.drawable.selector_snackbar_type)
        }

        setRoundPercent()

        clipToPadding = false
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray? {
        val drawableState = super.onCreateDrawableState(extraSpace + 5)
        mergeDrawableStates(drawableState, getTypeAttr())
        return drawableState
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()

        background?.let { bg ->
            bg.state = drawableState
            invalidate()
        }
    }

    private fun getTypeAttr(): IntArray {
        return when (snackBarType) {
            Type.Success -> TYPE_SUCCESS
            Type.Error -> TYPE_ERROR
            Type.Warning -> TYPE_WARNING
            Type.Info -> TYPE_INFO
            else -> TYPE_DEFAULT
        }
    }

    fun setIcon(
        @DrawableRes iconRes: Int
    ) {
        binding.imageViewIcon.setImageResource(iconRes)
    }

    fun setIconColor(
        @ColorRes iconTint: Int = R.color.white,
        forceTintColor: Boolean = false
    ) {
        forceIconTint = forceTintColor
        val resolvedColor = ContextCompat.getColor(context, iconTint)
        ImageViewCompat.setImageTintList(
            binding.imageViewIcon,
            ColorStateList.valueOf(resolvedColor)
        )
    }

    fun setText(
        message: String
    ) {
        binding.textViewMessage.text = message
    }

    fun setTextColor(
        @ColorRes textColor: Int = R.color.midnight,
        forceTextColor: Boolean = false
    ) {
        this.forceTextColor = forceTextColor
        val resolvedColor = ContextCompat.getColor(context, textColor)
        binding.textViewMessage.setTextColor(resolvedColor)
    }

    fun setTextSize(size: Float) {
        binding.textViewMessage.textSize = size
    }

    fun setIconVisibility(isVisible: Boolean) {
        binding.imageViewIcon.isVisible = isVisible
    }

    fun setSnackBarType(type: AirySnackbarType) {
        snackBarType = type
        when (type) {
            is Type.Custom -> {
                val bgColor = ContextCompat.getColor(context, type.bgColor)
                backgroundTintList = ColorStateList.valueOf(bgColor)
            }
            else -> {}
        }

        refreshDrawableState()
    }

    fun setRoundPercent(round: Float = HALF_ROUNDED) {
        val isChanged = roundPercent != round
        roundPercent = round
        if (roundPercent != NOT_ROUNDED) {
            if (path == null) {
                path = Path()
            }
            if (rect == null) {
                rect = RectF()
            }
            if (viewOutlineProvider == null) {
                viewOutlineProvider = object : ViewOutlineProvider() {
                    override fun getOutline(view: View, outline: Outline) {
                        val radius: Float = min(width, height) * roundPercent / 2
                        outline.setRoundRect(0, 0, width, height, radius)
                    }
                }
                outlineProvider = viewOutlineProvider
            }
            clipToOutline = true

            val radius: Float = min(width, height) * roundPercent / 2

            rect?.set(0f, 0f, width.toFloat(), height.toFloat())
            path?.run {
                reset()
                addRoundRect(rect ?: return, radius, radius, Path.Direction.CW)
            }
        } else {
            clipToOutline = false
        }

        if (isChanged) {
            invalidateOutline()
        }
    }
    fun playSound(soundResId: Int? = null, volumeLevel: VolumeLevel = VolumeLevel.of(0.15f)) {
        try {
            mediaPlayer?.release()

            val finalSoundResId = soundResId ?: R.raw.airy_default
            mediaPlayer = MediaPlayer.create(context, finalSoundResId).apply {
                setVolume(volumeLevel.value, volumeLevel.value)
                setOnCompletionListener { player -> player.release() }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun animateContentIn(delay: Int, duration: Int) {}

    override fun animateContentOut(delay: Int, duration: Int) {}

    companion object {
        private val TYPE_ERROR = intArrayOf(R.attr.type_error)
        private val TYPE_WARNING = intArrayOf(R.attr.type_warning)
        private val TYPE_INFO = intArrayOf(R.attr.type_info)
        private val TYPE_SUCCESS = intArrayOf(R.attr.type_success)
        private val TYPE_DEFAULT = intArrayOf(R.attr.type_default)

        const val FULL_ROUNDED = 1f
        const val HALF_ROUNDED = .5f
        const val NOT_ROUNDED = 0f
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}