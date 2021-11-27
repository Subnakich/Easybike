package ru.subnak.easybike.domain.journeyvalues

import androidx.lifecycle.LiveData

class GetJourneyValueListUseCase(private val journeyValueRepository: JourneyValueRepository) {

    fun getJourneyValueList(journeyID: Int): LiveData<List<JourneyValue>> {
        return journeyValueRepository.getJourneyValueList(journeyID)
    }
}