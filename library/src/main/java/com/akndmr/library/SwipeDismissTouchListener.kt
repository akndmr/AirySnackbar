package com.akndmr.library

import android.animation.Animator
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewPropertyAnimator
import kotlin.math.abs

class SwipeDismissTouchListener(
    private val view: View,
    private val config: SwipeConfig,
    private val onDismiss: () -> Unit
) : View.OnTouchListener {

    private sealed class SwipeState {
        object Idle : SwipeState()
        object Dragging : SwipeState()
        object Animating : SwipeState()
    }

    private val touchSlop = ViewConfiguration.get(view.context).scaledTouchSlop
    private var startX = 0f
    private var dX = 0f
    private var currentState: SwipeState = SwipeState.Idle
    private var currentAnimator: ViewPropertyAnimator? = null

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (currentState == SwipeState.Animating) {
                    currentAnimator?.cancel()
                }
                startX = event.rawX
                dX = view.x - startX
                currentState = SwipeState.Idle
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val distance = abs(event.rawX - startX)
                if (currentState == SwipeState.Idle && distance > touchSlop) {
                    val isMovingRight = event.rawX > startX
                    when (config.swipeDirection) {
                        is SwipeConfig.SwipeDirection.LeftOnly -> {
                            if (!isMovingRight) currentState = SwipeState.Dragging
                        }
                        is SwipeConfig.SwipeDirection.RightOnly -> {
                            if (isMovingRight) currentState = SwipeState.Dragging
                        }
                        is SwipeConfig.SwipeDirection.Both -> {
                            currentState = SwipeState.Dragging
                        }
                    }
                }
                
                if (currentState == SwipeState.Dragging) {
                    val newX = event.rawX + dX
                    val constrainedX = when (config.swipeDirection) {
                        is SwipeConfig.SwipeDirection.LeftOnly -> {
                            newX.coerceIn(-view.width.toFloat(), 0f)
                        }
                        is SwipeConfig.SwipeDirection.RightOnly -> {
                            newX.coerceIn(0f, view.width.toFloat())
                        }
                        is SwipeConfig.SwipeDirection.Both -> {
                            newX.coerceIn(-view.width.toFloat(), view.width.toFloat())
                        }
                    }
                    view.x = constrainedX
                    
                    val swipeProgress = abs(view.x) / (view.width * config.dismissThreshold)
                    val alpha = (config.maxAlpha - swipeProgress * config.alphaProgressFactor)
                        .coerceIn(config.minAlpha, config.maxAlpha)
                    view.alpha = alpha
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (currentState == SwipeState.Dragging) {
                    val swipeProgress = abs(view.x) / (view.width * config.dismissThreshold)
                    if (swipeProgress >= 1f && event.action == MotionEvent.ACTION_UP) {
                        val direction = view.x > 0
                        when (config.swipeDirection) {
                            is SwipeConfig.SwipeDirection.LeftOnly -> {
                                if (!direction) animateDismiss(direction)
                                else animateReset()
                            }
                            is SwipeConfig.SwipeDirection.RightOnly -> {
                                if (direction) animateDismiss(direction)
                                else animateReset()
                            }
                            is SwipeConfig.SwipeDirection.Both -> {
                                animateDismiss(direction)
                            }
                        }
                    } else {
                        animateReset()
                    }
                }
                currentState = SwipeState.Idle
                return true
            }
        }
        return false
    }

    private fun animateDismiss(toRight: Boolean) {
        currentState = SwipeState.Animating
        val targetX = if (toRight) view.width else -view.width
        currentAnimator = view.animate()
            .x(targetX.toFloat())
            .alpha(0f)
            .setDuration(config.animationDuration)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    currentState = SwipeState.Idle
                    onDismiss()
                }
                override fun onAnimationCancel(animation: Animator) {
                    currentState = SwipeState.Idle
                }
                override fun onAnimationRepeat(animation: Animator) {}
            })
    }

    private fun animateReset() {
        currentState = SwipeState.Animating
        val originalX = 0f
        currentAnimator = view.animate()
            .x(originalX)
            .alpha(config.maxAlpha)
            .setDuration(config.animationDuration)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    currentState = SwipeState.Idle
                }
                override fun onAnimationCancel(animation: Animator) {
                    currentState = SwipeState.Idle
                }
                override fun onAnimationRepeat(animation: Animator) {}
            })
    }

    companion object {
        const val DEFAULT_DISMISS_THRESHOLD = 0.3f
        const val DEFAULT_ANIMATION_DURATION = 200L
    }
} 