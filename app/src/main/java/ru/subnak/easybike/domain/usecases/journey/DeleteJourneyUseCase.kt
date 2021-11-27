package ru.subnak.easybike.domain.usecases.journey

import ru.subnak.easybike.domain.model.Journey
import ru.subnak.easybike.domain.repositories.JourneyRepository

class DeleteJourneyUseCase(private val journeyRepository: JourneyRepository) {

    suspend fun deleteJourney(journey: Journey) {
        journeyRepository.deleteJourney(journey)
    }
}