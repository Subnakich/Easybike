package ru.subnak.easybike.domain.journey

class AddJourneyUseCase(private val journeyRepository: JourneyRepository) {

    suspend fun addJourney(journey: Journey) {
        journeyRepository.addJourney(journey)
    }
}