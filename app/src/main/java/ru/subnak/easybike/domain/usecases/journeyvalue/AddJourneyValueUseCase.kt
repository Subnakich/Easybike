package ru.subnak.easybike.domain.usecases.journeyvalue

import ru.subnak.easybike.domain.model.JourneyValue
import ru.subnak.easybike.domain.repositories.JourneyValueRepository

class AddJourneyValueUseCase(private val journeyValueRepository: JourneyValueRepository) {

        suspend fun addJourneyValue(journeyValue: JourneyValue) {
            journeyValueRepository.addJourneyValue(journeyValue)
        }
}