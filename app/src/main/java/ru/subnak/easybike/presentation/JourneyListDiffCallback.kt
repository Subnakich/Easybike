package ru.subnak.easybike.presentation

import androidx.recyclerview.widget.DiffUtil
import ru.subnak.easybike.domain.model.Journey

class JourneyListDiffCallback(
    private val oldList: List<Journey>,
    private val newList: List<Journey>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.journeyID == newItem.journeyID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}