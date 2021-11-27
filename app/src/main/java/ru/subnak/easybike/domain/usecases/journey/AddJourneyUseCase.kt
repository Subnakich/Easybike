package ru.subnak.easybike.domain.usecases.journey

import ru.subnak.easybike.domain.model.Journey
import ru.subnak.easybike.domain.repositories.JourneyRepository

class AddJourneyUseCase(private val journeyRepository: JourneyRepository) {

    suspend fun addJourney(journey: Journey) {
        journeyRepository.addJourney(journey)
    }
}