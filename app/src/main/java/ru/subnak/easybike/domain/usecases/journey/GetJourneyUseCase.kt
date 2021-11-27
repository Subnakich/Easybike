package ru.subnak.easybike.domain.usecases.journey

import ru.subnak.easybike.domain.model.Journey
import ru.subnak.easybike.domain.repositories.JourneyRepository

class GetJourneyUseCase(private val journeyRepository: JourneyRepository) {

    suspend fun getJourney(journeyID: Int): Journey {
        return journeyRepository.getJourney(journeyID)
    }
}