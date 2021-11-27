package ru.subnak.easybike.domain.journeyvalues

import androidx.lifecycle.LiveData

interface JourneyValueRepository {

    suspend fun addJourneyValue(journeyValue: JourneyValue)

    fun getJourneyValueList(journeyID: Int): LiveData<List<JourneyValue>>
}