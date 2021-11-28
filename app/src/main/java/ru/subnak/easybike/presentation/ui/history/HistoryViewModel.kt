package ru.subnak.easybike.presentation.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.subnak.easybike.data.repositoriesimpl.JourneyRepositoryImpl
import ru.subnak.easybike.domain.model.Journey
import ru.subnak.easybike.domain.usecases.journey.AddJourneyUseCase
import ru.subnak.easybike.domain.usecases.journey.DeleteJourneyUseCase
import ru.subnak.easybike.domain.usecases.journey.GetJourneyListUseCase
import ru.subnak.easybike.domain.usecases.journey.GetJourneyUseCase

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = JourneyRepositoryImpl(application)

    private val addJourneyUseCase = AddJourneyUseCase(repository)
    private val getJourneyUseCase = GetJourneyUseCase(repository)
    private val deleteJourneyUseCase = DeleteJourneyUseCase(repository)
    private val getJourneyListUseCase = GetJourneyListUseCase(repository)

    val journeyList = getJourneyListUseCase.getJourneyList()

    fun deleteJourney(journey: Journey) {
        viewModelScope.launch {
            deleteJourneyUseCase.deleteJourney(journey)
        }
    }

    fun addJourney(journey: Journey) {
        viewModelScope.launch {
            addJourneyUseCase.addJourney(journey)
        }
    }

}