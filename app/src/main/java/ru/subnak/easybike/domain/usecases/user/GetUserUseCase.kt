package ru.subnak.easybike.domain.usecases.user

import ru.subnak.easybike.domain.model.User
import ru.subnak.easybike.domain.repositories.UserRepository

class GetUserUseCase(private val userRepository: UserRepository) {

    suspend fun getUser(userID: Int): User {
        return userRepository.getUser(userID)
    }
}