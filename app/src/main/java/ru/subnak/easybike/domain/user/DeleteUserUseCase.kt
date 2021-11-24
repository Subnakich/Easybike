package ru.subnak.easybike.domain.user

class DeleteUserUseCase(private val userRepository: UserRepository) {

    suspend fun deleteUser(user: User) {
        userRepository.deleteUser(user)
    }
}