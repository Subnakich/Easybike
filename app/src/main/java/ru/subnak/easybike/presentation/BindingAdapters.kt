package ru.subnak.easybike.presentation

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
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