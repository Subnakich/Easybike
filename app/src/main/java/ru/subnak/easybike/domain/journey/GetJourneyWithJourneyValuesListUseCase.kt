package ru.subnak.easybike.domain.journey

import androidx.lifecycle.LiveData

class GetJourneyWithJourneyValuesListUseCase(private val journeyRepository: JourneyRepository) {

    fun getJourneyWithJourneyValuesList(): LiveData<List<Journey>> {
        return journeyRepository.getJourneyWithJourneyValuesList()
    }
}