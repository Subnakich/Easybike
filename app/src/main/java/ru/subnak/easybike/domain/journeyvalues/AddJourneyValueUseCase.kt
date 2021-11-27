package ru.subnak.easybike.domain.journeyvalues

class AddJourneyValueUseCase(private val journeyValueRepository: JourneyValueRepository) {

        suspend fun addJourneyValue(journeyValue: JourneyValue) {
            journeyValueRepository.addJourneyValue(journeyValue)
        }
}