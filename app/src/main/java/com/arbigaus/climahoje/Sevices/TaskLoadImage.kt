package com.arbigaus.climahoje.Sevices

import android.graphics.Bitmap
import android.os.AsyncTask
import com.arbigaus.climahoje.Interfaces.LoadImageReceiverDelegate
import com.arbigaus.climahoje.Interfaces.LoadReceiverDelegate
import com.arbigaus.climahoje.Model.DataStore

class TaskLoadImage(private var delegate: LoadImageReceiverDelegate, image: String) : AsyncTask<String, Int, Bitmap>() {
    private val network = Network(image, Request.icon, true)

    override fun doInBackground(vararg p0: String?): Bitmap? {
        return network.getImageBitmap()
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        delegate.setUpdatedImage(true, result)
    }

}