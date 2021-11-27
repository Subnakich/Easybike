package ru.subnak.easybike.domain.usecases.journey

import androidx.lifecycle.LiveData
import ru.subnak.easybike.domain.model.Journey
import ru.subnak.easybike.domain.repositories.JourneyRepository

class GetJourneyWithJourneyValuesListUseCase(private val journeyRepository: JourneyRepository) {

    fun getJourneyWithJourneyValuesList(): LiveData<List<Journey>> {
        return journeyRepository.getJourneyWithJourneyValuesList()
    }
}