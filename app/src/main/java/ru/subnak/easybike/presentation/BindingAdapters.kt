package ru.subnak.easybike.presentation

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import ru.subnak.easybike.R
import ru.subnak.easybike.presentation.utils.TrackingObject.getFormattedStopTime
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@BindingAdapter("bindDistanceToString")
fun bindDistanceToString(textView: TextView, count: Double) {
    count.roundToInt()
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
    textView.text = getFormattedStopTime(count)
}

@BindingAdapter("bindIMG")
fun bindIMG(imageView: ImageView, bmp: Bitmap) {
    imageView.setImageBitmap(bmp)
}

@BindingAdapter("errorInputName")
fun bindErrorInputName(textInputLayout: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        textInputLayout.context.getString(R.string.error_input_name)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("errorInputAge")
fun bindErrorInputAge(textInputLayout: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        textInputLayout.context.getString(R.string.error_input_age)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("errorInputWeight")
fun bindErrorInputWeight(textInputLayout: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        textInputLayout.context.getString(R.string.error_input_weight)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("errorInputHeight")
fun bindErrorInputHeight(textInputLayout: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        textInputLayout.context.getString(R.string.error_input_height)
    } else {
        null
    }
    textInputLayout.error = message
}