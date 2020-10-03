package com.arbigaus.climahoje.Interfaces

import android.graphics.Bitmap

interface LoadImageReceiverDelegate {
    fun setUpdatedImage(status: Boolean, image: Bitmap?)
}