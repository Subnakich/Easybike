package ru.subnak.easybike.presentation.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.subnak.easybike.R
import ru.subnak.easybike.data.repositoriesimpl.UserRepositoryImpl
import ru.subnak.easybike.domain.model.User
import ru.subnak.easybike.domain.usecases.user.CheckUserUseCase
import kotlin.properties.Delegates

class UserCreateViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepositoryImpl(application)

    private val checkUserUseCase = CheckUserUseCase(repository)

    private var _userCheck = MutableLiveData<Int>()
    val userCheck: LiveData<Int>
        get() = _userCheck

    fun checkUser(userID: Int) {
        viewModelScope.launch {
            val item = checkUserUseCase.checkUser(userID)
            _userCheck.value = item
        }

    }
}