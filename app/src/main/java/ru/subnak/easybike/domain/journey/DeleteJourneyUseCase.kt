package ru.subnak.easybike.domain.journey

class DeleteJourneyUseCase(private val journeyRepository: JourneyRepository) {

    suspend fun deleteJourney(journey: Journey) {
        journeyRepository.deleteJourney(journey)
    }
}