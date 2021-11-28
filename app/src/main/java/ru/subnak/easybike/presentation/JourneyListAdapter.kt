package ru.subnak.easybike.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.subnak.easybike.databinding.JourneyBinding
import ru.subnak.easybike.domain.model.Journey

class JourneyListAdapter : ListAdapter<Journey, JourneyViewHolder>(JourneyDiffCallback()) {

    var onJourneyLongClickListener: ((Journey) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JourneyViewHolder {
        val binding = JourneyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JourneyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JourneyViewHolder, position: Int) {
        val journey = getItem(position)
        val binding = holder.binding
        binding.root.setOnLongClickListener {
            onJourneyLongClickListener?.invoke(journey)
            true
        }
        binding.journey = journey
    }
}