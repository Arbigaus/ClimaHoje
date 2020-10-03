package com.arbigaus.climahoje.View

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arbigaus.climahoje.Interfaces.LoadImageReceiverDelegate
import com.arbigaus.climahoje.Model.DataStore
import com.arbigaus.climahoje.Model.DayInfo
import com.arbigaus.climahoje.R

class DayInfoAdapter(var dayInfoList: MutableList<DayInfo>)
        : RecyclerView.Adapter<DayInfoAdapter.DayInfoHolder>(), LoadImageReceiverDelegate {
    var holderRef: DayInfoHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayInfoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_day_info, parent, false)
        return DayInfoHolder(view)
    }

    override fun getItemCount(): Int {
        return dayInfoList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DayInfoHolder, position: Int) {
        val dayInfoItem = dayInfoList.get(position)

        holderRef = holder

        holder.dayText.text = dayInfoItem.day
        holder.weatherText.text = dayInfoItem.weatherText
        holder.degreesText.text =
            """${dayInfoItem.maxDegree}° / ${dayInfoItem.minDegree}℃"""   // \u00B0 \u2103

        DataStore.getImageIcon(dayInfoItem.icon + "@4x.png", this)
    }

    inner class DayInfoHolder(view: View) : RecyclerView.ViewHolder(view) {
        var dayText: TextView = view.findViewById(R.id.dayText)
        var degreesText: TextView = view.findViewById(R.id.degreesText)
        var weatherText: TextView = view.findViewById(R.id.weatherText)
        var cardImageView: ImageView = view.findViewById(R.id.cardImageView)
    }

    override fun setUpdatedImage(status: Boolean, image: Bitmap?) {
        if (status) {
            holderRef?.cardImageView?.setImageBitmap(image)
        }
        else {
            Log.d("Error!", "Algo deu errado")
        }
    }
}