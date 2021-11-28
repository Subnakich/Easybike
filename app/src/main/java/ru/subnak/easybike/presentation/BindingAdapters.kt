package ru.subnak.easybike.presentation

import android.widget.TextView
import androidx.databinding.BindingAdapter
import ru.subnak.easybike.R
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("bindDistanceToString")
fun bindDistanceToString(textView: TextView, count: Double) {
    textView.text = String.format(
        textView.context.getString(R.string.distance_with_km),
        count
    )
}

@BindingAdapter("bindSpeedToString")
fun bindSpeedToString(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.speed_with_kmh),
        count
    )
}

@BindingAdapter("bindDateToString")
fun bindDateToString(textView: TextView, count: Long) {
    textView.text = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(count)
}

@BindingAdapter("bindDurationToString")
fun bindDurationToString(textView: TextView, count: Long) {
    textView.text = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(count)
}