package com.akndmr.library

@JvmInline
value class VolumeLevel private constructor(val value: Float) {
    init {
        require(value in 0f..1f) { "Volume level must be between 0.0 and 1.0" }
    }

    companion object {
        fun of(value: Float): VolumeLevel = VolumeLevel(value.coerceIn(0f, 1f))
    }
}