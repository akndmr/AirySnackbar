package com.akndmr.airysnackbar.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akndmr.airysnackbar.R
import com.akndmr.airysnackbar.databinding.ActivitySampleBinding
import com.akndmr.library.*

class SampleUsageActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySampleBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        binding = ActivitySampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            buttonSuccess.setOnClickListener {
                AirySnackbar.make(
                    source = AirySnackbarSource.ActivitySource(activity = this@SampleUsageActivity),
                    type = Type.Success,
                    attributes =
                    listOf(
                        TextAttribute.Text(text = "Successful AirySnackbar sample some longer text with horizontal padding 16dp and margin 24dp"),
                        SizeAttribute.Padding(
                            left = resources.getDimensionPixelSize(R.dimen.sample_margin_small),
                            right = resources.getDimensionPixelSize(R.dimen.sample_margin_small),
                            unit = SizeUnit.PX
                        ),
                        SizeAttribute.Margin(
                            right = resources.getDimensionPixelSize(R.dimen.sample_margin_medium),
                            left = resources.getDimensionPixelSize(R.dimen.sample_margin_medium),
                            unit = SizeUnit.PX
                        )
                    )
                ).show()
            }

            buttonError.setOnClickListener {
                AirySnackbar.make(
                    source = AirySnackbarSource.ActivitySource(activity = this@SampleUsageActivity),
                    type = Type.Error,
                    attributes =
                    listOf(
                        TextAttribute.Text(text = "Error AirySnackbar, default padding and margin"),
                        IconAttribute.Icon(iconRes = R.drawable.ic_error)
                    )
                ).show()
            }

            buttonWarning.setOnClickListener {
                AirySnackbar.make(
                    source = AirySnackbarSource.ActivitySource(activity = this@SampleUsageActivity),
                    type = Type.Warning,
                    attributes =
                    listOf(
                        TextAttribute.Text(text = "Warning AirySnackbar with padding top 48dp and bottom 24dp"),
                        IconAttribute.Icon(iconRes = R.drawable.ic_warning),
                        SizeAttribute.Padding(top = 48, bottom = 24, unit = SizeUnit.DP)
                    )
                ).show()
            }

            buttonInfo.setOnClickListener {
                AirySnackbar.make(
                    source = AirySnackbarSource.ActivitySource(activity = this@SampleUsageActivity),
                    type = Type.Info,
                    attributes =
                    listOf(
                        TextAttribute.Text(text = "Info AirySnackbar with top margin 16dp and no icon"),
                        IconAttribute.NoIcon,
                        SizeAttribute.Margin(top = 16, unit = SizeUnit.DP),
                        AnimationAttribute.SlideInOut
                    )
                ).show()
            }

            buttonCustom.setOnClickListener {
                AirySnackbar.make(
                    source = AirySnackbarSource.ViewSource(view = binding.buttonCustom),
                    type = Type.Custom(bgColor = R.color.teal_700),
                    attributes =
                    listOf(
                        TextAttribute.Text(text = "Custom color bg and custom icon with tint AirySnackbar"),
                        TextAttribute.TextColor(textColor = R.color.black),
                        IconAttribute.Icon(iconRes = R.drawable.ic_custom),
                        IconAttribute.IconColor(iconTint = R.color.teal_200),
                        SizeAttribute.Margin(left = 24, right = 24, unit = SizeUnit.DP),
                        SizeAttribute.Padding(top = 12, bottom = 12, unit = SizeUnit.DP),
                        RadiusAttribute.Radius(radius = 8f),
                        GravityAttribute.Top,
                        AnimationAttribute.FadeInOut
                    )
                ).show()
            }
        }
    }
}