package ru.subnak.easybike.domain.user

interface UserRepository {

    suspend fun addUser(user: User)

    suspend fun deleteUser(user: User)

    suspend fun editUser(user: User)

    suspend fun getUser(userID: Int): User
}