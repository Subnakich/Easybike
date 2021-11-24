package ru.subnak.easybike.domain.user

class EditUserUseCase(private val userRepository: UserRepository) {

    suspend fun editUser(user: User) {
        userRepository.editUser(user)
    }
}