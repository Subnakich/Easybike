package ru.subnak.easybike.presentation.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.subnak.easybike.data.repositoriesimpl.UserRepositoryImpl
import ru.subnak.easybike.domain.model.User
import ru.subnak.easybike.domain.usecases.user.AddUserUseCase
import ru.subnak.easybike.domain.usecases.user.EditUserUseCase
import ru.subnak.easybike.domain.usecases.user.GetUserUseCase

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepositoryImpl(application)

    private val getUserUseCase = GetUserUseCase(repository)
    private val addUserUseCase = AddUserUseCase(repository)
    private val editUserUseCase = EditUserUseCase(repository)


    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputAge = MutableLiveData<Boolean>()
    val errorInputAge: LiveData<Boolean>
        get() = _errorInputAge

    private val _errorInputWeight = MutableLiveData<Boolean>()
    val errorInputWeight: LiveData<Boolean>
        get() = _errorInputWeight

    private val _errorInputHeight = MutableLiveData<Boolean>()
    val errorInputHeight: LiveData<Boolean>
        get() = _errorInputHeight

    private val _errorInputSex = MutableLiveData<Boolean>()
    val errorInputSex: LiveData<Boolean>
        get() = _errorInputSex

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getUser(userId: Int) {
        viewModelScope.launch {
            val item = getUserUseCase.getUser(userId)
            _user.value = item
        }
    }

    fun addUser(inputName: String?, inputAge: String?, inputWeight: String?, inputHeight: String?, inputSex: String?, userID: Int) {
        val name = parseName(inputName)
        val age = parseAge(inputAge)
        val weight = parseWeight(inputWeight)
        val height = parseHeight(inputHeight)
        val sex = parseSex(inputSex)
        val fieldsValid = validateInput(name, age, weight, height, sex)
        if (fieldsValid) {
            viewModelScope.launch {
                val user = User(name, age, weight, height, sex, userID)
                addUserUseCase.addUser(user)
                finishWork()
            }
        }
    }

    fun editUser(inputName: String?, inputAge: String?, inputWeight: String?, inputHeight: String?, inputSex: String?, userID: Int) {
        val name = parseName(inputName)
        val age = parseAge(inputAge)
        val weight = parseWeight(inputWeight)
        val height = parseHeight(inputHeight)
        val sex = parseSex(inputSex)
        val fieldsValid = validateInput(name, age, weight, height, sex)
        if (fieldsValid) {
            _user.value?.let {
                viewModelScope.launch {
                    val user = it.copy(name = name, age = age, weight = weight, height = height, sex = sex, userID = userID)
                    editUserUseCase.editUser(user)
                    finishWork()
                }
            }
        }
    }

    private fun validateInput(name: String, age: Int, weight: Int, height: Int, sex: String): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (age <= 0) {
            _errorInputAge.value = true
            result = false
        }
        if (weight <= 0) {
            _errorInputWeight.value = true
            result = false
        }
        if (height <= 0) {
            _errorInputHeight.value = true
            result = false
        }
        if (sex.isBlank()) {
            _errorInputSex.value = true
            result = false
        }
        return result
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseSex(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseAge(inputAge: String?): Int {
        return try {
            inputAge?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun parseWeight(inputWeight: String?): Int {
        return try {
            inputWeight?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun parseHeight(inputHeight: String?): Int {
        return try {
            inputHeight?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputAge() {
        _errorInputAge.value = false
    }

    fun resetErrorInputWeight() {
        _errorInputWeight.value = false
    }

    fun resetErrorInputHeight() {
        _errorInputHeight.value = false
    }

    fun resetErrorInputSex() {
        _errorInputSex.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}