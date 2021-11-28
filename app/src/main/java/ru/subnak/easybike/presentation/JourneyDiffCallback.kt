package ru.subnak.easybike.presentation

import androidx.recyclerview.widget.DiffUtil
import ru.subnak.easybike.domain.model.Journey

class JourneyDiffCallback: DiffUtil.ItemCallback<Journey>()  {

    override fun areItemsTheSame(oldItem: Journey, newItem: Journey): Boolean {
        return oldItem.journeyID == newItem.journeyID
    }

    override fun areContentsTheSame(oldItem: Journey, newItem: Journey): Boolean {
        return oldItem == newItem
    }
}