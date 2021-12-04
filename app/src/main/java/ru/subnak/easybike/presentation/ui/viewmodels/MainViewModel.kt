package ru.subnak.easybike.presentation.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.subnak.easybike.data.repositoriesimpl.UserRepositoryImpl
import ru.subnak.easybike.domain.usecases.user.CheckUserUseCase

class MainViewModel(application: Application) : AndroidViewModel(application) {
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