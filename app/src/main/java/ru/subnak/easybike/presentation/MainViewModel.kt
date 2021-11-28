package ru.subnak.easybike.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.subnak.easybike.data.repositoriesimpl.JourneyRepositoryImpl
import ru.subnak.easybike.data.repositoriesimpl.UserRepositoryImpl
import ru.subnak.easybike.domain.usecases.journey.AddJourneyUseCase
import ru.subnak.easybike.domain.usecases.journey.DeleteJourneyUseCase
import ru.subnak.easybike.domain.usecases.journey.GetJourneyListUseCase
import ru.subnak.easybike.domain.usecases.journey.GetJourneyUseCase
import ru.subnak.easybike.domain.usecases.user.AddUserUseCase
import ru.subnak.easybike.domain.usecases.user.EditUserUseCase
import ru.subnak.easybike.domain.usecases.user.GetUserUseCase

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val repository = UserRepositoryImpl(application)

    private val addUserUseCase = AddUserUseCase(repository)
    private val editUserUseCase = EditUserUseCase(repository)
    private val getUserUseCase = GetUserUseCase(repository)

    private val repository1 = JourneyRepositoryImpl(application)

    private val addJourneyUseCase = AddJourneyUseCase(repository1)
    private val getJourneyUseCase = GetJourneyUseCase(repository1)
    private val deleteJourneyUseCase = DeleteJourneyUseCase(repository1)
    private val getJourneyListUseCase = GetJourneyListUseCase(repository1)

    val journeyList = getJourneyListUseCase.getJourneyList()



}