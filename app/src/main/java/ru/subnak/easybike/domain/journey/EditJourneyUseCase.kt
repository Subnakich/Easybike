package ru.subnak.easybike.domain.journey

class EditJourneyUseCase(private val journeyRepository: JourneyRepository) {

    suspend fun editJourney(journey: Journey) {
        journeyRepository.editJourney(journey)
    }
}