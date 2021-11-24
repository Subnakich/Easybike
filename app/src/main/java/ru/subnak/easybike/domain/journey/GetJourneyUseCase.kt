package ru.subnak.easybike.domain.journey

class GetJourneyUseCase(private val journeyRepository: JourneyRepository) {

    suspend fun getJourney(journeyID: Int): Journey {
        return journeyRepository.getJourney(journeyID)
    }
}