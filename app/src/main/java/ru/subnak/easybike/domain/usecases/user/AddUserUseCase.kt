package ru.subnak.easybike.domain.usecases.user

import ru.subnak.easybike.domain.model.User
import ru.subnak.easybike.domain.repositories.UserRepository

class AddUserUseCase(private val userRepository: UserRepository) {

    suspend fun addUser(user: User) {
        userRepository.addUser(user)
    }
}