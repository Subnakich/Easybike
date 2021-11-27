package ru.subnak.easybike.domain.usecases.journeyvalue

import androidx.lifecycle.LiveData
import ru.subnak.easybike.domain.model.JourneyValue
import ru.subnak.easybike.domain.repositories.JourneyValueRepository

class GetJourneyValueListUseCase(private val journeyValueRepository: JourneyValueRepository) {

    fun getJourneyValueList(journeyID: Int): LiveData<List<JourneyValue>> {
        return journeyValueRepository.getJourneyValueList(journeyID)
    }
}