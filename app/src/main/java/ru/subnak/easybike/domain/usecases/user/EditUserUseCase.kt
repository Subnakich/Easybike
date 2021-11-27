package ru.subnak.easybike.domain.usecases.user

import ru.subnak.easybike.domain.model.User
import ru.subnak.easybike.domain.repositories.UserRepository

class EditUserUseCase(private val userRepository: UserRepository) {

    suspend fun editUser(user: User) {
        userRepository.editUser(user)
    }
}