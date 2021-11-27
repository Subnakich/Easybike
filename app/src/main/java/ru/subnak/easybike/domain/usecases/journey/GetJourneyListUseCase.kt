package ru.subnak.easybike.domain.usecases.journey

import androidx.lifecycle.LiveData
import ru.subnak.easybike.domain.model.Journey
import ru.subnak.easybike.domain.repositories.JourneyRepository

class GetJourneyListUseCase(private val journeyRepository: JourneyRepository) {

    fun getJourneyList(): LiveData<List<Journey>> {
        return journeyRepository.getJourneyList()
    }
}