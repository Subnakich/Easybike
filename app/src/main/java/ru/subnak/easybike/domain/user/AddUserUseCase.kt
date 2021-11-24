package ru.subnak.easybike.domain.user

class AddUserUseCase(private val userRepository: UserRepository) {

    suspend fun addUser(user: User) {
        userRepository.addUser(user)
    }
}