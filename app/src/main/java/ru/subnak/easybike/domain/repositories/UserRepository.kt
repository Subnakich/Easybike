package ru.subnak.easybike.domain.repositories

import ru.subnak.easybike.domain.model.User

interface UserRepository {

    suspend fun addUser(user: User)

    suspend fun editUser(user: User)

    suspend fun getUser(userID: Int): User
}