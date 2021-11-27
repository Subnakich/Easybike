package ru.subnak.easybike.domain.repositories

import androidx.lifecycle.LiveData
import ru.subnak.easybike.domain.model.JourneyValue

interface JourneyValueRepository {

    suspend fun addJourneyValue(journeyValue: JourneyValue)

    fun getJourneyValueList(journeyID: Int): LiveData<List<JourneyValue>>
}