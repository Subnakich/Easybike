package ru.subnak.easybike.domain.usecases.user

import ru.subnak.easybike.domain.repositories.UserRepository

class CheckUserUseCase(private val userRepository: UserRepository) {

    suspend fun checkUser(userID: Int): Int {
        return userRepository.checkUser(userID)
    }
}