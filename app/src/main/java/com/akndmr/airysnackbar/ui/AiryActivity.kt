package com.akndmr.airysnackbar.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.akndmr.airysnackbar.R
import com.akndmr.airysnackbar.databinding.ActivityAiryBinding
import com.akndmr.airysnackbar.ui.ext.backgroundColor
import com.akndmr.airysnackbar.ui.ext.getDimensionDip
import com.akndmr.airysnackbar.ui.ext.px
import com.akndmr.airysnackbar.ui.ext.setRoundPercent
import com.akndmr.library.*
import com.akndmr.library.R as LibR

class AiryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAiryBinding

    private var type: Type = Type.Success

    private var attributes: MutableMap<Int, AirySnackbarAttribute> = mutableMapOf()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()

        with(binding) {
            chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
                var icon = R.drawable.ic_success
                var iconColor = R.color.white
                when (checkedIds.first()) {
                    R.id.chip_success -> {
                        type = Type.Success
                        icon = R.drawable.ic_success
                        iconColor = R.color.white
                    }
                    R.id.chip_info -> {
                        type = Type.Info
                        icon = LibR.drawable.ic_info
                        iconColor = R.color.white
                    }
                    R.id.chip_warning -> {
                        type = Type.Warning
                        icon = R.drawable.ic_warning2
                        iconColor = R.color.white
                    }
                    R.id.chip_error -> {
                        type = Type.Error
                        icon = R.drawable.ic_error2
                        iconColor = R.color.white
                    }
                    R.id.chip_custom -> {
                        type = Type.Custom(bgColor = LibR.color.mustardy)
                        icon = R.drawable.ic_custom
                        iconColor = LibR.color.midnight
                    }
                }
                imageViewIcon.setImageResource(icon)
                preview(attr = IconAttribute.IconColor(iconTint = iconColor))
                preview(attr = IconAttribute.Icon(iconRes = icon))
            }

            rangeSliderMargin.addOnChangeListener { _, value, _ ->
                value.toInt().also { margin ->
                    SizeAttribute.Margin(
                        left = margin, right = margin, top = margin, bottom = margin
                    ).also { preview(it) }
                }

                val marginDp = getDimensionDip(value).toInt()

                ConstraintSet().apply {
                    clone(previewContainer)
                    setMargin(R.id.preview, ConstraintSet.BOTTOM, marginDp)
                    setMargin(R.id.preview, ConstraintSet.START, marginDp)
                    setMargin(R.id.preview, ConstraintSet.END, marginDp)
                    applyTo(previewContainer)
                }
            }

            rangeSliderPadding.apply {
                addOnChangeListener { _, value, _ ->
                    value.toInt().also { padding ->
                        SizeAttribute.Padding(
                            left = padding, right = padding, top = padding, bottom = padding
                        ).also { preview(it) }
                    }
                }
            }

            rangeSliderRadius.apply {
                addOnChangeListener { _, value, _ ->
                    value.also { radius ->
                        RadiusAttribute.Radius(radius = radius).also { preview(it) }
                    }
                }
            }

            rangeSliderTextSize.addOnChangeListener { _, value, _ ->
                textViewMessage.textSize = value
                TextAttribute.TextSize(size = value).also { preview(it) }
            }

            buttonShow.setOnClickListener {
                AirySnackbar.make(
                    source = AirySnackbarSource.ActivitySource(this@AiryActivity),
                    type = type,
                    attributes = attributes.map { it.value }
                ).show()
            }
        }
        initPreview()
    }

    private fun initBinding() {
        binding = ActivityAiryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initPreview() {
        with(binding) {
            preview.setRoundPercent(.5f)
            chipGroup.check(R.id.chip_success)
            rangeSliderRadius.setValues(.5f)
            rangeSliderMargin.setValues(8f)
            rangeSliderPadding.setValues(8f)
            rangeSliderTextSize.setValues(14f)
            preview(
                TextAttribute.Text(
                    text = "Airy Snackbar message a little bit longer than what you could expect"
                )
            )
        }
    }

    private fun preview(attr: AirySnackbarAttribute? = null) {
        with(binding) {

            preview.backgroundColor(type)

            when (attr) {
                is TextAttribute.Text -> {
                    textViewMessage.text = attr.text
                    attributes.put(0, attr)
                }
                is TextAttribute.TextColor -> {
                    val resolvedColor =
                        ContextCompat.getColor(this@AiryActivity, attr.textColor)
                    textViewMessage.setTextColor(resolvedColor)
                    attributes.put(1, attr)
                }
                is TextAttribute.TextSize -> {
                    attributes.put(2, attr)
                }
                is IconAttribute.NoIcon -> {
                    imageViewIcon.isVisible = false
                    attributes.put(3, attr)
                }
                is IconAttribute.Icon -> {
                    imageViewIcon.setImageResource(attr.iconRes)
                    attributes.put(4, attr)
                }
                is IconAttribute.IconColor -> {
                    imageViewIcon.imageTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@AiryActivity,
                                attr.iconTint
                            )
                        )
                    attributes.put(8, attr)
                }
                is RadiusAttribute.Radius -> {
                    preview.setRoundPercent(attr.radius)
                    attributes.put(5, attr)
                }
                is SizeAttribute.Padding -> {
                    preview.setPadding(
                        attr.left.px.toInt(),
                        attr.top.px.toInt(),
                        attr.right.px.toInt(),
                        attr.bottom.px.toInt()
                    )
                    attributes.put(6, attr)
                }
                is SizeAttribute.Margin -> {
                    attributes.put(7, attr)
                }
                else -> {}
            }
        }
    }
}