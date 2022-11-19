package com.akndmr.airysnackbar.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akndmr.airysnackbar.R
import com.akndmr.airysnackbar.databinding.ActivityAiryBinding
import com.akndmr.library.*

class AiryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAiryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()

        with(binding) {
            buttonSuccess.setOnClickListener {
                AirySnackbar.make(
                    source = AirySnackbarSource.ActivitySource(this@AiryActivity),
                    type = Type.Success,
                    attributes = listOf(
                        TextAttribute.Text(text = "Successful AirySnackbar sample some longer text"),
                        SizeAttribute.Padding(
                            left = resources.getDimensionPixelSize(R.dimen.sample_margin_small),
                            right = resources.getDimensionPixelSize(R.dimen.sample_margin_small)
                        ),
                        SizeAttribute.Margin(
                            right = resources.getDimensionPixelSize(R.dimen.sample_margin_medium),
                            left = resources.getDimensionPixelSize(R.dimen.sample_margin_medium)
                        ),
                        RadiusAttribute.Radius(1.0f),
                        TextAttribute.TextSize(
                            size = resources.getDimension(
                                com.akndmr.library.R.dimen.snackbar_big_text_size
                            )
                        )
                    )
                ).show()
            }

            buttonError.setOnClickListener {
                AirySnackbar.make(
                    source = AirySnackbarSource.ActivitySource(this@AiryActivity),
                    type = Type.Error,
                    attributes = listOf(
                        TextAttribute.Text(text = "Error AirySnackbar"),
                        IconAttribute.Icon(iconRes = R.drawable.ic_error),
                        SizeAttribute.Padding(
                            left = resources.getDimensionPixelSize(R.dimen.sample_margin_small),
                            right = resources.getDimensionPixelSize(R.dimen.sample_margin_small)
                        ),
                        SizeAttribute.Margin(
                            right = resources.getDimensionPixelSize(R.dimen.sample_margin_medium),
                            left = resources.getDimensionPixelSize(R.dimen.sample_margin_medium)
                        ),
                        TextAttribute.TextSize(
                            size = resources.getDimension(
                                com.akndmr.library.R.dimen.snackbar_default_text_size
                            )
                        )
                    )
                ).show()
            }

            buttonWarning.setOnClickListener {
                AirySnackbar.make(
                    source = AirySnackbarSource.ActivitySource(this@AiryActivity),
                    type = Type.Warning,
                    attributes = listOf(
                        TextAttribute.Text(
                            text = "Warning AirySnackbar Warning AirySnackbar " +
                                    "Warning AirySnackbar Warning AirySnackbar Warning AirySnackbar"
                        ),
                        IconAttribute.Icon(iconRes = R.drawable.ic_warning)
                    )
                ).show()
            }

            buttonInfo.setOnClickListener {
                AirySnackbar.make(
                    source = AirySnackbarSource.ActivitySource(this@AiryActivity),
                    type = Type.Info,
                    attributes = listOf(
                        TextAttribute.Text(text = "Info AirySnackbar with top margin"),
                        IconAttribute.NoIcon,
                        SizeAttribute.Margin(
                            top = resources.getDimensionPixelSize(
                                R.dimen.sample_margin_small
                            )
                        ),
                        GravityAttribute.Bottom,
                        AnimationAttribute.SlideInOut
                    )
                ).show()
            }

            buttonCustom.setOnClickListener {
                AirySnackbar.make(
                    source = AirySnackbarSource.ActivitySource(this@AiryActivity),
                    type = Type.Custom(bgColor = R.color.cabbage),
                    attributes = listOf(
                        TextAttribute.Text(text = "Custom colored AirySnackbar"),
                        TextAttribute.TextColor(textColor = R.color.lavander),
                        IconAttribute.Icon(iconRes = R.drawable.ic_custom),
                        IconAttribute.IconColor(iconTint = R.color.teal_200)
                    )
                ).show()
            }
        }

    }

    private fun initBinding() {
        binding = ActivityAiryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}