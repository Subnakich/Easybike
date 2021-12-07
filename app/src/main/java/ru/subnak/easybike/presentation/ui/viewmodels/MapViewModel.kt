package ru.subnak.easybike.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.subnak.easybike.data.entity.JourneyDbModel
import ru.subnak.easybike.domain.model.Journey
import ru.subnak.easybike.domain.repositories.JourneyRepository

class MapViewModel constructor(val journeyRepository: JourneyRepository): ViewModel() {

        fun addJourney(journey: Journey) = viewModelScope.launch {
            journeyRepository.addJourney(journey)
        }

        fun deleteJourney(journey: Journey) = viewModelScope.launch {
            journeyRepository.deleteJourney(journey)
        }

        val getJourneyList = journeyRepository.getJourneyList()

    }
