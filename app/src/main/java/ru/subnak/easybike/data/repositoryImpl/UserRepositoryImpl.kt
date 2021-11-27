package ru.subnak.easybike.data.repositoryImpl

import android.app.Application
import ru.subnak.easybike.data.AppDatabase
import ru.subnak.easybike.data.mapper.UserMapper
import ru.subnak.easybike.domain.user.User
import ru.subnak.easybike.domain.user.UserRepository

class UserRepositoryImpl(
    application: Application
): UserRepository {

    private val userDao = AppDatabase.getInstance(application).userDao()
    private val mapper = UserMapper()

    override suspend fun addUser(user: User) {
        userDao.addUser(mapper.mapEntityToDbModel(user))
    }

    override suspend fun editUser(user: User) {
        userDao.addUser(mapper.mapEntityToDbModel(user))
    }

    override suspend fun getUser(userID: Int): User {
        val dbModel = userDao.getUser(userID)
        return mapper.mapDbModelToEntity(dbModel)
    }
}