package ru.subnak.easybike.domain.user

class GetUserUseCase(private val userRepository: UserRepository) {

    suspend fun getUser(userID: Int): User {
        return userRepository.getUser(userID)
    }
}