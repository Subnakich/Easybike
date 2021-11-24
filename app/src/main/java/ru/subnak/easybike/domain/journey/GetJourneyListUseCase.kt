package ru.subnak.easybike.domain.journey

import androidx.lifecycle.LiveData

class GetJourneyListUseCase(private val journeyRepository: JourneyRepository) {

    fun getJourneyList(): LiveData<List<Journey>> {
        return journeyRepository.getJourneyList()
    }
}