package ru.subnak.easybike.presentation.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.subnak.easybike.data.repositoriesimpl.UserRepositoryImpl
import ru.subnak.easybike.domain.model.User
import ru.subnak.easybike.domain.usecases.user.GetUserUseCase

class StatisticViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepositoryImpl(application)

    private val getUserUseCase = GetUserUseCase(repository)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun getUser(userId: Int) {
        viewModelScope.launch {
            val item = getUserUseCase.getUser(userId)
            _user.value = item
        }
    }
}