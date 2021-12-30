package ru.subnak.easybike.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.subnak.easybike.databinding.JourneyBinding
import ru.subnak.easybike.domain.model.Journey
import ru.subnak.easybike.presentation.ui.diffcallbacks.JourneyDiffCallback
import ru.subnak.easybike.presentation.ui.viewholders.JourneyViewHolder

class JourneyListAdapter : ListAdapter<Journey, JourneyViewHolder>(JourneyDiffCallback()) {

    var onJourneyLongClickListener: ((Journey) -> Unit)? = null
    var onJourneyClickListener: ((Journey) -> Unit)? = null

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
        binding.root.setOnClickListener {
            onJourneyClickListener?.invoke(journey)
        }
        binding.journey = journey
    }
}